package Model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.ReferenceCountUtil;
import javafx.application.Platform;

public class MainClass {

	private Connection connection;
	private SubBox subBox;
	private MainViewModel mainViewModel;
	private Catcher controller;
	private byte[] receivedData;
	private final String ptf6n1 = "PTF6N1";
	private final String ptf4n4 = "PTF4N4";
	private ClientManager clientManager = new ClientManager();
	private String subBoxAddress;

	public MainClass(int setSubBoxPort, int setHostPort, String setHostIP, String setControllerIP, String subBoxAddress,
			Catcher controller, MainViewModel mainViewModel) {
		this.subBoxAddress = subBoxAddress;
		this.controller = controller;
		this.mainViewModel = mainViewModel;
		SaveProperties saveProps = new SaveProperties();

		SubBoxManager boxManager = new SubBoxManager();
//		boxManager.addSubBox(new SubBox(2241, "127.0.0.1", "SUB BOX 3"));
//		SubBox subBox = boxManager.getBox("SUB BOX 3");	

		boxManager.addSubBox(
				new SubBox(setSubBoxPort, setHostIP, setHostPort, subBoxAddress, setControllerIP, controller));
		this.subBox = boxManager.getBox(subBoxAddress);

		parseClients();

		// saveProps.serialize(boxManager);
		// saveProps.serialize(clientManager);

		this.connection = new Connection(subBox.getSubBoxPort(), subBox.getHostPort(), subBox.getHostIP(),
				subBox.getControllerIP());

		subBox.setConnection(connection);

		try {
			connection.startup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MsgQueueHandler sendCommandAndReceiveAckHandler = new MsgQueueHandler(connection);
		
		connection.addHandlerCallback(new MessageHandler() {

			@Override
		    public boolean acceptInboundMessage(byte[] msg) throws Exception {
		        return msg.length > 0 && !subBox.isAck(msg);
		    }
			
			@Override
			public void handleMessage(ChannelHandlerContext ctx, byte[] msg) {
				

				
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						mainViewModel.updateMainLabelText("Message received!");
					}
				});

				receivedData = msg;
				if (!subBox.isAck(receivedData)) {
					System.out.println("Message Received new: " + Arrays.toString(receivedData));
					parseMsg();
				}
	

				//sendCommandAndReceiveAckHandler.add(msg);
			}
		});
		
		
		
		connection.addHandlerCallback(sendCommandAndReceiveAckHandler);
		subBox.addQueueHandler(sendCommandAndReceiveAckHandler);
		

		mainViewModel.setConnection(connection);
	}

	public void parseMsg() {

		ErrorHandle approve = new ErrorHandle();

		if (approve.approveCRC(receivedData) == false) {
			System.out.println("Crc is not correct!");
		} else {
			System.out.println("CRC is correct.");
			System.out.println("CRC high: " + approve.getcrcHigh());
			System.out.println("CRC low: " + approve.getcrcLow());
			subBox.setParams(receivedData);
			System.out.println(subBox.dataType(receivedData));
			System.out.println("Data in ASCII: " + subBox.showCmd(receivedData));
			System.out.println("Data in Bytes: " + subBox.showReceivedData(receivedData));
			if (subBox.ack()) {
				return;
			}
			if (subBox.getReceivedSubBoxAddress(receivedData).equals("????")) {
				subBox.send0Ack(connection, clientManager);
				System.out.println("ACK was sent successfully!");
				return;
			}

			if (clientManager.existPTF(subBox.getPTFAddr(receivedData))
					&& subBox.getReceivedSubBoxAddress(receivedData).equals(subBoxAddress)) {
				if (receivedData[6] == 0x44) {
					subBox.sendAck(connection, clientManager);
					System.out.println("ACK was sent successfully!");
				}
			} else {
				System.out.println("No module or controller existing with this address!");
			}
		}
	}

	public void parseClients() {
		HashMap<String, String> map = controller.getMap();

		for (Entry<String, String> entry : map.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();

			if (value.equals(ptf6n1)) {
				clientManager.addPtf6N1((PTF6N1) controller.getModuleModel(key));
			} else if (value.equals(ptf4n4)) {
				clientManager.addPtf4N4((PTF4N4) controller.getModuleModel(key));
			} else {

			}
		}
	}
}
