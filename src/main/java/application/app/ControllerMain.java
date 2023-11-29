package application.app;

import java.io.IOException;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class ControllerMain {
	@FXML
	protected Stage stage;
	protected Scene scene;
	protected Parent root;
	protected DictionaryManagement d = new DictionaryManagement();
	
	@FXML
	protected void switchToSearchScene(ActionEvent e) throws IOException {
		switchScene(e, "Search.fxml");
	}
	
	@FXML
	protected void switchToAPIScene(ActionEvent e) throws IOException {
		switchScene(e, "API.fxml");
	}
	
	@FXML
	protected void switchToAddScene(ActionEvent e) throws IOException {
		switchScene(e, "Add.fxml");
	}
	
	@FXML
	protected void switchToGameScene(ActionEvent e) throws IOException {
		switchScene(e, "GameUI.fxml");
	}
	
	protected void switchScene(ActionEvent e, String path) throws IOException {
		root = FXMLLoader.load(getClass().getResource(path));
		stage = (Stage)((Node)e.getSource()).getScene().getWindow();
		scene = new Scene(root);
		String css = this.getClass().getResource("application.css").toExternalForm();
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
	protected void Exit(ActionEvent e) {

		Alert.AlertType type = Alert.AlertType.CONFIRMATION;
		Alert alert = new Alert(type, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		
		alert.getDialogPane().setHeaderText("EXIT");
		alert.getDialogPane().setContentText("You sure want to EXIT?");
		alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toString());
		alert.getDialogPane().getStyleClass().add("dialog");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Platform.exit();
	        System.exit(0);
	        return;
		}
		else if (result.get() == ButtonType.CANCEL) {
			return;
		}
	}
}
