package me.asyc.jchat.client.network.pipeline;

import io.netty.util.AttributeKey;
import me.asyc.jchat.network.encryption.CryptoKey;
import me.asyc.jchat.network.pipeline.ChannelAttributes;

public final class ClientChannelAttributes extends ChannelAttributes {

	public static final AttributeKey<CryptoKey> SERVER_PUBLIC_KEY = AttributeKey.valueOf("jchat:client:server_key");

}
