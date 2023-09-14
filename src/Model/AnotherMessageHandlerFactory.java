package Model;

class AnotherMessageHandlerFactory implements MessageHandlerFactory {
    @Override
    public MessageHandler createHandler() {
        return new AnotherMessageHandler();
    }
}