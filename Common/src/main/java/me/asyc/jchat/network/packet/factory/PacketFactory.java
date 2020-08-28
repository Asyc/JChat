package me.asyc.jchat.network.packet.factory;

import me.asyc.jchat.network.packet.InboundPacket;

public interface PacketFactory {
	InboundPacket createPacket();
}
