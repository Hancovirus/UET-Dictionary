package application.app;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AddController extends ControllerMain {
	
	@FXML
	private TextField ExplainFill, AddFill;
	
	@FXML
	private AnchorPane AddAnchorPane;
	
	@FXML 
	private DialogPane dialog;
	
	public void addOrUpdateWord(ActionEvent e){
		String AddWord = AddFill.getText();
		String ExplainWord = ExplainFill.getText();
		
		Stage stage = (Stage) AddAnchorPane.getScene().getWindow();
		
		if(AddWord == "" && ExplainWord == "") {
			Alert.AlertType type = Alert.AlertType.ERROR;
			Alert alert = new Alert(type, "");

			alert.getDialogPane().setHeaderText("MISSING COMPONENT");
			alert.getDialogPane().setContentText("AddWord or ExplainWord is missing.");
			alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toString());
			alert.getDialogPane().getStyleClass().add("dialog");

			alert.showAndWait();
			return;
		}
		
		Alert.AlertType type = Alert.AlertType.CONFIRMATION;
		Alert alert = new Alert(type, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		
		if(AddWord != "" && !d.dictionaryLookup(AddWord).equals("NO Word")) {
			alert.getDialogPane().setHeaderText("UPDATE WORD");
			alert.getDialogPane().setContentText("This word already added, you sure want to update?");
			alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toString());
			alert.getDialogPane().getStyleClass().add("dialog");
			
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				d.removeFromDictionary(AddWord);
				DictionaryManagement.add_word_to_dictionary(AddWord, ExplainWord);
			}
			else if (result.get() == ButtonType.CANCEL) {
				return;
			}
		} else if (AddWord != "" && d.dictionaryLookup(AddWord).equals("NO Word")) {
			alert.getDialogPane().setHeaderText("ADD WORD");
			alert.getDialogPane().setContentText("You sure want to add?");
			alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toString());
			alert.getDialogPane().getStyleClass().add("dialog");
			
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				DictionaryManagement.add_word_to_dictionary(AddWord, ExplainWord);
			}
			else if (result.get() == ButtonType.CANCEL) {
				return;
			}
		}
		d.dictionaryExportToFile("dictionary.txt");
		AddFill.setText("");
		ExplainFill.setText("");
		return;
	}
}
