package me.asyc.jchat.client.network.packet.impl.outbound;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import me.asyc.jchat.client.JChatClient;
import me.asyc.jchat.network.packet.OutboundPacket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.PublicKey;

public class ClientHandshakePacket implements OutboundPacket {

    private static final AttributeKey<PublicKey> RSA_KEY_ATTRIBUTE = AttributeKey.valueOf("jchat:rsa_key");

    private final byte[] secret, validate;

    public ClientHandshakePacket(byte[] secret, byte[] validate) {
        this.secret = secret;
        this.validate = validate;
    }

    @Override
    public void write(Channel channel, OutputStream stream) throws Exception {
        DataOutputStream out = new DataOutputStream(stream);
        out.write(JChatClient.INSTANCE.getCryptoManager().encryptRSA(channel.attr(ClientHandshakePacket.RSA_KEY_ATTRIBUTE).get(), this.secret));
        out.write(JChatClient.INSTANCE.getCryptoManager().encryptRSA(channel.attr(ClientHandshakePacket.RSA_KEY_ATTRIBUTE).get(), this.validate));
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
