package me.asyc.jchat.network.utils;

public class ByteClassLoader extends ClassLoader {

	public Class<?> defineClass(String name, byte[] buffer) {
		return super.defineClass(name, buffer, 0, buffer.length);
	}

}
