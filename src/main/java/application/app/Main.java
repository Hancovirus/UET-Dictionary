package application.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Search.fxml"));
            Scene scene = new Scene(root);
            String css = this.getClass().getResource("application.css").toExternalForm();
            scene.getStylesheets().add(css);

            stage.setTitle("Dictionary Application");
            stage.initStyle(StageStyle.TRANSPARENT);

            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        DictionaryManagement.dictionaryinsertFromFile("dictionary.txt");
        launch(args);
    }
}
