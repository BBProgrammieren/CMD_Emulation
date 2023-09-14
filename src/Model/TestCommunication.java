package Model;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.junit.Before;
import org.junit.Test;

public class TestCommunication {
	
    private DatagramSocket ds;
    private int iport = 2241;
    private int sourcePort = 2240;

    @Before
    public void setUp() throws Exception {
        ds = new DatagramSocket(sourcePort);

        // Starting the StartApplication using Reflection
        Class<?> startApplicationClass = Class.forName("Model.StartApplication");
        Method runWithParametersMethod = startApplicationClass.getDeclaredMethod("runWithParameters", int.class, int.class, String.class, String.class, String.class);
        runWithParametersMethod.invoke(null, 2241, 2240, "127.0.0.1", "127.0.0.1", "AAAA");
    }

    @Test
    public void testSendCommandToMainApplication() throws IOException {
        String data = "AAAA1DR0";  // Dies ist ein Beispielbefehl, ähnlich dem in SendCommandtoSubBox
        byte[] byteData = data.getBytes();
        int crc16 = SendCommandtoSubBox.crc16_2(byteData);
        
        byte crc16_low = (byte) crc16;
        byte crc16_high = (byte) (crc16 >> 8);

        ByteArrayOutputStream cmdArray = new ByteArrayOutputStream();
        cmdArray.write((byte) 0x02);
        cmdArray.writeBytes(data.getBytes());
        cmdArray.writeBytes(new byte[] { 0x04, crc16_low, crc16_high, 0x03 });

        DatagramPacket dp = new DatagramPacket(cmdArray.toByteArray(), cmdArray.size());
        InetAddress empfaenger = InetAddress.getByName("127.0.0.1");
        dp.setPort(iport);
        dp.setAddress(empfaenger);
        ds.send(dp);

        byte[] buffer = new byte[256];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
        ds.receive(receivePacket);


        assertNotNull(receivePacket.getData());
    }

}
