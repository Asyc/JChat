package me.asyc.jchat.client.network.pipeline;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import me.asyc.jchat.network.exception.UnknownPacketException;
import me.asyc.jchat.network.packet.InboundPacket;
import me.asyc.jchat.network.packet.Packet;

@ChannelHandler.Sharable
public final class MessageHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        InboundPacket packet;
        try {
            packet = (InboundPacket) msg;
        } catch (ClassCastException e) {
            throw new UnknownPacketException();
        }

        packet.handle();

        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (cause instanceof UnknownPacketException) {
            System.err.println("Client " + ctx.channel().remoteAddress().toString() + " sent an invalid packet. Closing Connection.");
            ctx.channel().close();
        }

        super.exceptionCaught(ctx, cause);
    }
}
