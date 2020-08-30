package me.asyc.jchat.network.packet;

import io.netty.channel.Channel;


/**
 * A packet that will be read from an input source
 */
public interface InboundPacket extends Packet {

    /**
     * Reads the packet data from a packet payload.
     *
     * @param channel The socket channel
     * @param in The input payload
     * @throws Exception Thrown if the packet could not be parsed.
     */
    void read(Channel channel, byte[] in) throws Exception;

    /**
     * Handles the packet. This is implementation specific.
     * Called after the packet has been parsed and passed to the last stage of the netty pipeline,
     * the message handler.
     */
    void handle();
}
