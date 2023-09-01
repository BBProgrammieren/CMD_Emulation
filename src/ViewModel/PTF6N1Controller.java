package ViewModel;

import javafx.fxml.FXML;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.beans.value.ChangeListener;
import javafx.scene.text.Text;

import Model.Catcher;
import Model.PTF4N4;
import Model.PTF6N1;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

public class PTF6N1Controller {

	@FXML
	private Label lblPTF6N1;
	@FXML
	private TextField displaytxt;
	@FXML
	private Button fButton;
	@FXML
	private Button minusButton;
	@FXML
	private Button plusButton;
	@FXML
	private Button confirmButton;
	@FXML
	private Circle circleLamp;
	@FXML
	private Ellipse upLed;
	@FXML
	private Ellipse downLed;
	
	private PTF6N1 ptf6n1Model;
	private Stage stage;
	private Catcher catcher;
	private PTF4N4 moduleModel;
	


	public void initialize() {
		displaytxt.textProperty().bind(ptf6n1Model.getDisplayText());
		
		stage.setOnCloseRequest(event -> {
			catcher.removeOpened(stage);
		});


	    plusButton.setOnAction(e -> {
	        ptf6n1Model.increaseValue();
	    });

	    minusButton.setOnAction(e -> {
	        ptf6n1Model.decreaseValue();
	    });
	    
	    confirmButton.setOnAction(e -> {
	    	//ptf6n1Model.setKeyState("1", null, null, null);
	        ptf6n1Model.keyMessage("1000", "18", "01");
	    });
	    
	    
	    displaytxt.textProperty().addListener(new ChangeListener<String>() {
	        @Override
	        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
	            String testText = newValue != null && newValue.length() >= 6 ? newValue : "000000";  // We test for 6 digits
	            double oldFontSize = displaytxt.getFont().getSize();
	            double newFontSize = oldFontSize;
	            
	            Text text = new Text(testText);
	            text.setFont(Font.font(displaytxt.getFont().getName(), oldFontSize));
	            double width = text.getLayoutBounds().getWidth();

	            while (width > displaytxt.getWidth() && newFontSize > 0) {
	                newFontSize -= 0.5;
	                text.setFont(Font.font(displaytxt.getFont().getName(), newFontSize));
	                width = text.getLayoutBounds().getWidth();
	            }

	            if (newFontSize != oldFontSize) {
	            	displaytxt.setFont(new Font(displaytxt.getFont().getName(), newFontSize));
	            }
	        }
	    });
	    
	}
	
	public void setPTF6N1Model(PTF6N1 ptf6n1Model) {
		this.ptf6n1Model = ptf6n1Model;
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setCatcher(Catcher catcher) {
		// TODO Auto-generated method stub
		this.catcher = catcher;
	}

	
	
	
	
	
}
