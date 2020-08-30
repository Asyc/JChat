package me.asyc.jchat.network.buffer;

import java.io.OutputStream;
import java.util.Arrays;

/**
 * Byte Array wrapper class with utility functions to keep track of
 * position.
 *
 * Used to prevent constantly reallocating new buffers.
 */
public final class ByteArray extends OutputStream {

	/**
	 * The underlying buffer
	 */
	private final byte[] buffer;

	/**
	 * The current index of the buffer
	 */
	private int count;

	public ByteArray(int size) {
		this.buffer = new byte[size];
		this.count = 0;
	}

	@Override
	public void write(int b) {
		buffer[count++] = (byte) b;
	}

	/**
	 * Sets the current index to zero
	 */
	public void reset() {
		this.count = 0;
	}

	/**
	 * @return Returns whether the current index is at the max possible value
	 */
	public boolean isFull() {
		return this.count == this.buffer.length;
	}

	/**
	 * @return Returns if the current index is equal to zero
	 */
	public boolean isEmpty() {
		return this.count == 0;
	}

	/**
	 * @return Returns the current index of the buffer
	 */
	public int size() {
		return this.count;
	}

	/**
	 * @return Returns the max size of the underlying buffer
	 */
	public int capacity() {
		return this.buffer.length;
	}

	/**
	 * @return Returns the remaining writable bytes in the buffer
	 */
	public int getRemaining() {
		return this.buffer.length - this.count;
	}

	/**
	 * @return Returns a direct handle to the underlying buffer
	 */
	public byte[] getByteArray() {
		return this.buffer;
	}

	/**
	 * @return Returns a copy of the underlying buffer
	 */
	public byte[] toByteArray() {
		return Arrays.copyOf(this.buffer, this.buffer.length);
	}
}
