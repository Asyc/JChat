package me.asyc.jchat.network.packet;

/**
 * Interface that all packets implement.
 */
public interface Packet {

    /**
     * A unique identifier for each packet.
     * This short will be written as a part of the packet header.
     *
     * @return Returns a unique ID for the current packet
     */
    short getPacketID();
}
