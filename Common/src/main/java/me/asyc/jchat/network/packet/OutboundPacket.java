package me.asyc.jchat.network.packet;

import io.netty.channel.Channel;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A packet that will be written to an output stream.
 */
public interface OutboundPacket extends Packet {
	/**
	 * Writes the packet data to an output stream
	 *
	 * @param channel The channel/receiver of the packet
	 * @param out The output buffer that the packet data will be written to
	 * @throws Exception Thrown if the packet could not be serialized properly
	 */
	void write(Channel channel, OutputStream out) throws Exception;

	/**
	 *
	 * This method is used to reduce the amount of buffer re-allocations when writing a packet
	 *
	 * @return Returns an estimated size of the total bytes that will be written.
	 */
	int getEstimatedSize();
}
