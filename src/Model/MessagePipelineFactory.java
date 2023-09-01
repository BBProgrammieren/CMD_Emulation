package Model;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.DatagramChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class MessagePipelineFactory implements PipelineFactory{
		//private final int availableProcessors;
	    private final EventExecutorGroup executors;
	    
	    
	    /**
	     * Constructor fott {@link MessagePipelineFactory}
	     */
	    public MessagePipelineFactory() {
	        //availableProcessors = Runtime.getRuntime().availableProcessors();
	        executors = new DefaultEventExecutorGroup(1);
	    }
	    
		/**
		 *	Pipeline Factory method for channel initialization
		 */
		@Override
		public ChannelInitializer<DatagramChannel> createInitializer() {
			
			return new ChannelInitializer<DatagramChannel>() {

				@Override
				protected void initChannel(DatagramChannel ch) throws Exception {
					// Create chanel pipeline
	            	ChannelPipeline pipeline = ch.pipeline();
	            	
//	            	final MessageDecoder decoder = new MessageDecoder();
//	            	
//	            	pipeline.addLast("decoder", decoder);
	            	
	            	//final MessageHandler handler = new MessageHandler();
	            	
	            	//pipeline.addLast(executors, "handler", handler);
	            	//pipeline.addLast("handler", handler);
					
				}
				
			};
		}

}
