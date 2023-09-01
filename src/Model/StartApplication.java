package Model;

import java.io.File;

import ViewModel.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Lade die FXML Datei für die Hauptansicht
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Main.fxml"));

        SerializerClass serializer = new SerializerClass();
        MainViewModel mainViewModel = new MainViewModel();
        MainViewController test = new MainViewController();
        Catcher controller = new Catcher();
        Boolean deserialized = false; 
        
        controller = serializer.deserializeModules();

        if (controller == null) {
            controller = new Catcher();
        }
      
        //Fehlermeldung beim aller ersten start der Applikation
        File file = new File("savedState.ser");
        if(file.exists() && !file.isDirectory()) { 
            try {
                serializer.deserializeMainViewModel(mainViewModel);
                deserialized = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        controller.setMainModel(mainViewModel);
  
        test.setMainViewModel(mainViewModel);
        test.setCatcher(controller);
        test.setDeserializedBool(deserialized);

        loader.setController(test);
        Parent root = loader.load();       

        // Erstelle die Hauptansicht Scene und setze sie in die Bühne (Stage)
        Scene scene = new Scene(root, 500, 400);
        stage.setScene(scene);
        stage.setTitle("Main Menu");
        stage.show();

     
    }

    public static void main(String[] args) {
        launch(args);
    }
}