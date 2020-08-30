package me.asyc.jchat.server.network.packet.impl.inbound;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import me.asyc.jchat.network.encryption.CryptoKey;
import me.asyc.jchat.network.packet.InboundPacket;
import me.asyc.jchat.server.JChatServer;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

public final class ClientHandshakePacket implements InboundPacket {

    private static final AttributeKey<CryptoKey> KEY_ATTRIBUTE = AttributeKey.valueOf("jchat:key");
    private static final AttributeKey<byte[]> VALIDATE_BUFFER_ATTRIBUTE = AttributeKey.valueOf("jchat:validate");

    private Channel channel;
    private byte[] key;
    private byte[] validate, originalValidate;

    @Override
    public void read(Channel channel, byte[] buffer) throws IOException {
        this.channel = channel;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(buffer));
        this.key = new byte[in.readInt()];
        in.readFully(this.key);
        this.validate = new byte[in.readInt()];
        in.readFully(this.validate);

        this.originalValidate = channel.attr(ClientHandshakePacket.VALIDATE_BUFFER_ATTRIBUTE).getAndSet(null);
    }

    @Override
    public void handle() {
        if (!Arrays.equals(this.originalValidate, this.validate)) {
            System.err.println("Client(" + this.channel.remoteAddress().toString() + "): Handshake secret does not match original secret. Disconnecting.");
            this.channel.close();
        }

        this.channel.attr(ClientHandshakePacket.KEY_ATTRIBUTE).set(JChatServer.INSTANCE.getCryptoManager().createKeyFromBuffer(this.key));
    }

    @Override
    public short getPacketID() {
        return 0x01;
    }
}
