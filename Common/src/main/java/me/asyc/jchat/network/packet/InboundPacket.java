package me.asyc.jchat.network.packet;

import io.netty.channel.Channel;

import java.io.IOException;

public interface InboundPacket extends Packet {
    void read(Channel channel, byte[] in) throws IOException;
    void handle();
}
