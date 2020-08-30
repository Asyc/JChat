package me.asyc.jchat.network.pipeline;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.AttributeKey;
import me.asyc.jchat.network.buffer.ByteArray;
import me.asyc.jchat.network.encryption.CryptoKey;
import me.asyc.jchat.network.encryption.CryptoManager;

import java.util.List;

/**
 * Turns byte sequences/fragments into full packet payload. Also handles decryption.
 * <p>
 * Packet Format
 * <pre>
 * | Size    | Payload Header           | Payload     |
 * |---------|--------------------------|-------------|
 * | integer | packet identifier(short) | packet data |
 * </pre>
 */
public final class PacketFrameDecryptor extends ByteToMessageDecoder {

    private static final AttributeKey<CryptoKey> KEY_ATTRIBUTE = AttributeKey.valueOf("jchat:key");

    private final CryptoManager manager;
    private final CryptoKey key;

    private final ByteArray header;
    private ByteArray packetBuffer;

    public PacketFrameDecryptor(CryptoManager manager, CryptoKey key) {
        this.manager = manager;
        this.key = key;

        this.header = new ByteArray(4);
        this.packetBuffer = null;
    }

    /**
     * Loops until all bytes are read, decodes as many frames as possible, then passes
     * it on to the next handler in the netty pipeline.
     *
     * @param ctx The channel context
     * @param in The rcv buffer
     * @param out The output list
     * @throws Exception If the packet data could not be parsed
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() > 0) {
            byte[] decoded = this.decodeFrame(ctx, in);
            if (decoded != null) out.add(decoded);
        }
    }

    /**
     * This method reads the size header of the frame, and reads all the payload bytes.
     *
     * @param ctx The channel context
     * @param in The rcv buffer
     * @return Returns a non-encrypted byte array of the packet payload. This value can be null, when there is not enough information to read whole frame.
     * @throws Exception If the packet data could not be parsed
     */
    private byte[] decodeFrame(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        CryptoKey key = ctx.channel().attr(PacketFrameDecryptor.KEY_ATTRIBUTE).get();

        if (!this.header.isFull()) {
            int toRead = Math.min(this.header.getRemaining(), in.readableBytes());
            in.readBytes(this.header, toRead);

            if (!this.header.isFull()) return null;

            byte[] sizeBuffer = this.header.getByteArray();
            if (key != null) {
                sizeBuffer = this.manager.decrypt(key, sizeBuffer);
            }

            this.packetBuffer = new ByteArray(((sizeBuffer[0] << 24) + (sizeBuffer[1] << 16) + (sizeBuffer[2] << 8) + (sizeBuffer[3])));
        }

        if (!this.packetBuffer.isFull()) return null;

        int toRead = Math.min(this.packetBuffer.getRemaining(), in.readableBytes());
        in.readBytes(this.packetBuffer, toRead);

        if (!this.packetBuffer.isFull()) return null;

        byte[] packetBuffer = this.packetBuffer.getByteArray();
        if (key != null) {
            packetBuffer = this.manager.decrypt(this.key, this.header.getByteArray(), packetBuffer);
        }

        this.header.reset();
        this.packetBuffer = null;
        return packetBuffer;
    }
}
