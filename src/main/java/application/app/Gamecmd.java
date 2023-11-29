package application.app;

import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.Scanner;

public class Gamecmd extends Game {

    private boolean exit = false;
    private String ques;
    private ArrayList<String> options;


    public static void main(String[] args) {
        Gamecmd gamecmd = new Gamecmd();
        gamecmd.run();
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    @Override
    void initQuestion() {
        game.readQuestionsFromFile("C:\\Users\\chaka\\IdeaProjects\\App\\src\\main\\resources\\application\\app\\ques.txt");
        game.readAnswersFromFile("C:\\Users\\chaka\\IdeaProjects\\App\\src\\main\\resources\\application\\app\\ans.txt");
        game.readOptionsFromFile("C:\\Users\\chaka\\IdeaProjects\\App\\src\\main\\resources\\application\\app\\option.txt");
    }

    @Override
    void resetGame() {
        makeQuiz();
    }

    @Override
    void makeQuiz() {
        int rand = game.getRandom(50);
        ques = game.getQuestion().get(rand);
        options = game.getOption().get(rand);
        this.answer = game.getAnswer().get(rand);
    }

    @Override
    void GameOver() {

    }

    @Override
    void handleOptionClick(ActionEvent event) {

    }


    @Override
    void handlePlayAgainButtonClick(ActionEvent event) {

    }

    @Override
    void handleExitButtonClick(ActionEvent event) {

    }

    public void run() {
        initQuestion();
        makeQuiz();

        Scanner input = new Scanner(System.in);
        boolean exit = false;
        do {
            System.out.println(ques);
            System.out.println("a) " + options.get(0));
            System.out.println("b) " + options.get(1));
            System.out.println("c) " + options.get(2));
            System.out.println("d) " + options.get(3));

            String userAnswer;
            do {
                System.out.print("Choose your answer (a, b, c, or d): ");
                userAnswer = input.nextLine().trim().toLowerCase(); // Convert to lowercase for case-insensitive comparison
            } while (!isValidAnswer(userAnswer));

            check(getOptionFromAnswer(userAnswer));

            // Ask if the user wants to continue
            System.out.print("Do you want to continue? (yes/no): ");
            String continueChoice = input.nextLine().trim().toLowerCase();

            if (!continueChoice.equals("yes")) {
                System.out.println("Goodbye!");
                exit = true;
                break;
            }
            else {
                resetGame();
            }



        } while (!exit);


    }


    private boolean isValidAnswer(String answer) {
        return answer.equals("a") || answer.equals("b") || answer.equals("c") || answer.equals("d");
    }

    private String getOptionFromAnswer(String answer) {
        switch (answer) {
            case "a":
                return options.get(0);
            case "b":
                return options.get(1);
            case "c":
                return options.get(2);
            case "d":
                return options.get(3);
            default:
                return "Invalid Option";
        }
    }

    public void check(String choice) {
        if (choice.equals(answer)) {
            System.out.println("Your right!");
        } else {
            System.out.println("Your wrong! The correct answer is: " + this.answer);
        }
    }
}
