package top.fifthlight.mergetools.merger.plugin.expectactual;

import com.fasterxml.jackson.databind.ObjectMapper;
import top.fifthlight.mergetools.merger.api.AttributeEnvironment;
import top.fifthlight.mergetools.merger.api.MergeEntry;
import top.fifthlight.mergetools.merger.api.Plugin;
import top.fifthlight.mergetools.merger.api.PreprocessEnvironment;
import top.fifthlight.mergetools.processor.ActualData;
import top.fifthlight.mergetools.processor.AspectData;
import top.fifthlight.mergetools.processor.ExpectData;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class ExpectActualPlugin implements Plugin, ExpectActualPluginContext {
    @Override
    public int priority() {
        return 1000;
    }

    private static final String expectPrefix = "META-INF/expects/";
    private static final String actualPrefix = "META-INF/actuals/";
    private static final String aspectManifestPath = "META-INF/aspect.json";
    private static final String jsonSuffix = ".json";
    private final ObjectMapper mapper = new ObjectMapper();
    private final HashMap<String, ExpectData> expectDataMap = new HashMap<>();
    private final HashMap<String, ActualData> actualDataMap = new HashMap<>();
    private final HashSet<String> factoryClasses = new HashSet<>();

    private String aspectClassName;
    private String aspectImplPackageSuffix = null;
    private final List<AspectData> aspectDependencies = new ArrayList<>();

    private final HashMap<String, AspectData.ExpectEntry> upstreamExpects = new HashMap<>();
    private String aspectProviderFactory = null;

    @Override
    public Map<String, ActualData> getActualDataMap() {
        return actualDataMap;
    }

    @Override
    public Map<String, AspectData.ExpectEntry> getUpstreamExpectsMap() {
        return upstreamExpects;
    }

    public String getAspectProviderInterface() {
        return aspectClassName;
    }

    public String getAspectProviderFactory() {
        return aspectProviderFactory;
    }

    @Override
    public boolean processArg(String arg, PreprocessEnvironment environment) {
        try {
            if ("--aspect-class".equals(arg)) {
                aspectClassName = ExpectActualUtils.fqnToInternalName(environment.readNextArg());
                aspectProviderFactory = aspectClassName + "Factory";
                return true;
            }
            if ("--aspect-impl-package-suffix".equals(arg)) {
                aspectImplPackageSuffix = environment.readNextArg();
                return true;
            }
            if ("--aspect".equals(arg)) {
                var path = environment.resolvePath(Path.of(environment.readNextArg()));
                try (var jarFile = new JarFile(path.toFile())) {
                    var entry = jarFile.getJarEntry(aspectManifestPath);
                    if (entry == null) {
                        throw new IllegalStateException("Aspect JAR missing " + aspectManifestPath + ": " + path);
                    }
                    try (var inputStream = new BufferedInputStream(jarFile.getInputStream(entry));
                         var reader = new InputStreamReader(inputStream)) {
                        var aspectData = mapper.readValue(reader, AspectData.class);
                        aspectDependencies.add(aspectData);
                    }
                }
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    @Override
    public boolean processJarEntry(JarFile file, JarEntry entry, PreprocessEnvironment environment) throws IOException {
        var name = entry.getName();
        if (name.startsWith(expectPrefix) && name.endsWith(jsonSuffix)) {
            try (var inputStream = new BufferedInputStream(file.getInputStream(entry));
                 var reader = new InputStreamReader(inputStream)) {
                var expectData = mapper.readValue(reader, ExpectData.class);
                var interfaceFullQualifiedName = name.substring(expectPrefix.length(), name.length() - jsonSuffix.length());
                var interfaceClassPath = ExpectActualUtils.descriptorNameToInternalName(expectData.interfaceName());
                var interfaceFactoryPath = interfaceClassPath + "Factory.class";
                expectDataMap.put(interfaceFullQualifiedName, expectData);
                factoryClasses.add(interfaceFactoryPath);
                // Overwrite factory class if it exists
                environment.putMergeEntry(interfaceFactoryPath, new ExpectFactoryClassEntry(this, interfaceFullQualifiedName, expectData));
            }
            return true;
        } else if (name.startsWith(actualPrefix) && name.endsWith(jsonSuffix)) {
            try (var inputStream = new BufferedInputStream(file.getInputStream(entry));
                 var reader = new InputStreamReader(inputStream)) {
                var actualData = mapper.readValue(reader, ActualData.class);
                var interfaceFullQualifiedName = name.substring(actualPrefix.length(), name.length() - jsonSuffix.length());
                if (actualDataMap.containsKey(interfaceFullQualifiedName)) {
                    throw new IllegalStateException("Duplicate actual expectData: " + interfaceFullQualifiedName);
                }
                actualDataMap.put(interfaceFullQualifiedName, actualData);
            }
            return true;
        }

        // Filter generated factory classes out in case expect manifest entries are processed first
        return factoryClasses.contains(name);
    }

    private String computeImplInternalName(String aspectProviderInternalName) {
        if (aspectImplPackageSuffix == null || aspectImplPackageSuffix.isEmpty()) {
            return aspectProviderInternalName + "Impl";
        }
        var lastSlash = aspectProviderInternalName.lastIndexOf('/');
        var basePath = aspectProviderInternalName.substring(0, lastSlash);
        var shortName = aspectProviderInternalName.substring(lastSlash + 1);
        var suffixPath = aspectImplPackageSuffix.replace('.', '/');
        return basePath + "/" + suffixPath + "/" + shortName + "Impl";
    }

    @Override
    public void preSorting(Map<String, MergeEntry> mergeEntries, Map<String, String> manifestEntries, AttributeEnvironment environment) {
        // Step 1: Process dependent Aspect JARs
        for (var aspectData : aspectDependencies) {
            var hasDownstreamDelegates = false;
            for (var expectEntry : aspectData.expects()) {
                var fqn = ExpectActualUtils.internalNameToFqn(ExpectActualUtils.descriptorNameToInternalName(expectEntry.interfaceName()));
                if (!actualDataMap.containsKey(fqn)) {
                    // Unsolved from upstream
                    if (aspectClassName != null) {
                        // Pass it to downstream aspects
                        hasDownstreamDelegates = true;
                        upstreamExpects.put(fqn, expectEntry);
                    } else {
                        throw new IllegalStateException("Aspect expect not satisfied: " + fqn);
                    }
                } else {
                    // Clean existing SPI files
                    var spiManifestPath = "META-INF/services/" + fqn + "$Factory";
                    mergeEntries.remove(spiManifestPath);
                }
            }

            // Generate AspectProviderImpl class entry
            var aspectProviderInternalName = ExpectActualUtils.descriptorNameToInternalName(aspectData.aspectProviderInterface());
            var aspectProviderFqn = ExpectActualUtils.internalNameToFqn(aspectProviderInternalName);
            var implInternalName = computeImplInternalName(aspectProviderInternalName);
            mergeEntries.put(implInternalName + ".class", new AspectProviderImplEntry(this, hasDownstreamDelegates, aspectData, implInternalName));

            // Generate SPI manifest for generated AspectProviderImpl
            var servicesPath = "META-INF/services/" + aspectProviderFqn;
            var implFqn = ExpectActualUtils.internalNameToFqn(implInternalName);
            mergeEntries.put(servicesPath, new ServiceLoaderRegistrationEntry(implFqn));
        }

        // Step 2: Solve expects
        // Unsolved expects will be provided by aspects
        var unresolvedExpects = new ArrayList<ExpectData>();
        for (var expectEntry : expectDataMap.entrySet()) {
            var key = expectEntry.getKey();
            var expectData = expectEntry.getValue();
            var actualData = actualDataMap.get(key);

            // Clean SPI files
            var spiManifestPath = "META-INF/services/" + key + "$Factory";
            mergeEntries.remove(spiManifestPath);

            if (actualData != null) {
                // Clean actual SPI factory
                var actualSpiFactoryPath = ExpectActualUtils.descriptorNameToInternalName(actualData.spiFactoryName()) + ".class";
                mergeEntries.remove(actualSpiFactoryPath);
            } else {
                if (aspectClassName == null) {
                    throw new IllegalStateException("Missing actual class for: " + key);
                }
                // Replace factory class to aspect backed
                var interfaceClassPath = ExpectActualUtils.descriptorNameToInternalName(expectData.interfaceName());
                var factoryPath = interfaceClassPath + "Factory.class";
                var existing = (ExpectFactoryClassEntry) mergeEntries.get(factoryPath);
                mergeEntries.put(factoryPath, existing.withAspectProvider(aspectClassName, aspectProviderFactory));

                unresolvedExpects.add(expectData);
            }
        }

        // Step 3: Generate aspect files
        if (aspectClassName != null) {
            if (unresolvedExpects.isEmpty() && upstreamExpects.isEmpty()) {
                throw new IllegalStateException("Aspect JAR must have unresolved expects");
            }

            var aspectExpectEntries = Stream.concat(
                            unresolvedExpects.stream().map(e -> new AspectData.ExpectEntry(e.interfaceName(), e.constructors())),
                            upstreamExpects.values().stream().map(e -> new AspectData.ExpectEntry(e.interfaceName(), e.constructors()))).toList();
            mergeEntries.put(aspectClassName + ".class",
                    new AspectProviderInterfaceEntry(aspectClassName, aspectExpectEntries));
            mergeEntries.put(aspectClassName + "Factory.class",
                    new AspectProviderFactoryEntry(aspectClassName));
            var aspectData = new AspectData(ExpectActualUtils.internalNameToDescriptor(aspectClassName), ExpectActualUtils.internalNameToDescriptor(aspectProviderFactory), aspectExpectEntries);
            mergeEntries.put(aspectManifestPath, new AspectManifestEntry(aspectData));
        }
    }
}
