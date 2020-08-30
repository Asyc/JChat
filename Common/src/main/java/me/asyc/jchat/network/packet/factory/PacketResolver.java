package me.asyc.jchat.network.packet.factory;

import me.asyc.jchat.network.exception.UnknownPacketException;
import me.asyc.jchat.network.packet.InboundPacket;

/**
 * Used to resolve a packet identifier to the corresponding
 * packet object.
 */
public interface PacketResolver {
    /**
     * Creates a {@link InboundPacket} object that matches with the supplied identifier.
     *
     * @param id The unique identifier of the packet
     * @return Returns the created {@link InboundPacket} object
     * @throws UnknownPacketException Thrown if the supplied packet ID is invalid
     */
    InboundPacket createPacketFromID(short id) throws UnknownPacketException;
}
