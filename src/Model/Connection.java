package Model;

import java.io.ByteArrayOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.socket.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.net.InetSocketAddress;
import java.util.function.Consumer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class Connection {
	private final EventLoopGroup bossLoopGroup;
	private final ChannelGroup channelGroup;
	//private final Class<? extends PipelineFactory> pipelineFactoryClass;
	Bootstrap bootstrap = new Bootstrap();
	
	private final int subBoxPort;
	private Consumer<byte[]> callback;
	byte[] buffer = new byte[256];
	private byte[] receivedData;
	private int hostPort;
	private String hostIP;
	private String subBoxIP;
	private int runningNr;
	private Channel channel;
	private NioEventLoopGroup group;

	public Connection(int subBoxPort, int hostPort, String hostIP, String subBoxIP) {
		this.bossLoopGroup = new NioEventLoopGroup();
		this.channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
		//this.pipelineFactoryClass = pipelineFactoryClass;
		this.subBoxPort = subBoxPort;
		this.hostPort = hostPort;
		this.hostIP = hostIP;
		this.subBoxIP = subBoxIP;
	}

	public boolean checkConnection() {
		if (channel != null) {
			return true;
		} else {
			return false;
		}
	}

	public void closeConnection() {
		
		if (channel != null) {
			channel.close();
			channel = null;
		}

		// Shutdown des EventLoopGroups
		if (group != null) {
			group.shutdownGracefully();
			group = null;
		}
	}
	
//	public void receiveData(Consumer<byte[]> callback) throws Exception {
//		
//	}

	private ChannelPipeline pipeline;
	private DefaultEventExecutorGroup executors;
	
	public void addHandlerCallback(MessageHandler handler) {
		//String handlerName = "handler" + System.currentTimeMillis(); // unique name
		//pipeline.addLast(executors, handler.getClass().getSimpleName(), handler);
		pipeline.addLast(handler.getClass().getSimpleName(), handler);
	}
	

	
	public void startup() throws Exception {
		//this.callback = callback;
		this.group = new NioEventLoopGroup();
		

			bootstrap.group(bossLoopGroup)
	        .channel(NioDatagramChannel.class)
	        .option(ChannelOption.AUTO_CLOSE, true)
	        .option(ChannelOption.SO_BROADCAST, true);
			
			executors = new DefaultEventExecutorGroup(2);
			
			ChannelInitializer<io.netty.channel.socket.DatagramChannel> initializer = new ChannelInitializer<io.netty.channel.socket.DatagramChannel>() {

				@Override
				protected void initChannel(io.netty.channel.socket.DatagramChannel ch) throws Exception {
					// Create chanel pipeline
	            	pipeline = ch.pipeline();
					
				}
			};
			
			
			
			bootstrap.handler(initializer);
		        


		try {
			// ChannelFuture channelFuture = b.bind(new InetSocketAddress(this.subBoxIP,
			// this.subBoxPort)).sync();
			ChannelFuture channelFuture = bootstrap.bind(subBoxPort).sync();
            channelGroup.add(channelFuture.channel());
			this.channel = channelFuture.channel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 shutdown();
			 throw e;
		} finally {
			// group.shutdownGracefully();
		}
	}
	
	   public final void shutdown() throws Exception {
	        channelGroup.close();
	        bossLoopGroup.shutdownGracefully();
	    }

	public byte[] getfilteredData(byte[] receivedData) {
		int endData = 0;
		int etx = 0x03;

		for (int i = 0; i < receivedData.length; i++) {
			if (receivedData[i] == etx) {
				endData = i;
				break;
			}
		}
		byte[] data = Arrays.copyOfRange(receivedData, 0, endData + 1);
		return data;
	}

	public void send(ByteArrayOutputStream cmdArray) {
	    try {
	        InetAddress host;
	        host = InetAddress.getByName(this.hostIP);

	        byte[] cmdArrayBytes = cmdArray.toByteArray();
	        ByteBuf buffer = Unpooled.copiedBuffer(cmdArrayBytes);

	        DatagramPacket dataPacketAck = new DatagramPacket(buffer, new InetSocketAddress(host, this.hostPort));
	        
	       //String ascii = String.format("%02x", cmdArray.toByteArray());
	        String strData = "";
	        for (byte hex : cmdArray.toByteArray()) {
				strData += String.format("%02x", hex);
			}
	        StringBuilder output = new StringBuilder("");
	        for (int i = 0; i < strData.length(); i += 2) {
	            String str = strData.substring(i, i + 2);
	            output.append((char) Integer.parseInt(str, 16));
	        }
	        

	        if(this.hostPort > 0 && this.hostPort <= 65535 && this.channel != null) {  
	            this.channel.writeAndFlush(dataPacketAck);
	            System.out.println("MSG: " + output.toString());
	        } else {
	          
	        }
	    } catch (UnknownHostException e) {
	        e.printStackTrace();
	    }
	}



}
