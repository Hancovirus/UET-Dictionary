package application.app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class GameOR {

    protected ArrayList<String> question = new ArrayList<>();
    protected ArrayList<String> answer = new ArrayList<>();
    protected ArrayList<ArrayList<String>> option = new ArrayList<>();

    // Hàm đọc đáp án từ tệp ans.txt và đưa vào answer
    public void readAnswersFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                answer.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Hàm đọc câu hỏi từ tệp ques.txt và đưa vào question
    public void readQuestionsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Tìm vị trí của dấu ngoặc kép
                int startIndex = line.indexOf("\"");
                int endIndex = line.lastIndexOf("\"");

                // Kiểm tra xem có dấu ngoặc kép hay không và thêm câu vào danh sách
                if (startIndex != -1 && endIndex != -1 && startIndex != endIndex) {
                    String extractedQuestion = line.substring(startIndex + 1, endIndex).trim();
                    question.add(extractedQuestion);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readOptionsFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Tách các tùy chọn bằng dấu /
                String[] options = line.split("/");

                // Chuyển mảng options thành ArrayList và thêm vào danh sách option
                ArrayList<String> optionList = new ArrayList<>();
                for (String opt : options) {
                    optionList.add(opt.trim());
                }
                option.add(optionList);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }






    public int getRandom(int num) {
        Random random = new Random();
        return random.nextInt(num); // Trả về một số nguyên ngẫu nhiên từ 0 đến NUMBER_OF_QUESTIONS - 1
    }


    public ArrayList<ArrayList<String>> getOption() {
        return option;
    }

    public ArrayList<String> getQuestion() {
        return question;
    }

    public ArrayList<String> getAnswer() {
        return answer;
    }
}
