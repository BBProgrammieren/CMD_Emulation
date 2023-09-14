package Model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientManagerTest {

    private ClientManager clientManager;
    private Catcher catcher;

    @Before
    public void setUp() {
        clientManager = ClientManager.getInstance();
        catcher = new Catcher();
    }

    @Test
    public void testExistPTF() {
        PTF6N1 testClient = new PTF6N1("ssss", catcher);
        clientManager.addPtf6N1(testClient);

        assertTrue(clientManager.existPTF("ssss"));
        assertFalse(clientManager.existPTF("wwww"));
    }
}
