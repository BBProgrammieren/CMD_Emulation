package Model;

import javafx.beans.property.SimpleStringProperty;

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


	@Override
	public void setDefaultDisplay(char[] dataChar) {
	
			char[] dmode = new char[4];
			System.arraycopy(dataChar, 7, dmode, 0, 4);
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
