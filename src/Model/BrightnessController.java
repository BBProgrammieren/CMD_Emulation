package Model;

public class BrightnessController {
	 public static String processBrightness(char[] dataChar) {
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

			return str;
		}
}
