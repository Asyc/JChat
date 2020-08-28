package me.asyc.jchat.server.utils;

import org.objectweb.asm.Type;

public class ClassUtils {

	private ClassUtils() {
		throw new RuntimeException("Cannot instantiate utility class");
	}

	public static String getClassPath(Class<?> clazz) {
		return clazz.getCanonicalName().replace('.', '/');
	}

	public static String getClassName(Class<?> clazz) {
		String[] split = clazz.getCanonicalName().split("\\.");
		return split[split.length - 1];
	}

	public static String createMethodDescriptor(Class<?> returnType, Class<?>... parameters) {
		StringBuilder builder = new StringBuilder(20).append('(');

		for (Class<?> parameter : parameters) {
			builder.append(Type.getType(parameter).getDescriptor());
		}

		builder.append(')').append(Type.getType(returnType).getDescriptor());

		return builder.toString();
	}
}
