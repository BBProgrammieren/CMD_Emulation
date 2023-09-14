package Model;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class SubBox implements Serializable {

	private static final byte STX = 0x02;
	private static final byte EOT = 0x04;
	private static final byte ETX = 0x03;
	private static final String subBoxId = "SUB3    ";
	private int subBoxPort;
	private int hostPort;
	private String hostIp;
	private String controllerIP;
	private byte[] addr;
	private byte[] runningNumber;
	private int state;
	private byte[] data;
	private byte[] receivedData;
	private String subBoxAddr;
	private static final long serialVersionUID = 8235413380570399204L;
	private ExecuteCmd execute;
	private Catcher controller;
	private CmdType type;
	private int runningNrForACK;
	private MainViewModel mainViewModel;
	public CrcValue crc;
	private boolean isAck;
	private MsgQueueHandler queueHandler;
	private Connection connection;

	public SubBox(int subBoxPort, String hostIp, int setHostPort, String subBoxAddr, String controllerIP,
			Catcher controller) {
		
		this.controller = controller;
		this.hostIp = hostIp;
		this.hostPort = setHostPort;
		this.subBoxPort = subBoxPort;
		this.subBoxAddr = subBoxAddr;
		this.controllerIP = controllerIP;
		runningNumber = new byte[1];
		mainViewModel = controller.getMainModel();
		runningNrForACK = 10;
		isAck = false;
		state = 0;

	}
	
	SubBox(){
		
	}

	public void setConnection(Connection connection) { // Setter-Methode für Connection
		this.connection = connection;		
	}

	public String showReceivedData(byte[] packet) {
		String strData = "";
		byte[] data = packet;

		for (byte hex : data) {
			strData += String.format("%02x", hex);
		}
		int pos = strData.indexOf("03");

		if (pos != -1) {
			strData = strData.substring(0, pos + 2); // Add 2 to include "03" in the substring
		} else {
			System.out.println("03 not found in the string.");
		}
		return strData;
	}

	public void setParams(byte[] receivedData) {
		int posSTX = 0;
		int posEOT = -1;
		this.receivedData = receivedData;

		for (int i = 0; i < receivedData.length; i++) {
			if (receivedData[i] == EOT) {
				posEOT = i;
				break;
			}
		}
		this.addr = new byte[] { receivedData[1], receivedData[2], receivedData[3], receivedData[4] };

		this.state = 0;

		runningNrForACK = Integer.parseInt("" + (char) receivedData[5]);

	}

	public String dataType(byte[] receivedData) {
		int posSTX = 0;
		int posEOT = -1;
		receivedData = receivedData;

		for (int i = 0; i < receivedData.length; i++) {
			if (receivedData[i] == EOT) {
				posEOT = i;
				break;
			}
		}
		// Check ob ACK oder Data frame
		String type = "error";
		if (receivedData[6] == 0x44) {
			type = "Type of received data: data frame";
			this.isAck = false;
		} else if (Character.isDigit((char) receivedData[6])) {
			type = "Type of received data: ACK";
			this.isAck = true;
		} else {
			type = "Type of received data: ----";
		}
		return type;
	}

	public boolean isAck(byte[] receivedData) {
		

		
		int posSTX = 0;
		int posEOT = -1;
		receivedData = receivedData;

		for (int i = 0; i < receivedData.length; i++) {
			if (receivedData[i] == EOT) {
				posEOT = i;
				break;
			}
		}
		// Check ob ACK oder Data frame
		String type = "error";
		if (receivedData[6] == 0x44) {
			type = "Type of received data: data frame";
			this.isAck = false;
		} else if (Character.isDigit((char) receivedData[6])) {
			type = "Type of received data: ACK";
			this.isAck = true;
		} else {
			type = "Type of received data: ----";
		}
		return isAck;
	}

	public void sendAck(Connection connection, ClientManager clientManager) {
		ByteArrayOutputStream cmdArray = new ByteArrayOutputStream();
		execute = new ExecuteCmd(receivedData, getPTFAddr(receivedData), clientManager, controller);
		if (execute.sendNormalAck()) {
			String str = subBoxAddr + runningNrForACK + state;

			crc = new CrcValue(str.getBytes());

			cmdArray.write((byte) 0x02);
			cmdArray.writeBytes(str.getBytes());
			cmdArray.writeBytes(new byte[] { EOT, crc.getCrc16_low(), crc.getCrc16_high(), ETX });
			connection.send(cmdArray);
			setRunningNr();

		} else if (execute.getCmdType() == type.R_TYPE && getPTFAddr(receivedData) != null) {
			String str2;
			str2 = "" + subBoxAddr + runningNrForACK + state;

			crc = new CrcValue(str2.getBytes());
			cmdArray.write((byte) 0x02);
			cmdArray.writeBytes(str2.getBytes());
			cmdArray.writeBytes(new byte[] { EOT, crc.getCrc16_low(), crc.getCrc16_high(), ETX });
			connection.send(cmdArray);

			cmdArray = new ByteArrayOutputStream();

			if (clientManager.existPTF(getPTFAddr(receivedData))) {
				String id = controller.getType(getPTFAddr(receivedData));
				if (id.equals("PTF6N1")) {
					id = id.replace("PTF", "").replace("N", "N-");
					// Outputs: "6N-1"
				} else if (id.equals("PTF4N4")) {
					id = id.replace("PTF", "").replace("N", "N-");
					// Outputs: "4N-4"
				} else {
					id = "";
				}
				id = String.format("%-8s", id);

				String str = subBoxAddr + mainViewModel.getRunningNumber() + "D" + "<" + getPTFAddr(receivedData)
						+ "r000000000" + id;

				cmdArray.write((byte) 0x02);
				cmdArray.writeBytes(str.getBytes());
				cmdArray.writeBytes(new byte[] { EOT, crc.getCrc16_low(), crc.getCrc16_high(), ETX });
				queueHandler.add(cmdArray.toByteArray());
				setRunningNr();
			}
		}

		else if (execute.getCmdType() == type.R_TYPE) {

			byte[] data = new byte[24];
			String str = subBoxAddr + mainViewModel.getRunningNumber() + "D" + "r000000000" + subBoxId;
			String str2;
			str2 = "" + subBoxAddr + runningNrForACK + state;

			crc = new CrcValue(str2.getBytes());
			cmdArray.write((byte) 0x02);
			cmdArray.writeBytes(str2.getBytes());
			cmdArray.writeBytes(new byte[] { EOT, crc.getCrc16_low(), crc.getCrc16_high(), ETX });
			connection.send(cmdArray);

			cmdArray = new ByteArrayOutputStream();

			data = str.getBytes();

			crc = new CrcValue(data);

			cmdArray.write((byte) 0x02);
			cmdArray.writeBytes(data);
			cmdArray.writeBytes(new byte[] { EOT, crc.getCrc16_low(), crc.getCrc16_high(), ETX });
		//	connection.send(cmdArray);
			queueHandler.add(cmdArray.toByteArray());
			setRunningNr();

			sendI(connection);
			sendN(connection);
		}
	}

	private void sendN(Connection connection) {
		// TODO Auto-generated method stub
		HashMap<String, String> moduleMap = new HashMap<String, String>();
		moduleMap = controller.getMap();
		String str;
		String x = "1";

		for (Map.Entry<String, String> entry : moduleMap.entrySet()) {
			ByteArrayOutputStream cmdArray = new ByteArrayOutputStream();
			String key = entry.getKey();

			str = subBoxAddr + mainViewModel.getRunningNumber() + "D" + "<" + key + "n" + "0";

			crc = new CrcValue(str.getBytes());

			cmdArray.write((byte) 0x02);
			cmdArray.writeBytes(str.getBytes());
			cmdArray.writeBytes(new byte[] { EOT, crc.getCrc16_low(), crc.getCrc16_high(), ETX });
			str = "";
			setRunningNr();
			queueHandler.add(cmdArray.toByteArray());
		}
	}

	private void setRunningNr() {
		// TODO Auto-generated method stub
		MainViewModel mainViewModel = controller.getMainModel();

		if (mainViewModel.getRunningNumber() == 9) {
			mainViewModel.setRunningNumber(1);
		} else {
			mainViewModel.countRunningNumber();
		}
	}

	private void sendI(Connection connection) {
		HashMap<String, String> moduleMap = controller.getMap();
		String str;
		String state = "1";
		String slot = "0";

		for (Map.Entry<String, String> entry : moduleMap.entrySet()) {
			ByteArrayOutputStream cmdArray = new ByteArrayOutputStream();
			String key = entry.getKey();

			str = subBoxAddr + mainViewModel.getRunningNumber() + "D" + "i" + key + state + slot;

			crc = new CrcValue(str.getBytes());

			cmdArray.write((byte) 0x02);
			cmdArray.writeBytes(str.getBytes());
			cmdArray.writeBytes(new byte[] { EOT, crc.getCrc16_low(), crc.getCrc16_high(), ETX });
			str = "";
			setRunningNr();
			queueHandler.add(cmdArray.toByteArray());
		}
	}

	

	public void setAckData() {
		this.data = new byte[6];
		this.data[0] = addr[0];
		this.data[1] = addr[1];
		this.data[2] = addr[2];
		this.data[3] = addr[3];
		this.data[4] = (byte) runningNrForACK;
		this.data[5] = (byte) state; // noch state anpassen!

	}

	public String getReceivedSubBoxAddress(byte[] receivedData) {
		String str = showCmd(receivedData);
		str = str.substring(0, 4);
		return str;
	}

	public String showCmd(byte[] receivedData) {
		int endData = 0;
		String str = "";
		char[] dataChar;

		for (int i = 0; i < this.receivedData.length; i++) {
			if (this.receivedData[i] == EOT) {
				endData = i;
				break;
			}
		}

		byte[] data = Arrays.copyOfRange(receivedData, 1, endData);

		dataChar = new char[data.length];
		for (int t = 0; t < data.length; t++) {
			dataChar[t] = (char) data[t];
		}
		for (char letter : dataChar) {
			str += letter;
		}
		return str;
	}

	public String getIP() {
		return hostIp;
	}

	public int getSubBoxPort() {
		return subBoxPort;
	}

	public void setPort(int port) {
		this.subBoxPort = port;
	}

	public void setIP(String ip) {
		this.hostIp = ip;
	}

	public String getName() {
		return subBoxAddr;
	}

	public int getState() {
		return state;
	}

	public int getHostPort() {
		// TODO Auto-generated method stub
		return this.hostPort;
	}

	public String getHostIP() {

		return this.hostIp;
	}

	public String getControllerIP() {
		// TODO Auto-generated method stub
		return this.controllerIP;
	}

	public boolean containsModuleAddr(byte[] receivedData2) {
		String str = new String(receivedData2, StandardCharsets.UTF_8);
		return str.contains(">");
	}

	public String getPTFAddr(byte[] receivedData2) {
		// Byte-Array zu String konvertieren
		String str = new String(receivedData2, StandardCharsets.UTF_8);

		// Position des Zeichens '>' finden
		int index = str.indexOf('>');

		// Überprüfen, ob das Zeichen gefunden wurde und ob es genügend Zeichen danach
		// gibt
		if (index != -1 && index + 4 < str.length()) {
			// Die nächsten vier Zeichen extrahieren
			String extracted = str.substring(index + 1, index + 5);
			return extracted;
		}

		// Wenn das Zeichen nicht gefunden wurde oder nicht genügend Zeichen danach
		// vorhanden sind, Rückgabe null oder geeigneten Standardwert
		return null;
	}

	public void send0Ack(Connection connection, ClientManager clientManager) {
		// TODO Auto-generated method stub
		String str = "" + this.subBoxAddr + "09";
		CrcValue crc = new CrcValue(str.getBytes());
		ByteArrayOutputStream cmdArray = new ByteArrayOutputStream();
		cmdArray.write((byte) 0x02);
		cmdArray.writeBytes(this.subBoxAddr.getBytes());
		cmdArray.writeBytes("0".getBytes());
		cmdArray.writeBytes("9".getBytes());
		cmdArray.writeBytes(new byte[] { EOT, crc.getCrc16_low(), crc.getCrc16_high(), ETX });
		connection.send(cmdArray);
	}

	private void waitForAck(boolean b) {
		// TODO Auto-generated method stub

	}

	public boolean ack() {
		// TODO Auto-generated method stub
		return isAck;
	}

	public void addQueueHandler(MsgQueueHandler sendCommandAndReceiveAckHandler) {
		// TODO Auto-generated method stub
		this.queueHandler = sendCommandAndReceiveAckHandler;
	}
	

}
