package me.asyc.jchat.network.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.asyc.jchat.network.encryption.CryptoKey;
import me.asyc.jchat.network.encryption.CryptoManager;

/**
 * Creates and frames the packet and then writes it out to the socket sndbuf.
 */
public final class PacketFrameEncryptor extends MessageToByteEncoder<byte[]> {

    private final CryptoManager manager;
    private final CryptoKey key;

    public PacketFrameEncryptor(CryptoManager manager, CryptoKey key) {
        this.manager = manager;
        this.key = key;
    }

    /**
     * Writes all the packet data to the provided {@link ByteBuf}
     * as well as appending a four byte(integer) header of the total packet size.
     * @param ctx The channel context
     * @param msg The packet's payload
     * @param out The output buffer
     * @throws Exception Thrown if the packet data could not be written to the buffer, or the packet could not be encrypted
     */
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
