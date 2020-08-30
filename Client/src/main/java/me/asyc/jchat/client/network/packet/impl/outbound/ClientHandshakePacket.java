package me.asyc.jchat.client.network.packet.impl.outbound;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import me.asyc.jchat.client.JChatClient;
import me.asyc.jchat.client.encryption.ClientCryptoManager;
import me.asyc.jchat.network.encryption.CryptoKey;
import me.asyc.jchat.network.packet.OutboundPacket;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.security.PublicKey;

public class ClientHandshakePacket implements OutboundPacket {

    private static final AttributeKey<PublicKey> RSA_KEY_ATTRIBUTE = AttributeKey.valueOf("jchat:rsa_key");
    private static final AttributeKey<CryptoKey> KEY_ATTRIBUTE = AttributeKey.valueOf("jchat:key");

    private Channel channel;

    private final CryptoKey secret;
    private final byte[] validate;

    public ClientHandshakePacket(CryptoKey secret, byte[] validate) {
        this.secret = secret;
        this.validate = validate;
    }

    @Override
    public void write(Channel channel, OutputStream stream) throws Exception {
        this.channel = channel;

        ClientCryptoManager cryptoManager = JChatClient.INSTANCE.getCryptoManager();

        DataOutputStream out = new DataOutputStream(stream);

        PublicKey key = channel.attr(ClientHandshakePacket.RSA_KEY_ATTRIBUTE).get();
        out.write(cryptoManager.encryptRSA(key, this.secret.getKey().getEncoded()));
        out.write(cryptoManager.encryptRSA(key, cryptoManager.encrypt(this.secret, this.validate)));
    }

    @Override
    public void postWrite() {
        this.channel.attr(ClientHandshakePacket.KEY_ATTRIBUTE).set(this.secret);
    }

    @Override
    public int getEstimatedSize() {
        return 128 * 2;
    }

    @Override
    public short getPacketID() {
        return 0x01;
    }
}
