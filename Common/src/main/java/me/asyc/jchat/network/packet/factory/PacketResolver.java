package me.asyc.jchat.network.packet.factory;

import me.asyc.jchat.network.packet.InboundPacket;

public interface GlobalPacketFactory {
	InboundPacket createPacketFromID(short id);
	short maxPacketID();
}
