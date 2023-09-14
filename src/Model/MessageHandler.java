package Model;

import io.netty.buffer.ByteBuf;
//
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;


public abstract class MessageHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	
	public MessageHandler() {
		super(false);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		super.exceptionCaught(ctx, cause);
		cause.printStackTrace();
	}

	public abstract boolean acceptInboundMessage(byte[] msg) throws Exception;
	
	@Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
		 ByteBuf content = ((DatagramPacket)msg).content();
		    byte[] bytes = new byte[content.readableBytes()];
		    content.readBytes(bytes);
		    content.resetReaderIndex();
		    
        return acceptInboundMessage(bytes);
    }
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {

		//ReferenceCountUtil.retain(msg);
		//ctx.fireChannelRead(msg);
		
		 ByteBuf content = msg.content();
		    byte[] bytes = new byte[content.readableBytes()];
		    content.readBytes(bytes);
		    
		handleMessage(ctx, bytes);

	}

	public abstract void handleMessage(ChannelHandlerContext ctx, byte[] msg);
	
	
	public interface WriteListener {
		void messageRespond(boolean success);
	}

}