package me.asyc.jchat.network.packet;

import io.netty.channel.Channel;

import java.io.IOException;
import java.io.OutputStream;

public interface OutboundPacket extends Packet {
	void write(Channel channel, OutputStream out) throws IOException;
}
