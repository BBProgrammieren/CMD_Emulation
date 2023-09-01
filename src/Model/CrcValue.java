package Model;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CrcValue {
	
	public CrcValue(byte[] testBytes) {
	    int crc = 0x0; // initial value
        int polynomial = 0x1021; // 0001 0000 0010 0001 (0, 5, 12), in your case: 0x1081
        
      
        for (byte b : getOnlyData(testBytes)) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }

        crc &= 0xffff;
        
        this.crc16_low = (byte) crc;//getHigh(crc16);
		this.crc16_high = ((byte)(crc >> 8));        
        this.crcNumber = crc;  
	}
	
	private byte[] getOnlyData(byte[] data) {
		int beginData = 0;
		int endData = data.length;
		int eot = 0x04;
		int stx = 0x02;

		for (int i = 0; i < data.length; i++) {
			if (data[i] == stx) {
				beginData = i + 1;
			}
			if (data[i] == eot) {
				endData = i;
				break;
			}
		}
		
		data = Arrays.copyOfRange(data, beginData, endData);
		
		return data;
	}
 
    public byte getCrc16_low() {
		return crc16_low;
	}
	public byte getCrc16_high() {
		return crc16_high;
	}
	public int getCrcNumber() {
		return crcNumber;
	}
	private byte crc16_low;
    private byte crc16_high;
    private int crcNumber;
    
    
}
