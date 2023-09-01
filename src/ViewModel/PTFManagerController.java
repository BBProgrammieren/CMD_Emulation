package ViewModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import Model.Catcher;
import Model.MainViewModel;
import Model.PTF4N4;
import Model.PTF6N1;
import Model.SerializerClass;

public class PTFManagerController {
	@FXML
	private Label mainLabel;
	@FXML
	private TextField textField;
	@FXML
	private Button deleteButton;
	@FXML
	private Button addPTF6N1btn;
	@FXML
	private Button addPTF4N4btn;
	@FXML
	private Label label;
	@FXML
	private ListView<String> list;
	@FXML
	private Button returnButton;

	private SerializerClass serializer = new SerializerClass();
	private Stage newStage = new Stage();
	private Catcher controller;
	private final String ptf6n1 = "PTF6N1";
	private final String ptf4n4 = "PTF4N4";
	private MainViewModel mainViewModel;
	public void initialize() {

		addPTF6N1btn.setOnAction(event -> {
			String addr = textField.getText();
			String str = controller.addNewModule(addr, ptf6n1);
			if (str != null) {
				label.setText(str);
			} else {
//				this.ptf6n1Model = new PTF6N1(addr);
//				controller.addTypeAddressMap(ptf6n1Model, addr);
				label.setText("Successfuly PTF6N1 module added!");
				loadList();
				serializer.serializeModules(controller);
			}

			textField.clear();
		});

		addPTF4N4btn.setOnAction(event -> {
			textField.getText();
			String str = controller.addNewModule(textField.getText(), ptf4n4);
			if (str != null) {
				label.setText(str);
			} else {
//				this.ptf4n4Model = new PTF4N4(addr);
//				controller.addTypeAddressMap(ptf4n4Model, addr);
				label.setText("Successfuly PTF4N4 module added!");
				loadList();
				serializer.serializeModules(controller);
			}
			textField.clear();
		});

		deleteButton.setOnAction(event -> {
			String selectedItem = list.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				// Get the key from the selected item
				String selectedKey = selectedItem.split(":")[0];
				controller.removeModule(selectedKey);
				loadList();
				label.setText("'" + selectedItem + "' successfully deleted!");

				Optional<Stage> foundStage = controller.getOpened().stream()
						.filter(stage -> selectedKey.equals(stage.getTitle())).findFirst();

				if (foundStage.isPresent()) {
					Stage stage = foundStage.get();
					stage.close();
				} else {
					// Es wurde keine Stage mit dem gegebenen Titel gefunden.
				}
				serializer.serializeModules(controller);
			} else {
				label.setText("No selected item in list to delete!");
			}
		});

		newStage.setOnCloseRequest(event -> {
			controller.removeOpened(newStage);
		});

		returnButton.setOnAction(event -> {
			
			serializer.serializeModules(controller);

			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/View/Main.fxml"));
				
				MainViewController viewController = new MainViewController();
				viewController.setMainViewModel(this.mainViewModel);
				viewController.setCatcher(this.controller);

				loader.setController(viewController);
				
				Scene newScene = new Scene(loader.load(), 500, 400);
				
				Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				currentStage.setScene(newScene);
				
				currentStage.show();

			} catch (IOException e) {
				e.printStackTrace();
			}
		});

		list.setOnMouseClicked(event -> {
			if (event.getClickCount() == 2 && list.getSelectionModel().getSelectedItem() != null) {
				FXMLLoader loader = new FXMLLoader();
				Scene newScene;
				String selectedItem = list.getSelectionModel().getSelectedItem();
				String selectedKey = selectedItem.split(":")[0];
				String type = controller.getType(selectedKey);

				Optional<Stage> openedStage = controller.getOpened().stream()
						.filter(stage -> selectedKey.equals(stage.getTitle())).findFirst();

				if (!openedStage.isPresent()) {
					Stage newStage = new Stage();

					label.setText("Opening...");

					if (type.equals(ptf6n1)) {

						try {
							
							loader.setLocation(getClass().getResource("/View/PTF6N1View.fxml"));
							PTF6N1Controller test = new PTF6N1Controller();
							test.setPTF6N1Model((PTF6N1) controller.getModuleModel(selectedKey));
							test.setStage(newStage);
							test.setCatcher(controller);
							loader.setController(test);
							newScene = new Scene(loader.load());					
							newStage.setTitle(selectedKey);
							newStage.setScene(newScene);
							controller.addOpened(newStage, selectedKey);

							newStage.show();


						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					if (type.equals(ptf4n4)) {

						try {

							loader.setLocation(getClass().getResource("/View/PTF4N4View.fxml"));
							PTF4N4Controller test = new PTF4N4Controller();
							test.setPTF4N4Model((PTF4N4) controller.getModuleModel(selectedKey));
							test.setStage(newStage);
							test.setCatcher(controller);
							loader.setController(test);
							newScene = new Scene(loader.load());					
							newStage.setTitle(selectedKey);
							newStage.setScene(newScene);
							controller.addOpened(newStage, selectedKey);

							newStage.show();

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else {
					label.setText("Module already opened!");
				}
			}
		});
	}

	public void loadList() {
		HashMap<String, String> hashMap = this.controller.getMap();
		ObservableList<String> items = FXCollections.observableArrayList();

		if (hashMap != null && hashMap.isEmpty() == false) {

			for (Map.Entry<String, String> entry : hashMap.entrySet()) {
				String keyAndValue = entry.getKey() + ": " + entry.getValue();
				items.add(keyAndValue);
			}
			list.setItems(items);
		}
		else {
			hashMap = new HashMap<String, String>();
			controller.setModuleMap(hashMap);
		}
		controller.setTypeAddressMap();
	}

	public void setCatcher(Catcher controller2) {
		this.controller = controller2;
		loadList();
	}


	public void setMainViewModel(MainViewModel viewModel) {
	
		this.mainViewModel = viewModel;
	}
}
