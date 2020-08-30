package me.asyc.jchat.network.buffer;

import java.io.ByteArrayOutputStream;

public final class ByteBufferOutputStream extends ByteArrayOutputStream {

	@Override
	public synchronized byte[] toByteArray() {
		return super.buf;
	}
}
