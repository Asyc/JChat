package me.asyc.jchat.client.network.packet.impl.inbound;

import io.netty.channel.Channel;
import me.asyc.jchat.client.network.packet.impl.outbound.ClientHandshakePacket;
import me.asyc.jchat.network.packet.InboundPacket;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public final class ServerHandshakePacket implements InboundPacket {

    private Channel channel;
    private byte[] key, validate;

    @Override
    public void read(Channel channel, byte[] buffer) throws IOException {
        this.channel = channel;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(buffer));
        this.key = new byte[in.readInt()];
        in.readFully(this.key);
        this.validate = new byte[in.readInt()];
        in.readFully(this.validate);
    }

    @Override
    public void handle() {
        this.channel.writeAndFlush(new ClientHandshakePacket(this.key, this.validate));
    }

    @Override
    public short getPacketID() {
        return 0x00;
    }
}
