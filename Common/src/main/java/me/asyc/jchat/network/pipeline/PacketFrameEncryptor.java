package me.asyc.jchat.network.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.asyc.jchat.network.encryption.CryptoKey;
import me.asyc.jchat.network.encryption.CryptoManager;

public final class PacketFrameEncryptor extends MessageToByteEncoder<byte[]> {

    private final CryptoManager manager;
    private final CryptoKey key;

    public PacketFrameEncryptor(CryptoManager manager, CryptoKey key) {
        this.manager = manager;
        this.key = key;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) throws Exception {
        byte[] sizeHeader = new byte[]{
            (byte) ((msg.length >>> 24) & 0xFF),
            (byte) ((msg.length >>> 16) & 0xFF),
            (byte) ((msg.length >>> 8) & 0xFF),
            (byte) ((msg.length) & 0xFF)
        };

        byte[] encrypted = this.manager.encrypt(this.key, sizeHeader, msg);
        out.writeBytes(encrypted);
    }

}
