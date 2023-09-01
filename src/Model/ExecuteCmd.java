package Model;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ExecuteCmd {
	private byte[] data;
	private char[] dataChar;
	private CmdType type;
	private String addr;
	private ClientManager clientManager;
	private Catcher controller;
	private boolean sendNormalAck;

	public ExecuteCmd(byte[] data, String moduleAddr, ClientManager clientManager, Catcher controller) {
		this.controller = controller;
		this.data = data;
		int endData = 0;
		int eot = 0x04;
		this.clientManager = clientManager;
		sendNormalAck = false;

		this.addr = moduleAddr;

		for (int i = 0; i < this.data.length; i++) {
			if (this.data[i] == eot) {
				endData = i;
				break;
			}
		}

		this.data = Arrays.copyOfRange(data, 7, endData);
		if(this.data == null || this.data.length == 0) {
			this.data = Arrays.copyOfRange(data, 6, endData);
		}
		if(moduleAddr != null) {
			byteArrayToChar();
		}	
		else {
			dataChar = new char[this.data.length];

			for (int i = 0; i < this.data.length; i++) {
				dataChar[i] = (char) this.data[i];
			}

		}
		checkCmdType();
	}

	private void byteArrayToChar() {
	    // Convert the startString to a byte array
	    byte[] startBytes = addr.getBytes(StandardCharsets.US_ASCII);

	    // Find the start position of the startBytes in the data array
	    int startIndex = -1;
	    for (int i = 0; i <= data.length - startBytes.length; i++) {
	        boolean match = true;
	        for (int j = 0; j < startBytes.length; j++) {
	            if (data[i+j] != startBytes[j]) {
	                match = false;
	                break;
	            }
	        }
	        if (match) {
	            startIndex = i;
	            break;
	        }
	    }

	    // If the startBytes is not found, return without converting
	    if (startIndex == -1) {
	        System.out.println("Start string not found in data.");
	        return;
	    }

	    // Check if there is a byte after the startString
	    if (startIndex + startBytes.length >= data.length) {
	        System.out.println("No byte after the start string in data.");
	        return;
	    }
	    dataChar = new char[data.length - (startIndex + startBytes.length)];
	    for(int i = startIndex + startBytes.length, j = 0; i < data.length; i++, j++) {
	        dataChar[j] = (char) data[i];
	    }
	    // Convert the first byte after startString to a char

	    // Now you can do something with the result
	    System.out.println("Type: " + dataChar[0]);
	}

	public boolean sendNormalAck() {
		return sendNormalAck;
	}
	

	
	public CmdType getCmdType() {
		if(type == type.R_TYPE)
		return type;
		else {
			return null;
		}
	}

	private void checkCmdType() {
		PTFModuleInterface module = (PTFModuleInterface) controller.getModuleModel(addr);

		switch (dataChar[0]) {
		case 'D':
			type = type.D_TYPE;
			System.out.println("Command Typ: " + type);
			sendNormalAck = true;
			break;

		case 'T':
			type = type.T_TYPE;
			System.out.println("Command Typ: " + type);
			module.setDefaultDisplay(dataChar);
			sendNormalAck = true;
			break;
			
		case '1':
			if (dataChar[1] == 'V') {
				type = type.V_TYPE;
				System.out.println("Command Typ: " + type);
				sendNormalAck = true;
			}	
			break;
			
		case '0':
			if (dataChar[1] == 'V') {
				type = type.V_TYPE;
				System.out.println("Command Typ: " + type);
				sendNormalAck = true;
			}	
			break;


		case 'V':
			type = type.V_TYPE;
			module.setButtonRole(dataChar);
			System.out.println("Command Typ: " + type);
			sendNormalAck = true;
			break;

		case 'H':
			type = type.H_TYPE;
			System.out.println("Command Typ: " + type);
			if(dataChar.length >= 3) {
			module.setBrightness(dataChar);
			sendNormalAck = true;
			}
			else {
				System.out.println("Command too short!");
			}
			break;

		case 'S':
			type = type.S_TYPE;
			System.out.println("Command Typ: " + type);
			module.defineSound(dataChar);
			sendNormalAck = true;
			break;

		case 'Z':
			type = type.Z_TYPE;
			System.out.println("Command Typ: " + type);
			sendNormalAck = true;
			break;

		case '$':
			type = type.$_TYPE;
			System.out.println("Command Typ: " + type);
			module.playSound(dataChar);
			sendNormalAck = true;
			break;
			
		case ' ':
			type = type.$_TYPE;
			System.out.println("Command Typ: Null");
			sendNormalAck = true;
			break;
			
		case 'R':
			type = type.R_TYPE;
			System.out.println("Command Typ: " + type);
			sendNormalAck = false;
			
			break;
			

		default:
			System.out.println("Keinen Command Typ erkannt!");
		}
	}

}
