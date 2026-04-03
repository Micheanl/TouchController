package top.fifthlight.mergetools.merger.plugin.expectactual;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import top.fifthlight.mergetools.merger.api.MergeEntry;

import java.io.OutputStream;

public class AspectProviderFactoryEntry implements MergeEntry {
    private final String aspectClassName;

    public AspectProviderFactoryEntry(String aspectClassName) {
        this.aspectClassName = aspectClassName;
    }

    @Override
    public void write(OutputStream output) throws Exception {
        var providerInternalName = aspectClassName;
        var factoryInternalName = providerInternalName + "Factory";

        var classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        classWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC | Opcodes.ACC_SUPER, factoryInternalName, null, "java/lang/Object", null);

        classWriter.visitField(Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL,
                "instance", ExpectActualUtils.internalNameToDescriptor(providerInternalName), null, null);
        {
            var clinit = classWriter.visitMethod(Opcodes.ACC_STATIC, "<clinit>", "()V", null, null);
            clinit.visitCode();

            // ServiceLoader<AspectProvider> loader = ServiceLoader.load(AspectProvider.class)
            var loaderStart = new Label();
            // ServiceLoader.load(AspectProvider.class)
            clinit.visitLdcInsn(Type.getType(ExpectActualUtils.internalNameToDescriptor(providerInternalName)));
            clinit.visitMethodInsn(Opcodes.INVOKESTATIC, "java/util/ServiceLoader", "load", "(Ljava/lang/Class;)Ljava/util/ServiceLoader;", false);
            // loader = ServiceLoader.load(AspectProvider.class)
            clinit.visitLabel(loaderStart);
            clinit.visitVarInsn(Opcodes.ASTORE, 0);

            // Iterator<AspectProvider> iterator = loader.iterator();
            var iteratorStart = new Label();
            // load loader
            clinit.visitVarInsn(Opcodes.ALOAD, 0);
            // loader.iterator()
            clinit.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/util/ServiceLoader", "iterator", "()Ljava/util/Iterator;", false);
            // iterator = loader.iterator()
            clinit.visitLabel(iteratorStart);
            clinit.visitVarInsn(Opcodes.ASTORE, 1);

            // if (iterator.hasNext())
            // pop iterator
            clinit.visitVarInsn(Opcodes.ALOAD, 1);
            clinit.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Iterator", "hasNext", "()Z", true);
            // Jump to dontHasNext if false
            var dontHasNext = new Label();
            var end = new Label();
            clinit.visitJumpInsn(Opcodes.IFEQ, dontHasNext);

            // instance = iterator.next()
            // load iterator
            clinit.visitVarInsn(Opcodes.ALOAD, 1);
            clinit.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/Iterator", "next", "()Ljava/lang/Object;", true);
            // cast to AspectProvider
            clinit.visitTypeInsn(Opcodes.CHECKCAST, providerInternalName);
            clinit.visitFieldInsn(Opcodes.PUTSTATIC, factoryInternalName, "instance", ExpectActualUtils.internalNameToDescriptor(providerInternalName));
            clinit.visitJumpInsn(Opcodes.GOTO, end);

            // else
            clinit.visitLabel(dontHasNext);
            // throw new RuntimeException("No aspect provider found for AspectProvider");
            clinit.visitTypeInsn(Opcodes.NEW, "java/lang/RuntimeException");
            clinit.visitInsn(Opcodes.DUP);
            clinit.visitLdcInsn("No aspect provider found for " + aspectClassName);
            clinit.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/RuntimeException", "<init>", "(Ljava/lang/String;)V", false);
            clinit.visitInsn(Opcodes.ATHROW);

            // end
            clinit.visitLabel(end);
            clinit.visitInsn(Opcodes.RETURN);

            clinit.visitLocalVariable("loader", "Ljava/util/ServiceLoader;", null, loaderStart, end, 0);
            clinit.visitLocalVariable("iterator", "Ljava/util/Iterator;", null, iteratorStart, end, 1);

            clinit.visitMaxs(3, 2);
            clinit.visitEnd();
        }

        {
            var getInstance = classWriter.visitMethod(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC, "getInstance", ExpectActualUtils.internalNametoNoArgMethodDescriptor(providerInternalName), null, null);
            getInstance.visitCode();
            getInstance.visitFieldInsn(Opcodes.GETSTATIC, factoryInternalName, "instance", ExpectActualUtils.internalNameToDescriptor(providerInternalName));
            getInstance.visitInsn(Opcodes.ARETURN);
            getInstance.visitMaxs(1, 0);
            getInstance.visitEnd();
        }

        classWriter.visitEnd();
        output.write(classWriter.toByteArray());
    }
}
