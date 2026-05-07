package top.fifthlight.mergetools.merger.plugin.expectactual;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import top.fifthlight.mergetools.merger.api.MergeEntry;
import top.fifthlight.mergetools.processor.ExpectData;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AspectProviderInterfaceEntry implements MergeEntry {
    private final String aspectClassName;
    private final List<ExpectData> unresolvedExpects;

    public AspectProviderInterfaceEntry(String aspectClassName, List<ExpectData> unresolvedExpects) {
        this.aspectClassName = aspectClassName;
        this.unresolvedExpects = unresolvedExpects;
    }

    @Override
    public void write(OutputStream output) throws Exception {
        var classWriter = new ClassWriter(0);
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_INTERFACE | Opcodes.ACC_ABSTRACT, aspectClassName, null, "java/lang/Object", null);

        for (var expectData : unresolvedExpects) {
            var interfaceName = expectData.interfaceName();
            for (var constructor : expectData.constructors()) {
                var descriptor = "(" +
                        Arrays.stream(constructor.parameters()).map(ExpectData.Constructor.Parameter::type).collect(Collectors.joining()) +
                        ")" + interfaceName;
                classWriter.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_ABSTRACT, constructor.name(), descriptor, null, null);
            }
        }

        classWriter.visitEnd();
        output.write(classWriter.toByteArray());
    }
}
