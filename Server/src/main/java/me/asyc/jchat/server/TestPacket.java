package me.asyc.jchat.server;

import me.asyc.jchat.server.network.client.User;

public class TestPacket implements Packet {

	@Override
	public short getPacketID() {
		return 0;
	}

	@Override
	public void read(User user, byte[] in) {

	}

	@Override
	public void write(OutputBuffer out) {

	}

	@Override
	public void handle() {

	}
}
