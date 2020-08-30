package me.asyc.jchat.client.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import me.asyc.jchat.client.JChatClient;
import me.asyc.jchat.client.network.packet.factory.PacketList;
import me.asyc.jchat.client.network.pipeline.SocketChannelInitializer;

import java.net.SocketException;

public class Connection {

    private final Bootstrap bootstrap;

    public Connection(String ip, int port) throws SocketException {
        EventLoopGroup group = new NioEventLoopGroup(1);

        this.bootstrap = new Bootstrap()
            .group(group)
            .handler(new SocketChannelInitializer(JChatClient.INSTANCE.getCryptoManager(), PacketList.PACKET_RESOLVER));


        this.bootstrap.bind(ip, port);
        this.bootstrap.group(group);
        if (!this.bootstrap.connect(ip, port).awaitUninterruptibly().isSuccess()) {
            throw new SocketException();
        }
    }
}
