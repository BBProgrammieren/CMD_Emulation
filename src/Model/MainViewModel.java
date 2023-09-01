package Model;

import javafx.beans.value.ObservableValue;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MainViewModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6060771681392574490L;
	public boolean isFirstClickTextField;
	public boolean isFirstClickSubBoxPortText;
	public boolean isFirstClickSubBoxText;
	public boolean isFirstClickHostPortText;

	public boolean isSetHostIP;
	public boolean isSetSubBoxPort;
	public boolean isSetHostPort;
	public boolean isSetSubBoxName;
	public boolean isSetControllerIP;

	transient private SimpleStringProperty mainLabelText;
	transient private SimpleStringProperty hostIPText;
	transient private SimpleStringProperty controllerIPText;
	transient private SimpleStringProperty subBoxPortText;
	transient private SimpleStringProperty hostPortText;
	transient private SimpleStringProperty subBoxNameText;

	private Connection connection;

	SerializerClass serializer = new SerializerClass();
	private Boolean deserialized;
	private boolean startIsDisabled;
	private boolean closeIsDisabled;
	private boolean manageIsDisabled;
	private int runningNr;

	MainViewModel() {
		
		runningNr = 1;

		startIsDisabled = false;
		closeIsDisabled = true;
		manageIsDisabled = true;

		isSetHostIP = false;
		isSetSubBoxPort = false;
		isSetHostPort = false;
		isSetSubBoxName = false;
		isSetControllerIP = false;

		isFirstClickTextField = true;
		isFirstClickSubBoxPortText = true;
		isFirstClickSubBoxText = true;
		isFirstClickHostPortText = true;

		mainLabelText = new SimpleStringProperty("Please enter all informations and add some modules.");
		hostIPText = new SimpleStringProperty("IP...");
		controllerIPText = new SimpleStringProperty(getOwnIP());
		subBoxPortText = new SimpleStringProperty("Set SubBox Port...");
		hostPortText = new SimpleStringProperty("Set Host Port...");
		subBoxNameText = new SimpleStringProperty("Enter a SubBox name...");
	}

	public void startProcess(Catcher controller) {
		serializer.serializeMainViewModel(this);
		new MainClass(Integer.parseInt(subBoxPortText.get()), Integer.parseInt(hostPortText.get()), hostIPText.get(),
				controllerIPText.get(), subBoxNameText.get(), controller, this);
	}

	private String getOwnIP() {
		InetAddress ip = null;
		try {
			ip = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String ipAddress = ip.getHostAddress();
		return ipAddress;
	}

	public ObservableValue<String> getMainLabelText() {
		return mainLabelText;
	}

	public void updateMainLabelText(String str) {
		this.mainLabelText.set(str);
	}

	public StringProperty getHostIPText() {
		return hostIPText;
	}

	public StringProperty getControllerIPText() {
		return controllerIPText;
	}

	public StringProperty getSubBoxPortText() {
		return subBoxPortText;
	}

	public StringProperty getHostPortText() {
		return hostPortText;
	}

	public StringProperty getSubBoxText() {
		return subBoxNameText;
	}

	public void clearTextFieldText() {
		hostIPText.set(null);
	}

	public void clearSubBoxPortText() {
		subBoxPortText.set(null);
	}

	public void clearHostPortText() {
		hostPortText.set(null);
	}

	public void clearSubBoxNameText() {
		subBoxNameText.set(null);
	}

	public void setConnection(Connection connection) {
		// TODO Auto-generated method stub
		this.connection = connection;
	}

	public Connection getConnection() {
		if(connection != null) {
			return this.connection;
		}
		else {
			return connection = new Connection(Integer.valueOf(getSubBoxPortText().get()),
					Integer.valueOf(getHostPortText().get()), getHostIPText().get(),
					getControllerIPText().get());
		}
	}

	public void closeConnection() {
		connection.closeConnection();
		;
	}

	public boolean connected() {
		return connection.checkConnection();
	}

	public void setMainLabelText(String readObject) {
		// TODO Auto-generated method stub
		this.mainLabelText.set(readObject);
	}

	public void setHostIPText(String readObject) {
		// TODO Auto-generated method stub
		this.hostIPText.set(readObject);
	}

	public void setHostPortText(String readObject) {
		// TODO Auto-generated method stub
		this.hostPortText.set(readObject);
	}

	public void setSubBoxPortText(String readObject) {
		// TODO Auto-generated method stub
		this.subBoxPortText.set(readObject);
	}

	public void setControllerIPText(String readObject) {
		// TODO Auto-generated method stub
		this.controllerIPText.set(readObject);
	}

	public void setSubBoxText(String readObject) {
		// TODO Auto-generated method stub
		this.subBoxNameText.set(readObject);
	}

	public void setDeserializedBool(Boolean deserialized) {
		// TODO Auto-generated method stub
		this.deserialized = deserialized;
	}

	public Boolean getDeserializedBool() {
		return this.deserialized;
	}

	public void startIsDisabled(boolean b) {
		// TODO Auto-generated method stub
		this.startIsDisabled = b;
	}

	public boolean startIsDisabled() {
		// TODO Auto-generated method stub
		return this.startIsDisabled;
	}

	public void closeIsDisabled(boolean b) {
		// TODO Auto-generated method stub
		this.closeIsDisabled = b;
		this.manageIsDisabled = b;
	}

	public boolean closeIsDisabled() {
		// TODO Auto-generated method stub
		return this.closeIsDisabled;
	}

	public boolean allParams() {
		if(subBoxNameText.get() != null && !subBoxNameText.get().isEmpty() &&
		   controllerIPText.get() != null && !controllerIPText.get().isEmpty() &&
		   subBoxPortText.get() != null && !subBoxPortText.get().isEmpty() &&
		   hostPortText.get() != null && !hostPortText.get().isEmpty() &&
		   hostIPText.get() != null && !hostIPText.get().isEmpty()) {
			return true;
		}
		else {
			return false;
		}
	}

	public boolean manageIsDisabled() {
		// TODO Auto-generated method stub
		return this.manageIsDisabled;
	}
	
	public void setRunningNumber(int num) {
		this.runningNr = num;
	}
	
	public int getRunningNumber() {
		return this.runningNr;
	}

	public void countRunningNumber() {
		// TODO Auto-generated method stub
		runningNr++;
	}
	
}
