package ViewModel;

import Model.Catcher;

import Model.MainViewModel;
import Model.SerializerClass;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;

public class MainViewController {
	@FXML
	private Label mainLabel;
	@FXML
	private TextField hostIpText;
	@FXML
	private TextField controllerIpText;
	@FXML
	private TextField subBoxPortText;
	@FXML
	private TextField hostPortText;
	@FXML
	private Button manageModules;
	@FXML
	private TextField subBoxNameText;
	@FXML
	private Button startButton;
	@FXML
	private Button closeConnectionBtn;
	@FXML
	private Button saveStateBtn;
	
	SerializerClass serializer = new SerializerClass();


	private MainViewController mainInstance;
	private MainViewModel viewModel;
	private Catcher controller;
	private Boolean deserialized = false;




	public void setCatcher(Catcher controller) {
		this.controller = controller;
		controller.setTypeAddressMap();
	}

	@FXML
	public void initialize() {
		mainLabel.textProperty().bind(viewModel.getMainLabelText());
		hostIpText.textProperty().bindBidirectional(viewModel.getHostIPText());
		controllerIpText.textProperty().bindBidirectional(viewModel.getControllerIPText());
		subBoxPortText.textProperty().bindBidirectional(viewModel.getSubBoxPortText());
		hostPortText.textProperty().bindBidirectional(viewModel.getHostPortText());
		subBoxNameText.textProperty().bindBidirectional(viewModel.getSubBoxText());
		
				
		if (viewModel.startIsDisabled()) {
			startButton.setDisable(true);
		}
		else {
			startButton.setDisable(false);
		}
		
		if (viewModel.closeIsDisabled()) {
			closeConnectionBtn.setDisable(true);		
		}
		else {
			closeConnectionBtn.setDisable(false);
		}
		
		subBoxPortText.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue.matches("\\d*")) {
				subBoxPortText.setText(newValue.replaceAll("[^\\d]", ""));
				viewModel.updateMainLabelText("Attention: Only numbers accepted!");
			}
		});
		
		List<TextField> allTextFields = Arrays.asList(subBoxNameText, hostIpText, controllerIpText, hostPortText);

		for (TextField textField : allTextFields) {
		    textField.textProperty().addListener((observable, oldValue, newValue) -> {
		    });
		}

		manageModules.setOnAction(event -> openNewView(event));

		subBoxPortText.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		        if (!newValue) { 
		            if (isInteger(subBoxPortText.getText())) {
		            	
		                viewModel.updateMainLabelText("Port is " + subBoxPortText.getText() + "!");
		                viewModel.isSetSubBoxPort = true;
		                subBoxPortText.setStyle("-fx-border-color: none;");
		            } else {
		            	viewModel.isSetSubBoxPort = false;
		                viewModel.updateMainLabelText("Error: Please enter first a valid port!");
		                subBoxPortText.setStyle("-fx-border-color: red;");
		            }
		            
		        }
		    }
		});

		hostPortText.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
		        if (!newPropertyValue) {  // Wenn der Fokus verloren geht
		            if (isInteger(hostPortText.getText())) {
		                viewModel.updateMainLabelText("Port is " + hostPortText.getText() + "!");
		                viewModel.isSetHostPort = true;
		               
		                hostPortText.setStyle("-fx-border-color: none;"); // Entfernt die rote Umrandung, wenn die Eingabe korrekt ist
		            } else {
		            	viewModel.isSetHostPort = false;
		                viewModel.updateMainLabelText("Error: Please enter first a valid port!");
		                hostPortText.setStyle("-fx-border-color: red;"); // Setzt die Umrandung auf rot, wenn die Eingabe falsch ist
		            }

		        }
		    }
		});

		hostIpText.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
		        if (!newPropertyValue) {  // Wenn der Fokus verloren geht
		            if (!isValidIP(hostIpText.getText())) {
		                viewModel.updateMainLabelText("Error: Not a valid Host IP address!");
		                hostIpText.setText("");
		                hostIpText.setStyle("-fx-border-color: red;"); // Setzt die Umrandung auf rot, wenn die Eingabe falsch ist
		                viewModel.isSetHostIP = false;
		            } else {
		                viewModel.updateMainLabelText("Host IP is " + hostIpText.getText() + "!");
		                hostIpText.setStyle("-fx-border-color: none;"); // Entfernt die rote Umrandung, wenn die Eingabe korrekt ist
		                viewModel.isSetHostIP = true;
		               
		            }

		        }
		    }
		});


		controllerIpText.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
		        if (!newPropertyValue) {  // Wenn der Fokus verloren geht
		            if (!isValidIP(controllerIpText.getText())) {
		                viewModel.updateMainLabelText("Error: Not a valid Controller IP address!");
		                controllerIpText.setText("");
		                controllerIpText.setStyle("-fx-border-color: red;"); // Setzt die Umrandung auf rot, wenn die Eingabe falsch ist
		                viewModel.isSetControllerIP = false;
		            } else {
		                viewModel.updateMainLabelText("Controller IP is " + controllerIpText.getText() + "!");
		                controllerIpText.setStyle("-fx-border-color: none;"); // Entfernt die rote Umrandung, wenn die Eingabe korrekt ist
		                viewModel.isSetControllerIP = true;
		               
		            }

		        }
		    }
		});


		subBoxNameText.focusedProperty().addListener(new ChangeListener<Boolean>() {
		    @Override
		    public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
		        if (!newPropertyValue) {  // Wenn der Fokus verloren geht
		            if (subBoxNameText.getText() == null || subBoxNameText.getText().trim().isEmpty() || subBoxNameText.getText().length() != 4) {
		                viewModel.updateMainLabelText("Error: SubBoxName cannot be empty! Enter only 4 characters!");
		                subBoxNameText.setText("");
		                subBoxNameText.setStyle("-fx-border-color: red;"); // Setzt die Umrandung auf rot, wenn die Eingabe falsch ist
		                viewModel.isSetSubBoxName = false;
		            } else {
		                viewModel.updateMainLabelText("SubBoxName is " + subBoxNameText.getText() + "!");
		                subBoxNameText.setStyle("-fx-border-color: none;"); // Entfernt die rote Umrandung, wenn die Eingabe korrekt ist
		                viewModel.isSetSubBoxName = true;
		                
		            }

		        }
		    }
		});
		hostIpText.setOnMouseClicked(event -> {
			if (viewModel.isFirstClickTextField) {
				viewModel.clearTextFieldText();
				viewModel.isFirstClickTextField = false;
			}
		});

		subBoxPortText.setOnMouseClicked(event -> {
			if (viewModel.isFirstClickSubBoxPortText) {
				viewModel.clearSubBoxPortText();
				viewModel.isFirstClickSubBoxPortText = false;
			}
		});
		
		hostPortText.setOnMouseClicked(event -> {
			if (viewModel.isFirstClickHostPortText) {
				viewModel.clearHostPortText();
				viewModel.isFirstClickHostPortText = false;
			}
		});

		subBoxNameText.setOnMouseClicked(event -> {
			if (viewModel.isFirstClickSubBoxText) {
				viewModel.clearSubBoxNameText();
				viewModel.isFirstClickSubBoxText = false;
			}
		});

		startButton.setOnMouseClicked(event -> {
			
			Boolean bool = false;
		    boolean isAnyFieldInvalid = allTextFields.stream()
		        .anyMatch(field -> "red".equals(field.getStyle()) || field.getText().trim().isEmpty());
		    
		    if(viewModel.getDeserializedBool() 
		    	    && !viewModel.getControllerIPText().isEmpty().get()
		    	    && !viewModel.getHostIPText().isEmpty().get() 
		    	    && !viewModel.getHostPortText().isEmpty().get()
		    	    && !viewModel.getSubBoxPortText().isEmpty().get()
		    	    && !viewModel.getSubBoxText().isEmpty().get()) {
		    	bool = true;
		    	}

		    if ((!isAnyFieldInvalid && viewModel.isSetSubBoxName && viewModel.isSetControllerIP && viewModel.isSetHostIP && viewModel.isSetHostPort && viewModel.isSetSubBoxPort && !controller.getMap().isEmpty()) || bool) {
		        viewModel.updateMainLabelText("Ready to receive data!");
		        viewModel.startProcess(controller);
		        if(viewModel.connected()) {
		        	viewModel.startIsDisabled(true);
		        	viewModel.closeIsDisabled(false);
		            startButton.setDisable(true);
		            closeConnectionBtn.setDisable(false);
		        }			
		    } else {
		        if (controller.getMap().isEmpty()) {
		            viewModel.updateMainLabelText("Attention: Please add modules!");
		        }
		        else {
		            viewModel.updateMainLabelText("Attention: Please check all fields!");
		        }    	
		    }
		});

		
		closeConnectionBtn.setOnMouseClicked(event -> {
			
			startButton.setDisable(false);
			closeConnectionBtn.setDisable(true);
			viewModel.closeIsDisabled(true);
			viewModel.startIsDisabled(false);
			
			viewModel.closeConnection();
			viewModel.updateMainLabelText("Attention: Connection closed!");
		});
		
		saveStateBtn.setOnAction(event -> {
			
			serializer.serializeMainViewModel(viewModel);
			serializer.serializeModules(controller);
			viewModel.updateMainLabelText("All saved!");
		});
		
	
	}


	private boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private boolean isValidIP(String ip) {
		if (ip == null) {
			return false;
		}

		String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
				+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}

	@FXML
	private void openNewView(ActionEvent event) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/View/PTFManagerView.fxml"));

			Scene newScene = new Scene(loader.load(), 500, 400);

			PTFManagerController test = (PTFManagerController) loader.getController();
			test.setCatcher(this.controller);
			test.setMainViewModel(this.viewModel);

			Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			currentStage.setTitle("Module Manager");
			currentStage.setScene(newScene);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Catcher getController() {
		// TODO Auto-generated method stub
		return this.controller;
	}

	public void setMain(MainViewController main) {
		// TODO Auto-generated method stub
		this.mainInstance = main;
		setMainViewModel(main.getViewModel());
	}

	public void setMainViewModel(MainViewModel mainViewModel) {
		// TODO Auto-generated method stub
		this.viewModel = mainViewModel;
	}

	public MainViewModel getViewModel() {
		// TODO Auto-generated method stub
		return this.viewModel;
	}

	public void setDeserializedBool(Boolean deserialized) {
		// TODO Auto-generated method stub
		this.deserialized  = deserialized;
		viewModel.setDeserializedBool(deserialized);
	}

}