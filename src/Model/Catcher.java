package Model;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class Catcher implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8012064938453619769L;
	// key, value
	private HashMap<String, String> moduleMap = new HashMap<String, String>();
	transient private HashMap<Object, String> typeAddressMap;
	transient private String cmd;
	transient private HashMap<Stage, String> actuallyOpened = new HashMap<Stage, String>();
	transient private MainViewModel mainViewModel;
	transient private String state;
	transient private Converter converter;

	public Catcher() {
		typeAddressMap = new HashMap<Object, String>();

	}

	public void addTypeAddressMap(Object instance, String address) {
		typeAddressMap.put(instance, address);
	}

	public void removeTypeAddressMap(String address) {
		typeAddressMap.remove(address);
	}

	public String addNewModule(String moduleAddress, String type) {
		String str;

		if (moduleAddress.length() == 4) {
			if (moduleMap.containsKey(moduleAddress)) {
				str = "Module with same address added already!";
				return str;
			} else {
				moduleMap.put(moduleAddress, type);
				sendI(moduleAddress, "1");
				return null;
			}
		} else {
			str = "Address too long or too short! 4 Characters only!";
			return str;
		}
	}

	private void sendI(String moduleAddress, String state) {
		// TODO Auto-generated method stub
		String str;
		String slot = "1";
		converter = new Converter();
		ByteArrayOutputStream cmdArray = converter.getByteArrayOutputStreamFori(mainViewModel.getSubBoxText().get(),
				String.valueOf(mainViewModel.getRunningNumber()), "D", moduleAddress, state, slot);

		Connection connection = mainViewModel.getConnection();
		if (connection != null) {
			connection.send(cmdArray);
			setRunningNr();
		}
	}

	private void setRunningNr() {
		// TODO Auto-generated method stub

		if (mainViewModel.getRunningNumber() == 9) {
			mainViewModel.setRunningNumber(1);
		} else {
			mainViewModel.countRunningNumber();
		}
	}

	public HashMap<String, String> getMap() {
		return this.moduleMap;
	}

	public ListView<String> getList() {
		ListView<String> listView = new ListView<>();
		for (Map.Entry<String, String> entry : moduleMap.entrySet()) {
			String keyAndValue = entry.getKey() + "  (" + entry.getValue() + ")";
			listView.getItems().add(keyAndValue);
		}
		return listView;
	}

	public String getType(String addr) {
		return moduleMap.get(addr);
	}

	public void removeModule(String selectedItem) {
		// TODO Auto-generated method stub
		moduleMap.remove(selectedItem);
		typeAddressMap.remove(selectedItem);
		sendI(selectedItem, "0");
	}

	public void setCommand(String cmd) {
		this.cmd = cmd;
	}

	public String getCommand() {
		return cmd;
	}

	public void setModuleMap(HashMap hashMap) {
		// TODO Auto-generated method stub
		this.moduleMap = hashMap;
	}

	public Object getModuleModel(String addr) {
		// TODO Auto-generated method stub
		for (Object key : typeAddressMap.keySet()) {
			if (typeAddressMap.get(key).equals(addr)) {
				return key;
			}
		}
		return null;
	}

	public void setTypeAddressMap() {
		// TODO Auto-generated method stub
		final String ptf6n1 = "PTF6N1";
		final String ptf4n4 = "PTF4N4";
		for (Map.Entry<String, String> entry : moduleMap.entrySet()) {
			String key = entry.getKey();

			if (this.typeAddressMap == null) {
				this.typeAddressMap = new HashMap<Object, String>();
			}

			// Überprüfen, ob der Schlüssel bereits als Wert in typeAddressMap vorhanden ist
			if (!this.typeAddressMap.containsValue(key)) {
				if (ptf6n1.equals(moduleMap.get(key))) {
					typeAddressMap.put(new PTF6N1(key, this), key);
				} else if (ptf4n4.equals(moduleMap.get(key))) {
					typeAddressMap.put(new PTF4N4(key, this), key);
				}
			}
		}

	}

	public void removeOpened(Stage newStage) {
		// TODO Auto-generated method stub
		if (actuallyOpened == null) {
			actuallyOpened = new HashMap<Stage, String>();
		}
		actuallyOpened.remove(newStage);
	}

	public ArrayList<Stage> getOpened() {
		if (actuallyOpened == null) {
			actuallyOpened = new HashMap<Stage, String>();
		}
		return new ArrayList<Stage>(actuallyOpened.keySet());
	}

	public void addOpened(Stage newStage, String addr) {
		// TODO Auto-generated method stub
		if (actuallyOpened == null) {
			actuallyOpened = new HashMap<Stage, String>();
		}
		actuallyOpened.put(newStage, addr);
	}

	public boolean containsOpened(String addr) {
		// TODO Auto-generated method stub
		if (actuallyOpened == null) {
			actuallyOpened = new HashMap<Stage, String>();
		}
		return actuallyOpened.containsValue(addr);
	}

	public MainViewModel getMainModel() {
		// TODO Auto-generated method stub
		return this.mainViewModel;
	}

	public void setMainModel(MainViewModel mainViewModel2) {
		// TODO Auto-generated method stub
		this.mainViewModel = mainViewModel2;
	}
}
