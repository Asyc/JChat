package me.asyc.jchat.network.pipeline;

import io.netty.util.AttributeKey;
import me.asyc.jchat.network.encryption.CryptoKey;


public class ChannelAttributes {

	public static final AttributeKey<byte[]> VALIDATE_BUFFER = AttributeKey.valueOf("jchat:validate");
	public static final AttributeKey<CryptoKey> KEY = AttributeKey.valueOf("jchat:key");

	protected ChannelAttributes() {}

}
