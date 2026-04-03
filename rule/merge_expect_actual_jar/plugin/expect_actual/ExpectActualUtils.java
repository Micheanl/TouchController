package top.fifthlight.mergetools.merger.plugin.expectactual;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import top.fifthlight.mergetools.processor.ActualData;
import top.fifthlight.mergetools.processor.ExpectData;

public class ExpectActualUtils {
    public static String descriptorNameToInternalName(String descriptor) {
        if (!descriptor.startsWith("L") || !descriptor.endsWith(";")) {
            throw new IllegalArgumentException("Invalid descriptor: " + descriptor);
        }
        return descriptor.substring(1, descriptor.length() - 1);
    }

    public static String internalNameToDescriptor(String internalName) {
        return "L" + internalName + ";";
    }

    public static String internalNameToFqn(String internalName) {
        return internalName.replace('/', '.');
    }

    public static String internalNametoNoArgMethodDescriptor(String returnInternalName) {
        return "()L" + returnInternalName + ";";
    }

    public static String fqnToInternalName(String fqn) {
        return fqn.replace('.', '/');
    }

    public static Label[] loadParameters(MethodVisitor mv, ExpectData.Constructor.Parameter[] parameters, int startIndex) {
        var variableLabels = new Label[parameters.length];
        for (var i = 0; i < parameters.length; i++) {
            var parameter = parameters[i];
            var label = new Label();
            variableLabels[i] = label;
            mv.visitLabel(label);
            var objType = parameter.type().charAt(0);
            switch (objType) {
                case 'L' -> mv.visitVarInsn(Opcodes.ALOAD, i + startIndex);
                case 'Z', 'B', 'S', 'C', 'I' -> mv.visitVarInsn(Opcodes.ILOAD, i + startIndex);
                case 'J' -> mv.visitVarInsn(Opcodes.LLOAD, i + startIndex);
                case 'F' -> mv.visitVarInsn(Opcodes.FLOAD, i + startIndex);
                case 'D' -> mv.visitVarInsn(Opcodes.DLOAD, i + startIndex);
            }
        }
        return variableLabels;
    }

    public static void generateDirectCallMethodBody(
            MethodVisitor mv,
            ActualData.Constructor actualConstructor,
            ExpectData.Constructor expectConstructor,
            String actualClassName,
            String parameterTypes,
            String returnType,
            boolean isStatic
    ) {
        switch (actualConstructor.type()) {
            case CONSTRUCTOR -> {
                mv.visitTypeInsn(Opcodes.NEW, actualClassName);
                mv.visitInsn(Opcodes.DUP);
            }
            case STATIC_METHOD -> {
            }
        }

        var localOffset = isStatic ? 0 : 1;

        var parameters = expectConstructor.parameters();
        var variableLabels = loadParameters(mv, parameters, localOffset);

        switch (actualConstructor.type()) {
            case CONSTRUCTOR -> {
                var constructorDescriptor = "(" + parameterTypes + ")V";
                mv.visitMethodInsn(Opcodes.INVOKESPECIAL, actualClassName, "<init>", constructorDescriptor, false);
            }
            case STATIC_METHOD -> {
                var actualMethodDescriptor = "(" + parameterTypes + ")" + returnType;
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, actualClassName, expectConstructor.name(), actualMethodDescriptor, false);
            }
        }
        var endLabel = new Label();
        mv.visitLabel(endLabel);
        mv.visitInsn(Opcodes.ARETURN);

        for (var i = 0; i < parameters.length; i++) {
            var parameter = parameters[i];
            mv.visitLocalVariable(parameter.name(), parameter.type(), null, variableLabels[i], endLabel, i + localOffset);
        }
        mv.visitMaxs(parameters.length + 2, parameters.length + localOffset);
    }
}
