package Model;

import java.io.ByteArrayOutputStream;

public class Converter {
	
		private static final byte STX = 0x02;
	    private static final byte EOT = 0x04;
	    private static final byte ETX = 0x03;

	    public ByteArrayOutputStream getByteArrayOutputStream(String addrSubBox, String runningNr, String type, String direction, String addr, String message) {
	    	CrcValue crc;
			crc = new CrcValue(message.getBytes());

			ByteArrayOutputStream cmdArray = new ByteArrayOutputStream();
			cmdArray.write((byte) STX);
			cmdArray.writeBytes(addrSubBox.getBytes());
			cmdArray.writeBytes(runningNr.getBytes());
			cmdArray.writeBytes(type.getBytes());
			cmdArray.writeBytes(direction.getBytes());
			cmdArray.writeBytes(addr.getBytes());
			cmdArray.writeBytes(message.getBytes());
			cmdArray.writeBytes(new byte[] { EOT, crc.getCrc16_low(), crc.getCrc16_high(), ETX });
			return cmdArray;
		}
	    
	    
	    public ByteArrayOutputStream getByteArrayOutputStreamFori(String addrSubBox, String runningNr, String type, String addr, String state, String slot) {
	    	CrcValue crc;
	    	String message = addrSubBox + runningNr + type + "i" + addr + state + slot;
			crc = new CrcValue(message.getBytes());

			ByteArrayOutputStream cmdArray = new ByteArrayOutputStream();
			cmdArray.write((byte) STX);
			cmdArray.writeBytes(addrSubBox.getBytes());
			cmdArray.writeBytes(runningNr.getBytes());
			cmdArray.writeBytes(type.getBytes());
			cmdArray.writeBytes("i".getBytes());
			cmdArray.writeBytes(addr.getBytes());
			cmdArray.writeBytes(state.getBytes());
			cmdArray.writeBytes(slot.getBytes());
			cmdArray.writeBytes(new byte[] { EOT, crc.getCrc16_low(), crc.getCrc16_high(), ETX });
			return cmdArray;
		}


	    public static byte[] convertCmdToByteArray(String cmd) {
	        // Zerlegen Sie den Befehl cmd in seine Komponenten
	        String addrSubBox = cmd.substring(0, 4); // Annahme, dass addrSubBox 4 Zeichen lang ist
	        String runningNr = cmd.substring(4, 5);  // Annahme, dass runningNr 2 Zeichen lang ist
	        String type = cmd.substring(5, 6);
	        String addr = cmd.substring(6, 7);      // Annahme, dass addr 4 Zeichen lang ist
	        String message = cmd.substring(7, 8);      // Der Rest des Befehls

	        Converter converter = new Converter();
	        ByteArrayOutputStream cmdArray = converter.getByteArrayOutputStream(addrSubBox, runningNr, type, addr, message);
	        
	        return cmdArray.toByteArray();
	    }


	    private ByteArrayOutputStream getByteArrayOutputStream(String addrSubBox, String runningNr, String type, String addr, String message) {
	        CrcValue crc = new CrcValue(message.getBytes());

	        ByteArrayOutputStream cmdArray = new ByteArrayOutputStream();
	        cmdArray.write((byte) STX);
	        cmdArray.writeBytes(addrSubBox.getBytes());
	        cmdArray.writeBytes(runningNr.getBytes());
	        cmdArray.writeBytes(type.getBytes());
	        cmdArray.writeBytes(addr.getBytes());
	        cmdArray.writeBytes(message.getBytes());
	        cmdArray.writeBytes(new byte[] { EOT, crc.getCrc16_low(), crc.getCrc16_high(), ETX });
	        return cmdArray;
	    }
		
}
