package me.asyc.jchat.client.network.packet.factory;

import me.asyc.jchat.client.network.packet.impl.inbound.ServerHandshakePacket;
import me.asyc.jchat.network.exception.UnknownPacketException;
import me.asyc.jchat.network.packet.InboundPacket;
import me.asyc.jchat.network.packet.factory.PacketFactory;
import me.asyc.jchat.network.packet.factory.PacketFactorySupplier;
import me.asyc.jchat.network.packet.factory.PacketResolver;

public enum PacketList {
    SERVER_HANDSHAKE(ServerHandshakePacket.class),

    ;
    public static final PacketResolver PACKET_RESOLVER = new PacketResolver() {
        @Override
        public InboundPacket createPacketFromID(short id) throws UnknownPacketException {
            return PacketList.createPacketFromID(id);
        }
    };

    private final PacketFactory factory;

    PacketList(Class<? extends InboundPacket> clazz) {
        String className = "me.asyc.jchat.server.network.packet.factory.impl." + PacketList.getClassName(clazz);
        this.factory = PacketFactorySupplier.createPacketFactory(className, clazz);
    }

    public static InboundPacket createPacketFromID(short id) {
        try {
            return PacketList.values()[id].factory.createPacket();
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    private static String getClassName(Class<?> clazz) {
        String[] name = clazz.getName().split("\\.");
        return name[name.length - 1];
    }
}
