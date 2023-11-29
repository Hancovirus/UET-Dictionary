package application.app;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static javafx.util.Duration.seconds;

public class Game1Controller extends Game implements Initializable   {
    private static final Integer STARTTIME = 10;

    @FXML
    protected Pane timer;

    @FXML
    protected Label turn;
    @FXML
    protected Button exit;
    protected IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME * 100);
    protected boolean act = false;
    protected Timeline timeline = new Timeline();

    @FXML
    private Button playAgain;
    @FXML
    private Label score;
    @FXML
    private Label lastScore;
    @FXML
    private Label HighScore;
    @FXML
    private Label question;
    @FXML
    private Button optionA;
    @FXML
    private Button optionB;
    @FXML
    private Button optionC;
    @FXML
    private Button optionD;
    @FXML
    private Button next;
    private boolean choose = false;
    private int currentscore = 0;
    private int currentturn = 3;
    private int highScore;

    @Override
   void initQuestion() {
        game.readQuestionsFromFile("src/main/resources/application/app/ques.txt");
        game.readAnswersFromFile("src/main/resources/application/app/ans.txt");
        game.readOptionsFromFile("src/main/resources/application/app/option.txt");
    }

    public void initialize(URL url, ResourceBundle resourceBundle) {
        String cssFile = "/application/app/application.css";
        String externalForm = getClass().getResource(cssFile).toExternalForm();
        next.setVisible(false);
        initQuestion();
        timer.getStylesheets().add(externalForm);
        timer.setId("game-timer");
        // Load the sound file
        timerSound = new AudioClip(getClass().getResource("/application/app/music.mp3").toString());
        timerSound.setVolume(0.1);
        timerSound.play();
        loadHighScoreFromFile("src/main/resources/application/app/highscore.txt");
        gameOver.setVisible(false);
        makeQuiz();
    }

    @Override
    void resetGame() {
        // Stop the old timer sound
        timerSound.stop();
        currentturn = 3; // Reset the number of turns
        currentscore = 0; // Reset the score
        turn.setText("Turn: " + String.valueOf(currentturn));
        score.setText("   Score :   " + String.valueOf(currentscore));
        gameOver.setVisible(false);

        // Play the new timer sound
        timerSound = new AudioClip(getClass().getResource("/application/app/music.mp3").toString());
        timerSound.setVolume(0.1);
        timerSound.play();

        makeQuiz(); // Start a new round
    }


    @Override
    void makeQuiz() {
        resetOptionButtons();
        choose = false;
        int rand = game.getRandom(50);
        question.setText(game.getQuestion().get(rand));
        ArrayList<String> options = game.getOption().get(rand);
        optionA.setText(options.get(0));
        optionB.setText(options.get(1));
        optionC.setText(options.get(2));
        optionD.setText(options.get(3));
        this.answer = game.getAnswer().get(rand);
        timerAct(1);

    }
    @Override
    void GameOver() {
        Platform.runLater(() -> {
            gameOver.setVisible(true);
            setHighScore(currentscore);
            lastScore.setText("Your Score :  " + String.valueOf(currentscore));
            HighScore.setText("High Score :  " + String.valueOf(highScore));

        });
    }


    public void resetOptionButtons() {
        optionA.getStyleClass().clear();
        optionB.getStyleClass().clear();
        optionC.getStyleClass().clear();
        optionD.getStyleClass().clear();

        optionA.getStyleClass().add("game-word-button");
        optionB.getStyleClass().add("game-word-button");
        optionC.getStyleClass().add("game-word-button");
        optionD.getStyleClass().add("game-word-button");
        next.setVisible(false);
    }


    public void handleOnClickPlay() {
        if (currentturn > 0) {
            makeQuiz();
        } else {
            GameOver();
        }
    }
    @Override
    @FXML
    void handleOptionClick(ActionEvent event) {
        Button clickedOption = (Button) event.getSource();
        String selectedAnswer = clickedOption.getText();
        if (choose == false) {
            choose = true;
            if (selectedAnswer.equals(answer)) {
                System.out.println("Correct!");
                clickedOption.getStyleClass().add("correct-answer");
                currentscore += 10;
                score.setText("   Score :   " + String.valueOf(currentscore));
            } else {
                System.out.println("Wrong!");
                clickedOption.getStyleClass().add("wrong-answer");
                checkRemainingOptions();
                currentturn--;
                turn.setText("Turn: " + String.valueOf(currentturn));
            }
            // Stop the timer when the answer is chosen
            timerAct(0);
            next.setVisible(true);
        }

    }

    @Override
    @FXML
    void handlePlayAgainButtonClick(ActionEvent event) {
        resetGame();
        gameOver.setVisible(false);
    }

    @Override
    @FXML
    void handleExitButtonClick(ActionEvent event) {
        try {
            switchToGameScene(event);
            timerSound.stop();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void checkRemainingOptions() {
        // Highlight the correct answer
        if (optionA.getText().equals(answer)) {
            optionA.getStyleClass().add("correct-answer");
        } else if (optionB.getText().equals(answer)) {
            optionB.getStyleClass().add("correct-answer");
        } else if (optionC.getText().equals(answer)) {
            optionC.getStyleClass().add("correct-answer");
        } else if (optionD.getText().equals(answer)) {
            optionD.getStyleClass().add("correct-answer");
        }
    }

    public void timerAct(int in) {
        if (in == 1) {
            ExecutorService threadpool = Executors.newCachedThreadPool();
            ListeningExecutorService service = MoreExecutors.listeningDecorator(threadpool);
            service.submit(
                    () -> {
                        act = true;
                        timeSeconds.set((STARTTIME) * 100);
                        timer.prefWidthProperty().bind(timeSeconds.multiply(700).divide(STARTTIME * 100));

                        timeline.getKeyFrames().add(new KeyFrame(seconds(STARTTIME), new KeyValue(timeSeconds, 0)));
                        timeline.playFromStart();

                        while (act) {
                            if (timer.getPrefWidth() == 0) {
                                Platform.runLater(() -> {
                                    checkRemainingOptions();
                                    currentturn--;
                                    turn.setText("Turn: " + String.valueOf(currentturn));
                                    next.setVisible(true);
                                });

                                break;
                            }

                            // Add a small delay to avoid excessive UI updates
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
        } else {
            act = false;
            timeline.stop();
        }
    }




    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int newHighScore) {
        if (newHighScore > highScore) {
            highScore = newHighScore;
            writeHighScoreToFile();
        }
    }

    public void loadHighScoreFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            if (line != null) {
                highScore = Integer.parseInt(line.trim());
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
    }

    public void writeHighScoreToFile() {
        String filePath = "src/main/resources/application/app/highscore.txt"; // Change the file path as needed

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(String.valueOf(highScore));
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
    }


}
