package ViewModel;

import Model.Catcher;
import Model.PTF4N4;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

public class PTF4N4Controller {

	@FXML
	private Label PTF6N1;
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
	
	private PTF4N4 ptf4n4Module;
	private Stage stage;
	private Catcher controller;
	
	
	public void initalize() {
		stage.setOnCloseRequest(event -> {
			controller.removeOpened(stage);
		});
	}


	public void setPTF4N4Model(PTF4N4 moduleModel) {
		// TODO Auto-generated method stub
		this.ptf4n4Module = moduleModel;
	}


	public void setStage(Stage newStage) {
		// TODO Auto-generated method stub
		this.stage = newStage;
	}


	public void setCatcher(Catcher controller) {
		// TODO Auto-generated method stub
		this.controller = controller;
	}
}
