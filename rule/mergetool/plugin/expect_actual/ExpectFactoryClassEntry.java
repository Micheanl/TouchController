package top.fifthlight.mergetools.merger.plugin.expectactual;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import top.fifthlight.mergetools.merger.api.MergeEntry;
import top.fifthlight.mergetools.processor.ActualData;
import top.fifthlight.mergetools.processor.ExpectData;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

public record ExpectFactoryClassEntry(ExpectActualPluginContext context, String interfaceFullQualifiedName,
                                      ExpectData expectData, String aspectProviderInterface,
                                      String aspectProviderFactory) implements MergeEntry {
    public ExpectFactoryClassEntry(ExpectActualPluginContext context, String interfaceFullQualifiedName, ExpectData expectData) {
        this(context, interfaceFullQualifiedName, expectData, null, null);
    }

    public ExpectFactoryClassEntry withAspectProvider(String aspectProviderInterface, String aspectProviderFactory) {
        return new ExpectFactoryClassEntry(context, interfaceFullQualifiedName, expectData, aspectProviderInterface, aspectProviderFactory);
    }

    private record MethodPair(String parameterTypes, String name) {
        public MethodPair(ExpectData.Constructor constructor) {
            this(Arrays.stream(constructor.parameters()).map(ExpectData.Constructor.Parameter::type).collect(Collectors.joining()), constructor.name());
        }

        public MethodPair(ActualData.Constructor constructor) {
            this(Arrays.stream(constructor.parameters()).map(ActualData.Constructor.Parameter::type).collect(Collectors.joining()), constructor.name());
        }
    }

    @Override
    public void write(OutputStream output) throws Exception {
        var expectBinaryName = expectData.interfaceName();
        var factoryInternalName = ExpectActualUtils.descriptorNameToInternalName(expectBinaryName) + "Factory";

        var classWriter = new ClassWriter(0);
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, factoryInternalName, null, "java/lang/Object", null);

        if (aspectProviderInterface != null) {
            writeDelegateMethods(classWriter, expectBinaryName, factoryInternalName);
        } else {
            writeDirectCallMethods(classWriter, expectBinaryName);
        }

        classWriter.visitEnd();
        output.write(classWriter.toByteArray());
    }

    private void writeDirectCallMethods(ClassWriter classWriter, String expectBinaryName) {
        var actualData = context.getActualDataMap().get(interfaceFullQualifiedName);

        var expectConstructors = Arrays.stream(expectData.constructors()).collect(Collectors.toMap(
                MethodPair::new,
                constructor -> constructor,
                (a, b) -> {
                    throw new IllegalStateException("Duplicate expect constructors: " + a + ", " + b);
                }
        ));
        var actualConstructors = Arrays.stream(actualData.constructors()).collect(Collectors.toMap(
                MethodPair::new,
                constructor -> constructor,
                (a, b) -> {
                    throw new IllegalStateException("Duplicate actual constructors: " + a + ", " + b);
                }
        ));

        for (var expectConstructorPair : expectConstructors.entrySet()) {
            var methodPair = expectConstructorPair.getKey();
            var expectConstructor = expectConstructorPair.getValue();
            var actualConstructor = actualConstructors.get(methodPair);
            if (actualConstructor == null) {
                throw new IllegalStateException("No actual constructor found for method pair " + methodPair);
            }

            var generatedMethodDescriptor = "(" + methodPair.parameterTypes() + ")" + expectBinaryName;
            var methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, expectConstructor.name(), generatedMethodDescriptor, null, null);
            methodVisitor.visitCode();

            var actualClassName = ExpectActualUtils.descriptorNameToInternalName(actualData.implementationName());
            ExpectActualUtils.generateDirectCallMethodBody(
                    methodVisitor, actualConstructor, expectConstructor,
                    actualClassName, methodPair.parameterTypes(), true
            );

            methodVisitor.visitEnd();
        }
    }

    private void writeDelegateMethods(ClassWriter classWriter, String expectBinaryName, String factoryInternalName) {
        ExpectActualUtils.generateAspectProviderField(classWriter, factoryInternalName, aspectProviderInterface, aspectProviderFactory);

        for (var expectConstructor : expectData.constructors()) {
            var generatedMethodDescriptor = "(" +
                    Arrays.stream(expectConstructor.parameters()).map(ExpectData.Constructor.Parameter::type).collect(Collectors.joining()) +
                    ")" + expectBinaryName;
            var methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, expectConstructor.name(), generatedMethodDescriptor, null, null);
            methodVisitor.visitCode();
            ExpectActualUtils.generateAspectCallMethodBody(methodVisitor, factoryInternalName, generatedMethodDescriptor, aspectProviderInterface, expectConstructor, true);
            methodVisitor.visitEnd();
        }
    }
}
