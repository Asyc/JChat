package me.asyc.jchat.server.network.packet.impl.outbound;

import io.netty.channel.Channel;
import me.asyc.jchat.network.packet.OutboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public final class ServerHandshakePacket implements OutboundPacket {

    private final byte[] key, validate;

    public ServerHandshakePacket(byte[] key, byte[] validate) {
        this.key = key;
        this.validate = validate;
    }

    @Override
    public void write(Channel channel, OutputStream stream) throws IOException {
        DataOutputStream out = new DataOutputStream(stream);
        out.writeInt(this.key.length);
        out.write(this.key);
        out.writeInt(this.validate.length);
        out.write(this.validate);
        out.flush();
    }

    @Override
    public short getPacketID() {
        return 0x00;
    }

    @Override
    public int getEstimatedSize() {
        return 4 + this.key.length + 4 + this.validate.length;
    }
}
