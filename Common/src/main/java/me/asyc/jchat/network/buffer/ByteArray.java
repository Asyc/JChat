package me.asyc.jchat.network.buffer;

import java.util.Arrays;

public final class ByteArray {

	private final byte[] buffer;
	private int count;

	public ByteArray(int size) {
		this.buffer = new byte[size];
		this.count = 0;
	}

	public void write(byte... bytes) {
		for (byte b : bytes) {
			this.buffer[count++] = b;
		}
	}

	public void reset() {
		this.count = 0;
	}

	public byte[] copy() {
		return Arrays.copyOf(this.buffer, this.buffer.length);
	}

	public byte[] get() {
		return this.buffer;
	}

	public int remaining() {
		return this.buffer.length - this.count;
	}

	public int side() {
		return this.count;
	}

	public int capacity() {
		return this.buffer.length;
	}

	public void incrementSize(int size) {
		this.count = size;
	}

	public boolean full() {
		return this.count == this.buffer.length;
	}
}
