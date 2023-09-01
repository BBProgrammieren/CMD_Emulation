package Model;

import java.util.Arrays;

public class ErrorHandle {

	private CrcValue crc; 
	private byte crcHigh;
	private byte crcLow;
	ErrorHandle(){
		
	}
	
	public String getcrcHigh() {
		String str = Byte.toString(crcHigh);
		return str;
	}
	
	public String getcrcLow() {
		String str = Byte.toString(crcLow);
		return str;
	}
	
	public boolean approveCRC(byte[] data){
		char n = 'N';
		char c = 'C';
		boolean bool = false;
		getCrcFromReceivedMessage(data);
		
		this.crc = new CrcValue(data);
		if ((crc.getCrc16_high() == crcHigh && crc.getCrc16_low() == crcLow) || (crcHigh == (byte) c && crcLow == (byte) n)) {
			bool = true;
		}
		return bool;
	}
	
	private void getCrcFromReceivedMessage(byte[] data) {
		int beginData = 0;
		int endData = 0;
		int etx = 0x03;
		int eot = 0x04;

		for (int i = 0; i < data.length; i++) {
			if (data[i] == eot) {
				beginData = i;
			}
			if (data[i] == etx) {
				endData = i;
				break;
			}
		}
		
		data = Arrays.copyOfRange(data, beginData + 1, endData);
		
		this.crcHigh = data[1];
		this.crcLow = data[0];
	}
}
