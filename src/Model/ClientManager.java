package Model;

import java.util.HashMap;

public class ClientManager{

	private static ClientManager instance = null;
	private HashMap<String, PTFModuleInterface> clients;

	// Privater Konstruktor, sodass kein anderer ClientManager direkt instanziert werden kann
	private ClientManager() {
		clients = new HashMap<>();
	}

	// Öffentliche Methode zur Abrufung der Singleton-Instanz
	public static ClientManager getInstance() {
		if (instance == null) {
			instance = new ClientManager();
		}
		return instance;
	}

	public void addPtf6N1(PTF6N1 ptf) {
		clients.put(ptf.getAddr(), ptf);
	}

	public void addPtf4(PTF4N4 ptf) {
		clients.put(ptf.getAddr(), ptf);
	}

	public PTFModuleInterface getClient(String addr) {
		return clients.get(addr);
	}
	
	public boolean existPTF(String addr) {
	    return isClientValid(addr);
	}

	private boolean isClientValid(String addr) {
	    return clients.containsKey(addr) || addr == null;
	}
	
	public HashMap<String, PTFModuleInterface> getHashMap(){
		return clients;		
	}

	public void addPtf4N4(PTF4N4 ptf) {
		clients.put(ptf.getAddr(), ptf);
	}
}
