package Model;

import io.netty.channel.ChannelHandlerContext;

class AnotherMessageHandler extends MessageHandler {
    @Override
    public boolean acceptInboundMessage(byte[] msg) {
        // Implementierung für AnotherMessageHandler
        return false; // Beispiel
    }

    @Override
    public void handleMessage(ChannelHandlerContext ctx, byte[] msg) {
        // Implementierung für AnotherMessageHandler
    }
}