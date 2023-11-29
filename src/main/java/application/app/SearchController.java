package application.app;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class SearchController extends ControllerMain implements Initializable {

	@FXML
	private int page = 0;
	private String currentWord, currentExplainWord;
	private String searchWord = "";
	private ObservableList<String> arr = FXCollections.observableArrayList();
	
	@FXML
	private TextField searchField, SwitchPage;
	
	@FXML
	private ListView<String> SearchList;
	
	@FXML
	private Label wordLabel,pageLabel,explainField;
	
	@FXML
	public void DownList(ActionEvent e){
		if (page == d.getPage() - 1) {
			return;
		}
		page++;
		UpdateList();
	}
	
	@FXML
	public void UpList(ActionEvent e){
		if (page == 0) {
			return;
		}
		page--;
		UpdateList();
	}
	
	@FXML
	public void Delete(ActionEvent e){
		Alert.AlertType type = Alert.AlertType.CONFIRMATION;
		Alert alert = new Alert(type, "");
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.initOwner(stage);
		
		alert.getDialogPane().setHeaderText("DELETE WORD");
		alert.getDialogPane().setContentText("This word already added, you sure want to delete?");
		alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toString());
		alert.getDialogPane().getStyleClass().add("dialog");
		
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {	
			String targetWord = wordLabel.getText();
			d.removeFromDictionary(targetWord);
			d.dictionaryExportToFile("dictionary.txt");
			Search();
			UpdateList();
			wordLabel.setText("Word");
			explainField.setText("Explain");	
		}
		else if (result.get() == ButtonType.CANCEL) {
			return;
		}
	}
	
	@FXML
	private void Search() {
		searchWord = searchField.getText();
		if ((arr = d.getdictionary(searchWord)) == null) {
			return;
		}
		page = 0;
		UpdateList();
	}
	
	@FXML
	public void UpdateList(){	
		ObservableList<String> show = FXCollections.observableArrayList();
		for (int i = 0; i < DictionaryManagement.wordsinlist; i++) {
			if (page * DictionaryManagement.wordsinlist + i >= arr.size()) {
				break;
			}
			show.add(arr.get(page * DictionaryManagement.wordsinlist + i));
		}
		SearchList.getItems().clear();
		SearchList.getItems().addAll(show);
		pageLabel.setText("Page " + (page + 1) + " Out Of " + d.getPage());
	}
	
	@FXML
	public void TTS(){
		d.dictionarySpeech(wordLabel.getText());
	}
	
	@FXML
	public void Goto(){
		Alert.AlertType type = Alert.AlertType.ERROR;
		Alert alert = new Alert(type, "");
		alert.getDialogPane().setHeaderText("WRONG INPUT");
		alert.getDialogPane().getStylesheets().add(getClass().getResource("application.css").toString());
		alert.getDialogPane().getStyleClass().add("dialog");
		try {
			if (Integer.parseInt(SwitchPage.getText()) <= d.getPage() 
					&& Integer.parseInt(SwitchPage.getText()) > 0 ) {
				page = Integer.parseInt(SwitchPage.getText()) - 1;
				UpdateList();
				SwitchPage.setText("");
			} else {
				SwitchPage.setText("");
				alert.getDialogPane().setContentText("Number must be larger or equal to 1 and smaller or equal to " + d.getPage());
				alert.showAndWait();
				return;
			}
		} catch (NumberFormatException e) {
			SwitchPage.setText("");
			alert.getDialogPane().setContentText("Only accept number.");
			alert.showAndWait();
			return;
		}
	}	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Search();
		UpdateList();
		
		SearchList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if ((currentWord = SearchList.getSelectionModel().getSelectedItem()) != null) {
					currentExplainWord = d.dictionaryLookup(currentWord).replaceAll("\t", "\n");
					wordLabel.setText(currentWord);			
					explainField.setText(currentExplainWord);	
				}
			}
		});
		
		searchField.textProperty().addListener((observable, oldValue, newValue) -> {
			Search();
		});
	}
}
