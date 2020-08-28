package me.asyc.jchat.network.packet.factory;

import me.asyc.jchat.network.utils.ClassUtils;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public final class ByteCodeGenerator {

	private ByteCodeGenerator() {
		throw new RuntimeException("cannot instantiate utility class");
	}

	public static byte[] createFactoryClass(String className, Class<?> factoryInterface, String methodName, Class<?> returnType) {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

		cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, className, null, ClassUtils.getClassPath(Object.class), new String[]{ClassUtils.getClassPath(factoryInterface)});

		String returnTypePath = ClassUtils.getClassPath(returnType);
		{
			MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", ClassUtils.createMethodDescriptor(void.class), null, null);
			mv.visitCode();
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, ClassUtils.getClassPath(Object.class), "<init>", ClassUtils.createMethodDescriptor(void.class), false);
			mv.visitInsn(Opcodes.RETURN);
			mv.visitMaxs(0, 0); // Have to use so ASM will generate the method's max stack/locals automatically
			mv.visitEnd();
		}
		{
			MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, methodName, ClassUtils.createMethodDescriptor(returnType.getInterfaces()[0]), null, null);
			mv.visitCode();
			mv.visitTypeInsn(Opcodes.NEW, returnTypePath);
			mv.visitInsn(Opcodes.DUP);
			mv.visitMethodInsn(Opcodes.INVOKESPECIAL, returnTypePath, "<init>", ClassUtils.createMethodDescriptor(void.class), false);
			mv.visitInsn(Opcodes.ARETURN);
			mv.visitMaxs(0, 0);
			mv.visitEnd();
		}

		cw.visitEnd();
		return cw.toByteArray();
	}

}
