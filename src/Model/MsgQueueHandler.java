package Model;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentLinkedQueue;
import io.netty.channel.ChannelHandlerContext;

public class MsgQueueHandler extends MessageHandler {
	
    private ConcurrentLinkedQueue<byte[]> queue = new ConcurrentLinkedQueue<>();
    private Connection connection;
	private int runningNr;
	private CommandInfo info;

	private volatile boolean isAcknowledged = true;
	
    public MsgQueueHandler(Connection connection) {
    	super();
    	
        this.connection = connection;
        this.info = new CommandInfo();
       
    }

    public void add(byte[] bs) {
    	
    	System.out.println("Adding to queue: " + new String(bs));
    	
        queue.add(bs);
        
        if(isAcknowledged) {
        	byte[] currentMsg = queue.poll();
        	
        	if(currentMsg != null) {
        		isAcknowledged = false;
        		this.runningNr = info.getRunningNumber(currentMsg);
        		
        		System.out.println("Sending message in add: " + new String(currentMsg));
        		writemessageflush(currentMsg); //..e.g. send message
        	}
        }
    }

	@Override
	public void handleMessage(ChannelHandlerContext ctx, byte[] msg) {
		byte b = (byte) msg[5];
		int byteValue  = b & 0xFF;
		char asciiChar = (char) byteValue;  // Wandelt die Ganzzahl in ein ASCII-Zeichen um
		int runningAck = Character.getNumericValue(asciiChar); 
		if (msg[6] == 0x44) {
			this.isAcknowledged = false;
		} else if (Character.isDigit((char) msg[6])) {
			if (runningAck == runningNr) {
				this.isAcknowledged = true;
			}
		
			else {
				 System.out.println("Die Bedingung ist nicht erfüllt!");
				    System.out.println("msg[5] ist: " + (char) msg[5]); // Um den Wert von msg[5] zu prüfen
				    System.out.println("runningNr ist: " + runningNr); // Um den Wert von runningNr zu prüfen
				    this.isAcknowledged = false;
			}
		} else {
			this.isAcknowledged = false;
		}
				
		if(isAcknowledged) {
			
			
			sendNextMessage();
			}
			else {
				//resend last command
			}
	}
	
	private void sendNextMessage() {
		byte[] currentMsg = queue.poll();
		

		
	    if(currentMsg != null) {
			System.out.println("Sending message in handleMessage, because we got an ack: " + new String(currentMsg));
	    	
	        writemessageflush(currentMsg); // e.g. send message
	        this.runningNr = info.getRunningNumber(currentMsg);
	        isAcknowledged = false;
	    }
		
	}

	private void writemessageflush(byte[] currentMsg) {
		// TODO Auto-generated method stub
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			byteArrayOutputStream.write(currentMsg);
			connection.send(byteArrayOutputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean acceptInboundMessage(byte[] msg) throws Exception {
		// TODO Auto-generated method stub
		return true;
	}
}


