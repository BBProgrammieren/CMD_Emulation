package Model;

import java.util.HashMap;

public class ClientManager{

	private HashMap<String, PTFModuleInterface> clients;

	public ClientManager() {
		clients = new HashMap<>();
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
		boolean bool = false;
		if (clients.containsKey(addr) || addr == null) {
			bool = true;
		}
		return bool;
	}
	
	public HashMap<String, PTFModuleInterface> getHashMap(){
		return clients;		
	}

	public void addPtf4N4(PTF4N4 ptf) {
		// TODO Auto-generated method stub
		clients.put(ptf.getAddr(), ptf);
	}
}
