package application.app;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Game2Controller extends Game implements Initializable {

    public static int numguess = 10;

    @FXML
    private Label Word;
    @FXML
    private Label Guess;
    @FXML
    private Label ans;
    @FXML
    private Label guessIt;

    private String currentString;
    public static boolean isTimerSoundPlaying = false;

    @Override
    public void initQuestion() {
        game.readAnswersFromFile("src/main/application/app/resourcesques2.txt");
    }

    @Override
    protected void makeQuiz() {

        int rand = game.getRandom(100);
        this.answer = game.getAnswer().get(rand);

        Word.setText(makeString(this.answer));
        currentString = makeString(this.answer);
    }

    public void initialize(URL arg0, ResourceBundle arg1) {

        // Tiếp tục với mã khác trong initialize
        initQuestion();
        timerSound = new AudioClip(getClass().getResource("/application/app/music.mp3").toString());
        timerSound.setVolume(0.1);
        timerSound.play();
        gameOver.setVisible(false);
        makeQuiz();
    }

    @Override
    @FXML
    public void handleOptionClick(ActionEvent event) {
        if (numguess > 1) {
            Button clickedButton = (Button) event.getSource();
            System.out.println(answer);
            Character buttontext = clickedButton.getText().charAt(0);
            char[] sArray = currentString.toCharArray();
            if (answer.contains(String.valueOf(buttontext))) {
                for (int i = 0; i < answer.length(); i++) {
                    if (answer.charAt(i) == buttontext) {
                        sArray[i] = buttontext;
                    }
                }
                clickedButton.setStyle("-fx-background-color: #39FA09");
                currentString = new String(sArray);
                Word.setText(currentString);
            } else {
                clickedButton.setStyle("-fx-background-color: #F02D2A;");
                numguess--;
                Guess.setText(Integer.toString(numguess));

            }
            if (currentString.equals(answer)) {
                Word.setTextFill(Color.web("#39FA09"));
                guessIt.setText("You guess it right!");
                GameOver();

            }
        } else {
            guessIt.setText("You guess it wrong!");
            GameOver();
        }


    }

    @Override
    public void GameOver() {
        Platform.runLater(() -> {
            gameOver.setVisible(true);
            ans.setText(answer);
            numguess = 10;
        });
    }
    @Override
    @FXML
    public void handlePlayAgainButtonClick(ActionEvent event) {
        // Reload the FXML file to reset the entire scene
        resetGame();
    }
    @Override
    @FXML
    public void handleExitButtonClick(ActionEvent event) {
        try {
            timerSound.stop();
            switchToGameScene(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resetGame() {
        // Stop the timer sound when loading a new scene
        if (timerSound != null) {
            timerSound.stop();
            isTimerSoundPlaying = false;
        }

        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Game2.fxml"));
            Parent root = loader.load();

            // Get the current stage from any UI element in the current scene
            Stage currentStage = (Stage) Guess.getScene().getWindow();

            // Set the new scene in the current stage
            currentStage.setScene(new Scene(root));

            // Show the current stage with the new scene
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String makeString(String s) {
        int n = s.length();
        StringBuilder stringBuilder = new StringBuilder(n);
        while (stringBuilder.length() < n) {
            stringBuilder.append('-'); // Bạn có thể thay ' ' bằng ký tự mong muốn
        }
        String newstring = stringBuilder.toString();
        return newstring;
    }


}