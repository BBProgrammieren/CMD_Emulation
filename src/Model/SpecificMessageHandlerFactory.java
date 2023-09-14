package Model;

class SpecificMessageHandlerFactory implements MessageHandlerFactory {
    @Override
    public MessageHandler createHandler() {
        return new SpecificMessageHandler();
    }
}