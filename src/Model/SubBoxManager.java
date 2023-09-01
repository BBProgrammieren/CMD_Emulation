package Model;

import java.io.Serializable;
import java.util.HashMap;

public class SubBoxManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1555617815132707850L;
	private HashMap<String, SubBox> boxes;

	public SubBoxManager() {
		boxes = new HashMap<>();
	}

	public void addSubBox(SubBox box) {
		boxes.put(box.getName(), box);
	}

	public SubBox getBox(String name) {

		return boxes.get(name);

	}

	public void saveBox(SaveProperties saveProps) {
		// TODO Auto-generated method stub

	}

	public HashMap<String, SubBox> getHashMap() {
		// TODO Auto-generated method stub
		return boxes;
	}

}
