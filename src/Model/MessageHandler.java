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

/**
 * {@link MessageHandler} is the UDP Message Handler and reply the client after message parsing.
 * @author Sameer Narkhede See <a href="https://narkhedesam.com">https://narkhedesam.com</a>
 * @since Sept 2020
 * 
 */
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
	
	/**
	 * Actual Message handling and reply to server.
	 * 
	 * @param ctx  {@link ChannelHandlerContext}
	 * @param msg  {@link Message}
	 */
//	private void handleMessage(ChannelHandlerContext ctx, DatagramPacket msg) {
//
//		System.out.println("Message Received : " + msg);

		//ByteBuf buf = Unpooled.wrappedBuffer("Hey Sameer Here!!!!".getBytes());

//		// Send reply
//		final WriteListener listener = new WriteListener() {
//			@Override
//			public void messageRespond(boolean success) {
//				System.out.println(success ? "reply success" : "reply fail");
//			}
//		};
//
//		ctx.channel().writeAndFlush(new DatagramPacket(buf, msg.getSender())).addListener(new ChannelFutureListener() {
//			@Override
//			public void operationComplete(ChannelFuture future) throws Exception {
//				if (listener != null) {
//					listener.messageRespond(future.isSuccess());
//				}
//			}
//		});
	//}

	/**
	 * {@link WriteListener} is the lister message status interface.
	 * @author Sameer Narkhede See <a href="https://narkhedesam.com">https://narkhedesam.com</a>
	 * @since Sept 2020
	 * 
	 */
	public interface WriteListener {
		void messageRespond(boolean success);
	}

}