package me.asyc.jchat.server.network.packet.factory;

import me.asyc.jchat.network.packet.InboundPacket;
import me.asyc.jchat.network.packet.Packet;
import me.asyc.jchat.network.packet.factory.ByteCodeGenerator;
import me.asyc.jchat.network.packet.factory.PacketFactory;
import me.asyc.jchat.server.utils.ByteClassLoader;
import me.asyc.jchat.server.utils.ClassUtils;

public enum PacketList {
	;

	private PacketFactory factory;

	PacketList(Class<? extends InboundPacket> clazz) {
		String className = "me/asyc/jchat/server/network/packet/factory/impl/" + ClassUtils.getClassName(clazz);
		ByteClassLoader classLoader = new ByteClassLoader();
		Class<?> created = classLoader.defineClass(className.replace('/', '.'), ByteCodeGenerator.createFactoryClass(className, PacketFactory.class, "createPacket", clazz));

		try {
			this.factory = (PacketFactory) created.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("failed to create packet factory classes");
			System.exit(-1);
		}
	}

	public static short maxPacketID() {
		return (short) (PacketList.values().length - 1);
	}

	public static Packet createPacketByID(short id) {
		return PacketList.values()[id].factory.createPacket();
	}
}
