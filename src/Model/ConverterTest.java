package Model;

import org.junit.*;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;

public class ConverterTest {

    private Converter converter;

    @Before
    public void setUp() {
        converter = new Converter();
    }
    
    @Test
    public void testConvertCmdToByteArray() {
        String cmd = "AAAA1DR0";
        byte[] expected = new byte[]{2, 65, 65, 65, 65, 49, 68, 82, 48, 4, 83, 54, 3};
        byte[] result = Converter.convertCmdToByteArray(cmd);
        assertArrayEquals(expected, result);
    }


    @Test
    public void testGetByteArrayOutputStream() {
        String addrSubBox = "AAAA";
        String runningNr = "1";
        String type = "H";
        String direction = "<";
        String addr = "ffff";
        String message = "10";

        ByteArrayOutputStream result = converter.getByteArrayOutputStream(addrSubBox, runningNr, type, direction, addr, message);
        assertNotNull(result);
        // Weitere Assertions 
    }

    @Test
    public void testGetByteArrayOutputStreamFori() {
        String addrSubBox = "AAAA";
        String runningNr = "1";
        String type = "D";
        String addr = "ffff";
        String state = "0";
        String slot = "0";

        ByteArrayOutputStream result = converter.getByteArrayOutputStreamFori(addrSubBox, runningNr, type, addr, state, slot);
        assertNotNull(result);
        // Weitere Assertions 
    }
}
