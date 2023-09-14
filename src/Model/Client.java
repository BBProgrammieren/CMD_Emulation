package Model;

public class Client {
	 public static void main(String[] args) {
	        MessageHandlerFactory factory = new SpecificMessageHandlerFactory();
	        MessageHandler handler = factory.createHandler();
	    }
}
