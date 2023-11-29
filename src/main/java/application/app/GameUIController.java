package application.app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class GameUIController extends ControllerMain {

	@FXML
    public void switchToGame1Scene(ActionEvent event) throws IOException {
		super.switchScene(event,"Game1.fxml");
    }
	@FXML
	public void switchToGame2Scene(ActionEvent event) throws IOException {
		super.switchScene(event,"Game2.fxml");
	}
}
