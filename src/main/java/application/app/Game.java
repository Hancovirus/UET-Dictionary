package application.app;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public  abstract class Game extends ControllerMain {
    protected GameOR game = new GameOR();
    protected AudioClip timerSound;
    protected String answer;
    @FXML
    protected Pane gameOver;
    abstract void initQuestion();
    abstract void resetGame();
    abstract void makeQuiz();
    abstract void  GameOver();
    abstract void handleOptionClick(ActionEvent event);
    protected void switchToGameUI() {
        try {
            switchScene(new ActionEvent(), "GameUI.fxml");
        } catch (IOException e) {
            e.printStackTrace(); // Xử lý lỗi nếu có
        }
    }
    abstract void handlePlayAgainButtonClick(ActionEvent event);
    abstract void handleExitButtonClick(ActionEvent event);

}
