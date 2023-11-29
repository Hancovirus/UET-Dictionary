package application.app;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ApiController extends ControllerMain {
	
@FXML
	
	private String in = "en";
	private String out = "vi";
	
	private int inX = 60;
	private int outX = 400;
	
	@FXML
	private TextArea TransIn, TransOut;
	
	@FXML
	private Label LangIn, LangOut;
	
	public void translateWord(ActionEvent e) throws IOException {
		String word = TransIn.getText();
		String Trans = d.dictionaryTrans(in,out,word);
		TransOut.setText(Trans);
	}

	public void swapTrans(ActionEvent e){
		String temp = in;
		in = out;
		out = temp;
		TransIn.setText("");
		TransOut.setText("");
		
		int sw = inX;
		inX = outX;
		outX = sw;
		LangIn.setLayoutX(inX);
		LangOut.setLayoutX(outX);
	}
}
