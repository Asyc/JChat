package me.asyc.jchat.network.packet.factory;

import me.asyc.jchat.network.packet.InboundPacket;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;

/**
 * A class used to generate dynamic implementation classes of the {@link PacketFactory} class.
 *
 * Used to reduce the overhead of reflection.
 */
public final class PacketFactorySupplier {

    private static final String OBJECT_CLASS_PATH = getClassPath(Object.class.getName());
    private static final String PACKET_FACTORY_PATH = getClassPath(PacketFactory.class.getName());
    private static final String INBOUND_PACKET_PATH = getClassPath(InboundPacket.class.getName());

    /**
     * A newly generated instance of {@link PacketFactory} will be set here. Then returned by {@link PacketFactorySupplier#createPacketFactory(String, Class)}. (Used to obtain a handle to the instance)
     */
    private static volatile PacketFactory cachedPacketFactory;

    /**
     * Dynamically generates and loads a new implementation of {@link PacketFactory}
     *
     * @param className The name of the class to generate
     * @param returnType The return type of the class to generate
     * @return Returns an instance of the new generated class
     * @throws UnsupportedOperationException Thrown if the class has already been generated
     */
    public static synchronized PacketFactory createPacketFactory(String className, Class<? extends InboundPacket> returnType) throws UnsupportedOperationException {
        try {   // Check if the class already is defined
            Class.forName(className);
            throw new UnsupportedOperationException();
        } catch (ClassNotFoundException ignore) {}

        String classPath = PacketFactorySupplier.getClassPath(className);
        String returnTypePath = PacketFactorySupplier.getClassPath(returnType.getName());

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        cw.visit(V1_8, ACC_PUBLIC, classPath, null, null, new String[]{PacketFactorySupplier.PACKET_FACTORY_PATH});

        {
            MethodVisitor mv = cw.visitMethod(ACC_STATIC, "<clinit>", "()V", null, null);
            mv.visitCode();

            mv.visitTypeInsn(NEW, returnTypePath);
            mv.visitMethodInsn(INVOKESPECIAL, returnTypePath, "<init>", "()V", false);

            mv.visitMaxs(0, 0); //Have to use so ASM will generate the method's max stack/locals automatically
            mv.visitEnd();
        }
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();

            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, PacketFactorySupplier.OBJECT_CLASS_PATH, "<init>", "()V", false);

            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESTATIC, returnTypePath, "setCachedPacketFactory", "(L" + PacketFactorySupplier.PACKET_FACTORY_PATH + ";)V", false);

            mv.visitInsn(Opcodes.RETURN);

            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        {
            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "createPacket", "()L" + PacketFactorySupplier.INBOUND_PACKET_PATH + ';', null, null);
            mv.visitCode();

            mv.visitTypeInsn(NEW, returnTypePath);
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, returnTypePath, "<init>", "()V", false);
            mv.visitInsn(ARETURN);

            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }

        cw.visitEnd();
        DynamicClassLoader.INSTANCE.defineClass(className, cw.toByteArray());

        PacketFactory factory = PacketFactorySupplier.cachedPacketFactory;
        PacketFactorySupplier.cachedPacketFactory = null;   //Make sure to set to allow object to be garbage collected when needed
        return factory;
    }

    /**
     * This method is used in the dynamically generated classes.
     * Sets the current {@link PacketFactorySupplier#cachedPacketFactory} to the parameter.
     *
     * @param cachedPacketFactory The instance of the {@link PacketFactory} object.
     */
    @SuppressWarnings("unused")
    public static synchronized void setCachedPacketFactory(PacketFactory cachedPacketFactory) {
        PacketFactorySupplier.cachedPacketFactory = cachedPacketFactory;
    }

    /**
     * @param name The name of the class
     * @return Returns the internal name of the provided class
     */
    private static String getClassPath(String name) {
        return name.replace('.', '/');
    }

    /**
     * Singleton class loader that allows definition of dynamically generated class
     * by providing the its data buffer
     */
    private static class DynamicClassLoader extends ClassLoader {
        public static final DynamicClassLoader INSTANCE = new DynamicClassLoader();

        private DynamicClassLoader() {
            super();
        }

        public void defineClass(String name, byte[] buffer) {
            super.defineClass(name, buffer, 0, buffer.length);
        }
    }
}
