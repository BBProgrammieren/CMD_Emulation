package Model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveProperties {
	FileOutputStream fileOutputStream;
	ObjectOutputStream objectOutputStream;
	private ClientManager clientManager;
	private SubBoxManager sbManager;

	public SaveProperties() {
		try {
			fileOutputStream = new FileOutputStream("PickTerm.txt");
			objectOutputStream = new ObjectOutputStream(fileOutputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void serialize(SubBoxManager sbManager) {
		this.sbManager = sbManager;
		try {

			objectOutputStream.writeObject(sbManager);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				objectOutputStream.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				objectOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void serialize(ClientManager clientManager) {
		this.clientManager = clientManager;
		try {

			objectOutputStream.writeObject(clientManager);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				objectOutputStream.flush();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				objectOutputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void deserializeAll() {
		FileInputStream fileInputStream;
		try {
			
			fileInputStream = new FileInputStream("PickTerm.txt");
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			ClientManager cm = (ClientManager) objectInputStream.readObject();
			SubBoxManager sbm = (SubBoxManager) objectInputStream.readObject();
			objectInputStream.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
