package Model;

public class MockSubBox extends SubBox {

    public MockSubBox(int subBoxPort, String hostIp, int setHostPort, String subBoxAddr, String controllerIP,
                      Catcher controller) {
        super(subBoxPort, hostIp, setHostPort, subBoxAddr, controllerIP, controller);
    }

    @Override
    public void setConnection(Connection connection) {
        // Mock-Implementierung
    }

    @Override
    public String showReceivedData(byte[] packet) {
        return "Mock Daten";
    }

    @Override
    public void setParams(byte[] receivedData) {
        // Mock-Implementierung
    }

    @Override
    public String dataType(byte[] receivedData) {
        return "Mock Typ";
    }

    @Override
    public boolean isAck(byte[] receivedData) {
        return true; 
    }

    @Override
    public void sendAck(Connection connection, ClientManager clientManager) {
       
    }

    // ... für alle anderen Methoden in der Klasse SubBox
}
