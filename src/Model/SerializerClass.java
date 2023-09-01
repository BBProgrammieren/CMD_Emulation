package Model;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.StringProperty;

public class SerializerClass {
	private MainViewModel instance;

	public void serializeMainViewModel(MainViewModel instance) {
		try {
			ArrayList<String> dataList = new ArrayList<>();
			dataList.add(instance.getMainLabelText().getValue());
			dataList.add(instance.getHostIPText().getValue());
			dataList.add(instance.getHostPortText().getValue());
			dataList.add(instance.getSubBoxPortText().getValue());
			dataList.add(instance.getControllerIPText().getValue());
			dataList.add(instance.getSubBoxText().getValue());

			FileOutputStream fileOut = new FileOutputStream("savedState.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(dataList);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public void deserializeMainViewModel(MainViewModel instance) {
		File file = new File("savedState.ser");
		if (!file.exists() || !file.canRead()) {
			System.err.println("Die Datei 'savedState.ser' existiert nicht oder kann nicht gelesen werden.");
			return;
		}

		try {
			FileInputStream fileIn = new FileInputStream("savedState.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			ArrayList<String> dataList = (ArrayList<String>) in.readObject();

			if (dataList != null && dataList.size() >= 6) {
				((StringProperty) instance.getMainLabelText()).setValue(dataList.get(0));
				((StringProperty) instance.getHostIPText()).setValue(dataList.get(1));
				((StringProperty) instance.getHostPortText()).setValue(dataList.get(2));
				((StringProperty) instance.getSubBoxPortText()).setValue(dataList.get(3));
				((StringProperty) instance.getControllerIPText()).setValue(dataList.get(4));
				((StringProperty) instance.getSubBoxText()).setValue(dataList.get(5));
			} else {
				System.err.println("Die gespeicherte Datei hat nicht genug Daten zum Lesen.");
			}

			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
		}
	}

	public void serializeModules(Catcher instance) {
		try {
			FileOutputStream fileOut = new FileOutputStream("catcher.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(instance); // Serialize the entire Catcher object
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public Catcher deserializeModules() {
	    Catcher controller = null;
		File file = new File("catcher.ser");
	    
	    try {
	    	if(file.length() == 0) {
	    	  controller = new Catcher();
	    	} else {
	    	    FileInputStream fileIn = new FileInputStream(file);
	    	    ObjectInputStream in = new ObjectInputStream(fileIn);
	    	    controller = (Catcher) in.readObject();
	    	    in.close();
	    	    fileIn.close();
	    	}
	    } catch (IOException | ClassNotFoundException e) {
	        e.printStackTrace();
	    }

	    return controller;
	}

}
