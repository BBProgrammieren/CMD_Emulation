package Model;

public class BridgeSubBox extends SubBox {
    
    private String key1;
    private String key2;
    private String key3;
    private String key4;
    private String msg;
    
    // Konstruktor
    public BridgeSubBox() {
    	
    	
        this.key1 = "";
        this.key2 = "";
        this.key3 = "";
        this.key4 = "";
        this.msg = "";
    }

    public String getSubBoxAddress() {
        throw new UnsupportedOperationException("Diese Methode ist in PTF6N1SubBox nicht unterstützt.");
    }

    public void setMainModel() {
        
    }

    public void setDisplayText() {
       
    }

    public String getAddr() {
        return "PTF6N1SubBox spezifische Adresse";
    }

    @Override
    public void setConnection(Connection connection) { // Setter-Methode für Connection
    	throw new UnsupportedOperationException("Initialize-Methode wird in PTF6N1SubBox nicht unterstützt.");		
	}
}
