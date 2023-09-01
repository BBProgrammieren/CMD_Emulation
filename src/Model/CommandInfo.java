package Model;

import java.io.ByteArrayOutputStream;

public class CommandInfo {

	private static final byte EOT = 0x04;
	private byte[] receivedData;

	public int getRunningNumber(ByteArrayOutputStream byteArrayOutputStream) {
		int runningNrForACK;
		int posSTX = 0;
		int posEOT = -1;
		this.receivedData = byteArrayOutputStream.toByteArray();

		for (int i = 0; i < receivedData.length; i++) {
			if (receivedData[i] == EOT) {
				posEOT = i;
				break;
			}
		}
	
		return runningNrForACK = Integer.parseInt("" + (char) receivedData[5]);
	
	}

	public int getRunningNumber(byte[] currentMsg) {
		int runningNrForACK;
		int posSTX = 0;
		int posEOT = -1;
		this.receivedData = currentMsg;

		for (int i = 0; i < receivedData.length; i++) {
			if (receivedData[i] == EOT) {
				posEOT = i;
				break;
			}
		}
	
		return runningNrForACK = Integer.parseInt("" + (char) receivedData[5]);
	
	}
}
