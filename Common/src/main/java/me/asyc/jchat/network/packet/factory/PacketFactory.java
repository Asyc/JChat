package me.asyc.jchat.network.packet.factory;

import me.asyc.jchat.network.packet.InboundPacket;

/**
 * Factory class that will create a packet.
 *
 * Used to prevent the use of reflection in creating objects, to minimize overhead.
 */
public interface PacketFactory {
    /**
     * @return Returns a new {@link InboundPacket} handle.
     */
    InboundPacket createPacket();
}
