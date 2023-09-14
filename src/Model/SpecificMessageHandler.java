package Model;

import io.netty.channel.ChannelHandlerContext;

class SpecificMessageHandler extends MessageHandler {
    @Override
    public boolean acceptInboundMessage(byte[] msg) {
        // Implementierung für SpecificMessageHandler
        return true; // Beispiel
    }

    @Override
    public void handleMessage(ChannelHandlerContext ctx, byte[] msg) {
        // Implementierung für SpecificMessageHandler
    }
}