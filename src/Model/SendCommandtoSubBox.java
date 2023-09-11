package Model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SendCommandtoSubBox { // host

	public static void main(String[] args) {

		try {
			int iport = 2241;
			int sourcePort = 2240;
			byte[] buffer = new byte[256];
			DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
			DatagramSocket ds = new DatagramSocket(sourcePort);

			// String data = "AAAA1D>BWRfH10"; // dieser Command wird an die subbox gesendet
			// String data = "BWRf1DT999999111111";
			// String data = "?????0000zzzz";
			String data = "AAAA1DR0";
			// String data = "AAAA1D>BWRfR0";

			byte[] byteData = data.getBytes();
			// int crc16 = 55164;
			int crc16 = crc16_2(byteData);
			// 1bbf
			// 7cd7
			System.out.println("Debug: " + Integer.toHexString(crc16));
			System.out.println("Debug int of crc: " + crc16);

			byte crc16_low = (byte) crc16;// getHigh(crc16);
			byte crc16_high = ((byte) (crc16 >> 8));

			System.out.println("CRC test: Integer: " + crc16 + ", Hex Representation: " + Integer.toHexString(crc16_low)
					+ Integer.toHexString(crc16_high));

			ByteArrayOutputStream cmdArray = new ByteArrayOutputStream();
			cmdArray.write((byte) 0x02);
			cmdArray.writeBytes(data.getBytes());
			cmdArray.writeBytes(new byte[] { 0x04, crc16_low, crc16_high, 0x03 });

			DatagramPacket dp = new DatagramPacket(cmdArray.toByteArray(), cmdArray.size());

			System.out.println("Packet sent: " + byteArrayToHex(cmdArray.toByteArray()));

			InetAddress empfaenger = InetAddress.getByName("127.0.0.1");

			dp.setPort(iport);
			dp.setAddress(empfaenger);
			ds.send(dp);
//			ds.close();
//			dp.setPort(2242);
//			ds.send(dp);
//			ds = new DatagramSocket(sourcePort);
			ds.receive(receivePacket);
			System.out.println("---------------------------------");
			System.out.println("Packet received!");

			checkReceivedPacket(receivePacket);

			if (byteArrayToHex(receivePacket.getData()) == null) {
				System.out.println("Keine Antwort erhalten!");
			} else {
				System.out.println("MSG from SubBox:");
			}

		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static byte[] intToBytes(final int i) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.putInt(i);
		return bb.array();
	}

	private static byte[] shortToBytes(final short i) {
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.putShort(i);
		return bb.array();
	}

	public static byte getLow(int crc16) {

		// byte[] array = new byte[] {0x0, 0x0, 0x7c, (byte) 0xd7 };

		byte[] array = shortToBytes((short) crc16);

		return array[0];
	}

	public static byte getHigh(int crc16) {

		// byte[] array = new byte[] {0x0, 0x0, 0x7c, (byte) 0xd7 };
		System.out.println(Integer.toHexString(crc16));

		byte test = (byte) (crc16);

		System.out.println(Integer.toHexString(test));

		return test;

	}

	public static String byteArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder(a.length * 2);
		for (byte b : a) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	private static final int POLYNOMIAL = 0x1021;
	private static final int PRESET_VALUE = 0;

	public static int crc16(byte[] data) {
		int current_crc_value = PRESET_VALUE;
		for (int i = 0; i < data.length; i++) {
			current_crc_value ^= data[i] & 0xFF;
			for (int j = 0; j < 8; j++) {
				if ((current_crc_value & 1) != 0) {
					current_crc_value = (current_crc_value >>> 1) ^ POLYNOMIAL;
				} else {
					current_crc_value = current_crc_value >>> 1;
				}
			}
		}

		return current_crc_value & 0xFFFF;
	}

	public static void checkReceivedPacket(DatagramPacket packet) {

		byte[] data = packet.getData();
		// ByteBuffer buffer = new ByteBuffer().

		byte[] addrSubBox = Arrays.copyOfRange(data, 1, 5);
		byte[] onlyData = getOnlyData(data);

		String addr = "" + ((char) data[1]) + ((char) data[2]) + ((char) data[3]) + ((char) data[4]);
		// System.Text.Encoding.Default.GetString();

		byte[] fullData = Arrays.copyOfRange(data, 1, 7);

		System.out.println("adr from SubBox: " + new String(addrSubBox));
		System.out.println("Running number: " + (char) onlyData[5]);
		System.out.println("State: " + (int) (char) onlyData[6]);
		System.out.println("crc: " + onlyData[8] + " | " + onlyData[9]);

		System.out.println("crc self calculated: " + (char) crc16_2(fullData));

		System.out.print("Data from sub box: ");
		for (byte hex : onlyData) {
			System.out.print(String.format("%02x", hex));
		}
		System.out.println("");
		System.out.println("Number of crc: " + getCrcNum(data[8], data[9]));

		// TODO: convert two byte to an integer
		// intToBytes -> bytesToInt
	}

	private static byte[] getOnlyData(byte[] data) {
		int endData = 0;
		int etx = 0x03;

		for (int i = 0; i < data.length; i++) {
			if (data[i] == etx) {
				endData = i;
				break;
			}
		}
		data = Arrays.copyOfRange(data, 0, endData + 1);
		return data;
	}

	public static int getCrcNum(byte low, byte high) {
//		int num;
//		num = 0;
//		byte[] crc = data;

		int crc16_low = low;
		int crc16_high = (high << 8);
		return crc16_low + crc16_high;

		// return num;
	}

	public static int crc16_2(byte[] testBytes) {
		int crc = 0x0; // initial value
		int polynomial = 0x1021; // 0001 0000 0010 0001 (0, 5, 12), in your case: 0x1081

		for (byte b : testBytes) {
			for (int i = 0; i < 8; i++) {
				boolean bit = ((b >> (7 - i) & 1) == 1);
				boolean c15 = ((crc >> 15 & 1) == 1);
				crc <<= 1;
				if (c15 ^ bit)
					crc ^= polynomial;
			}
		}

		crc &= 0xffff;

		return crc;
	}
}
