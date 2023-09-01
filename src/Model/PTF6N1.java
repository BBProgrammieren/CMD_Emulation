package Model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import ViewModel.PTF6N1Controller;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class PTF6N1 implements PTFModuleInterface {

	private static final byte EOT = 0;
	private char cmdChar;
	private byte[] data;
	private char[] dataChar;
	private CmdType type;
	private String addr;
	private String subBoxAddress;

	SimpleStringProperty displayText;
	private Catcher catcher;
	private String characterNum;
	private String key1;
	private String key2;
	private String key3;
	private String key4;
	private String msg;
	private MainViewModel mainViewModel;
	private Converter converter;

	public PTF6N1(String addr, Catcher catcher) {

		this.addr = addr;
		this.catcher = catcher;
		displayText = new SimpleStringProperty();
		setMainModel();
	}

	public String getSubBoxAddress() {
		return mainViewModel.getSubBoxText().get();
	}

	public void setMainModel() {
		this.mainViewModel = catcher.getMainModel();
	}

	public void setDisplayText(String str) {
		openModule();
		Platform.runLater(() -> {
			displayText.set(str);
		});
	}

	public String getAddr() {
		return this.addr;
	}

	public void defineSound(char[] dataChar) {
		// TODO Auto-generated method stub
		String statusKey1 = null;
		String statusKey2 = null;
		String statusKey3 = null;
		String statusKey4 = null;
		this.dataChar = dataChar;

		System.out.println("Lautst�rke:" + dataChar[1]);

		if (dataChar[8] == '0') {
			statusKey1 = "Ton aus";
			System.out.println(statusKey1);
		} else if (dataChar[8] == '1') {
			statusKey1 = "Ton an";
			System.out.println(statusKey1);
		}
		if (dataChar[9] == '0') {
			statusKey2 = "Ton aus";
		} else if (dataChar[9] == '1') {
			statusKey2 = "Ton an";
		}
		if (dataChar[10] == '0') {
			statusKey3 = "Ton aus";
		} else if (dataChar[10] == '1') {
			statusKey3 = "Ton an";
		}
		if (dataChar[11] == '0') {
			statusKey4 = "Ton aus";
		} else if (dataChar[11] == '1') {
			statusKey4 = "Ton an";
		}

		System.out.println("Lautst�rke auf gesetzt: " + dataChar[1]);
		System.out.println("Frequenz 1 auf gesetzt: " + dataChar[2] + dataChar[3]);
		System.out.println("L�nge 1 auf gesetzt: " + dataChar[4] + dataChar[5]);
		System.out.println("Frequenz 2 auf gesetzt: " + dataChar[6] + dataChar[7]);
		System.out.println("L�nge 2 auf gesetzt: " + dataChar[8] + dataChar[9]);
		System.out.println("Frequenz 3 auf gesetzt: " + dataChar[10] + dataChar[11]);
		System.out.println("L�nge 3 auf gesetzt: " + dataChar[12] + dataChar[13]);
		System.out.println("Key 1 auf gesetzt: " + dataChar[14] + " Status: " + statusKey1);
		System.out.println("Key 2 auf gesetzt: " + dataChar[15] + " Status: " + statusKey2);
		System.out.println("Key 3 auf gesetzt: " + dataChar[16] + " Status: " + statusKey3);
		System.out.println("Key 4 auf gesetzt: " + dataChar[17] + " Status: " + statusKey4);
	}

	public void setBrightness(char[] dataChar) {
		// TODO Auto-generated method stub
		System.out.println("Helligkeit gesetzt: " + dataChar[1] + dataChar[2]);

	}

	@Override
	public void playSound(char[] dataChar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void disableDisplay() {
		// TODO Auto-generated method stub

	}

	public ObservableValue<? extends String> getDisplayText() {
		// TODO Auto-generated method stub
		return displayText;
	}

	public void increaseValue() {
		if (displayText.get() != null) {
			int i = Integer.parseInt(displayText.get());
			if (i < 999999) { // Only increase if it's less than 999999
				i++;
				displayText.set(String.format("%06d", i));
			}
		}
	}

	public void decreaseValue() {
		if (displayText.get() != null) {
			int i = Integer.parseInt(displayText.get());
			if (i > 000000) { // Only decrease if it's more than 000000
				i--;
				displayText.set(String.format("%06d", i));
			}
		}
	}

	@Override
	public void setDefaultDisplay(char[] dataChar) {
		// TODO Auto-generated method stub
		char[] dmode = new char[6];
		System.arraycopy(dataChar, 7, dmode, 0, 6);
		String str = "";
		for (int i = 0; i < dmode.length; i++) {
			switch (dmode[i]) {
			case '0':
				str += " ";
				break;
			case '1':
				str += dataChar[i + 1];
				break;
			case '2':
				str += "";
				break;
			case '3':
				str += "";
				break;
			default:
				str += "";
				break;
			}
		}

		setDisplayText(str);
	}

	public void openModule() {
		if (catcher.containsOpened(this.addr)) {
			return;
		}

		Platform.runLater(() -> {
			FXMLLoader loader = new FXMLLoader();
			Scene newScene;
			Stage newStage = new Stage();

			try {
				PTF6N1Controller test = new PTF6N1Controller();
				test.setPTF6N1Model((PTF6N1) catcher.getModuleModel(this.addr));
				test.setStage(newStage);
				test.setCatcher(catcher);

				loader.setLocation(getClass().getResource("/View/PTF6N1View.fxml"));
				loader.setController(test);

				newScene = new Scene(loader.load());
				newStage.setTitle(this.addr);
				newStage.setScene(newScene);
				catcher.addOpened(newStage, this.addr);

				newStage.show();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	public void keyMessage(String keyState, String kt, String ts) {
		if (!mainViewModel.allParams()) {
			return;
		}
		// Überprüfen, ob der keyState eine gültige Länge hat
		if (keyState.length() != 4) {
			System.out.println("Ungültiger keyState. Es müssen genau vier Zeichen sein.");
		}

		// Überprüfen, ob nur eine der Tasten den Status '1' oder '3' hat
		int countOfOneAndThree = 0;
		for (int i = 0; i < keyState.length(); i++) {
			if (keyState.charAt(i) == '1' || keyState.charAt(i) == '3') {
				countOfOneAndThree++;
			}
		}
		if (countOfOneAndThree > 1) {
			System.out.println("Ungültiger keyState. Nur eine der Tasten kann den Status '1' oder '3' haben.");
		}

		// Überprüfen, ob die Längen von kt und ts korrekt sind
		if (kt.length() != 2 || ts.length() != 2) {
			System.out.println("Ungültige kt oder ts. Beide müssen genau zwei Zeichen lang sein.");
		}

		// Formatierung der Nachricht
		this.msg = "k" + keyState + kt + ts;

		sendMsg();
	}

	private void sendMsg() {

		converter = new Converter();
//		Connection con = new Connection(Integer.valueOf(mainViewModel.getSubBoxPortText().get()),
//				Integer.valueOf(mainViewModel.getHostPortText().get()), mainViewModel.getHostIPText().get(),
//				mainViewModel.getControllerIPText().get());
//		
		Connection con = mainViewModel.getConnection();
		if(con != null) {
			con.send(converter.getByteArrayOutputStream(getSubBoxAddress(), String.valueOf(mainViewModel.getRunningNumber()), "D", "<", addr,
					this.msg));
			
			if (mainViewModel.getRunningNumber() == 9) {
				mainViewModel.setRunningNumber(1);
			} else {
				mainViewModel.countRunningNumber();
			}
		}		
	}

	public String getDialogState() {
		return "01";
	}

	public String getDialogId() {
		return "1";
	}

	public String getData() {
		return displayText.get();
	}

	public String getKeyNum() {
		return this.characterNum;
	}

	public String getTime() {
		return "10";
	}

	@Override
	public void setButtonRole(char[] dataChar) {
		key1 = String.valueOf(dataChar[1]);
		key2 = String.valueOf(dataChar[2]);
		key3 = String.valueOf(dataChar[3]);
		key4 = String.valueOf(dataChar[4]);
	}

	public void setKeyState(String key1, String key2, String key3, String key4) {
		this.key1 = key1;
		this.key2 = key2;
		this.key3 = key3;
		this.key4 = key4;
	}

	public String getKeyState(String key) {
		switch (key) {
		case "key1":
			return key1;
		case "key2":
			return key2;
		case "key3":
			return key3;
		case "key4":
			return key4;
		default:
			return "Invalid key";
		}
	}

	public String getTs() {
		return "11";

	}

	@Override
	public void setKeyState() {
		// TODO Auto-generated method stub

	}

}
