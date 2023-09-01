package Model;

import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PTFView {
	
	private Stage newWindow; 
	private String address;
	private boolean isClosed = false;
	
	public PTFView(String address) {
		this.address = address;
        this.newWindow = new Stage();
        newWindow.setTitle(address);
        VBox vbox = new VBox();
        Scene scene = new Scene(vbox, 300, 200);
        newWindow.setScene(scene);
        newWindow.initStyle(StageStyle.UTILITY);
        newWindow.setOnCloseRequest(event -> handleClose());
        newWindow.show();
	}

	private void handleClose() {
		this.isClosed = true;
	}
	
	public boolean isClosed() {
		return this.isClosed;
	}


	public String getAddress() {
		return this.address;
	}
	
    public void close() {
        newWindow.close();
    }

}
