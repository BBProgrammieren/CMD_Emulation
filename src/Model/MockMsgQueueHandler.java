package Model;

import java.nio.charset.StandardCharsets;

import io.netty.channel.ChannelHandlerContext;

public class MockMsgQueueHandler extends MsgQueueHandler {

    public MockMsgQueueHandler(Connection connection) {
        super(connection);
    }

    @Override
    public void add(byte[] bs) {
      
        System.out.println("Mocked add: " + new String(bs, StandardCharsets.UTF_8));
    }

    @Override
    public void handleMessage(ChannelHandlerContext ctx, byte[] msg) {
        
        System.out.println("Mocked handle message: " + new String(msg, StandardCharsets.UTF_8));
    }
    
}

