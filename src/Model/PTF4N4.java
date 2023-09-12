package Model;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PTF4N4 implements PTFModuleInterface{

	private String addr;

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

	public PTF4N4(String addr, Catcher catcher) {

		this.addr = addr;
		this.catcher = catcher;
		displayText = new SimpleStringProperty();
		setMainModel();
	}
	
	public void setMainModel() {
		this.mainViewModel = catcher.getMainModel();
	}


	@Override
	public void defineSound(char [] dataChar) {
		// TODO Auto-generated method stub
		
	}

	public void setBrightness() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSound(char [] dataChar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disableDisplay() {
		// TODO Auto-generated method stub
		
	}

	public String getAddr() {
		// TODO Auto-generated method stub
		return this.addr;
	}

	@Override
	public void setBrightness(char[] dataChar) {
		// TODO Auto-generated method stub
		
	}
	
	public void setDisplayText(String str) {
		openModule();
		Platform.runLater(() -> {
			displayText.set(str);
		});
	}
	
	public void openModule() {
		if (catcher.containsOpened(this.addr)) {
			return;
		}

		Platform.runLater(() -> {
			FXMLLoader loader = new FXMLLoader();
			Scene newScene;
			Stage newStage = new Stage();

//			try {
//				PTF6N1Controller test = new PTF6N1Controller();
//				test.setPTF6N1Model((PTF6N1) catcher.getModuleModel(this.addr));
//				test.setStage(newStage);
//				test.setCatcher(catcher);
//
//				loader.setLocation(getClass().getResource("/View/PTF6N1View.fxml"));
//				loader.setController(test);
//
//				newScene = new Scene(loader.load());
//				newStage.setTitle(this.addr);
//				newStage.setScene(newScene);
//				catcher.addOpened(newStage, this.addr);
//
//				newStage.show();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		});
	}


	@Override
	public void setDefaultDisplay(char[] dataChar) {
	
		String brightnessValue = BrightnessController.processBrightness(dataChar);
		setDisplayText(brightnessValue);
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
	public void setKeyState() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setButtonRole(char[] dataChar) {
		// TODO Auto-generated method stub
		
	}

	
}
