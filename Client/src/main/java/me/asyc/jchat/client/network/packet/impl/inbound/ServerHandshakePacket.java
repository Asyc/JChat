package me.asyc.jchat.client.network.packet.impl.inbound;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import me.asyc.jchat.client.JChatClient;
import me.asyc.jchat.client.encryption.ClientCryptoManager;
import me.asyc.jchat.client.network.packet.impl.outbound.ClientHandshakePacket;
import me.asyc.jchat.network.encryption.CryptoKey;
import me.asyc.jchat.network.encryption.CryptoManager;
import me.asyc.jchat.network.packet.InboundPacket;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

public final class ServerHandshakePacket implements InboundPacket {

    private static final AttributeKey<PublicKey> RSA_KEY_ATTRIBUTE = AttributeKey.valueOf("jchat:rsa_key");
    private static final AttributeKey<CryptoKey> KEY_ATTRIBUTE = AttributeKey.valueOf("jchat:key");

    private Channel channel;
    private byte[] key, validate;

    @Override
    public void read(Channel channel, byte[] buffer) throws IOException {
        this.channel = channel;

        DataInputStream in = new DataInputStream(new ByteArrayInputStream(buffer));
        this.key = new byte[128];
        in.readFully(this.key);

        this.validate = new byte[128];
        in.readFully(this.validate);
    }

    @Override
    public void handle() throws Exception {
        ClientCryptoManager cryptoManager = JChatClient.INSTANCE.getCryptoManager();
        this.channel.attr(ServerHandshakePacket.RSA_KEY_ATTRIBUTE).set(cryptoManager.createKeyFromBufferRSA(this.key));

        this.channel.writeAndFlush(new ClientHandshakePacket(cryptoManager.generateKey(), this.validate));
    }

    @Override
    public short getPacketID() {
        return 0x00;
    }
}
