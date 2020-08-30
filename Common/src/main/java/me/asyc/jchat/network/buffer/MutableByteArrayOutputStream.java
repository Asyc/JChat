package me.asyc.jchat.network.buffer;

import java.io.ByteArrayOutputStream;

/**
 * Extension of {@link ByteArrayOutputStream} that allows direct access to the underlying
 * byte array.
 *
 * This is used to prevent copying of arrays.
 */
public final class MutableByteArrayOutputStream extends ByteArrayOutputStream {

    public MutableByteArrayOutputStream() {
        super();
    }

    public MutableByteArrayOutputStream(int size) {
        super(size);
    }

    /**
     * @return Returns a copy of the underlying buffer
     */
    public synchronized byte[] getByteArray() {
        return super.buf;
    }
}
