package application.app;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.voicerss.tts.AudioCodec;
import com.voicerss.tts.AudioFormat;
import com.voicerss.tts.Languages;
import com.voicerss.tts.VoiceParameters;
import com.voicerss.tts.VoiceProvider;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class DictionaryManagement {
    private static ArrayList<Word> Dictionary = new ArrayList<>();
    ObservableList<String> TargetDictionary = FXCollections.observableArrayList();
    int number_of_words = 0;
    private static Trie trie = new Trie();
    public static final int wordsinlist = 10;

    public static void sortWordList() {
        Collections.sort(Dictionary, new Comparator<Word>() {
            @Override
            public int compare(Word word1, Word word2) {
                // So sánh theo thuộc tính target
                return word1.getWord_target().compareTo(word2.getWord_target());
            }
        });
    }

    public static void add_word_to_dictionary(String Word_target, String Word_explain) {
        Word_target = fixing_input(Word_target);
        Word_explain = fixing_input(Word_explain);
        Word New_word = new Word(Word_target, Word_explain);
        Dictionary.add(New_word);
        trie.insert(fixing_input(Word_target));
    }

    public static String fixing_input(String input) {
        if (input.equals("")) {
            return "";
        }
        input = input.toLowerCase();
        input = input.trim();
        char firstChar = input.charAt(0);
        char upperFirstChar = Character.toUpperCase(firstChar);
        return upperFirstChar + input.substring(1);

    }

    public void insertFromCommandline() {
        int n;
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("How many words you want to insert?");
            n = input.nextInt();
            number_of_words += n;
            if (n <= 0) {
                System.out.println("Invalid input. Please enter valid number.");
            }

        } while (n <= 0);
        input.nextLine();
        for (int i = 0; i < n; i++) {
            System.out.print("Enter Word " + (i + 1) + " target-explain: ");
            String inputLine = input.next();

            String[] parts = inputLine.split("-", 2);

            if (parts.length >= 2) {
                String Word_target = fixing_input(parts[0]);
                String Word_explain = fixing_input(parts[1]);
                //Word New_word = new Word(Word_target, Word_explain);
                add_word_to_dictionary(Word_target, Word_explain);
            } else {
                System.out.println("Invalid input. Please enter both target and explain separated by a hyphen.");
                i--;
            }
        }
    }

    public int getPage() {
        if (TargetDictionary == null) {
            return 1;
        }
        int page = TargetDictionary.size();
        page = (page / wordsinlist) + 1;
        return page;
    }

    public void showAllWords() {
        Scanner input = new Scanner(System.in);

        System.out.printf("%-7s | %-20s | %-30s%n", "N0", "English", "Vietnamese");
        boolean exit = false;
        int page = 0;

        sortWordList();

        int get;

        while (!exit) {
            for (int i=0;i<wordsinlist;i++) {
                if (page * wordsinlist + i == TargetDictionary.size()) {
                    break;
                }
                System.out.printf("%-7d | %-20s | %-30s%n", i + 1, TargetDictionary.get(page * wordsinlist + i)
                        , dictionaryLookup(TargetDictionary.get(page * wordsinlist + i)));
            }
            System.out.println("Page " + (page + 1) + " out of " + getPage());
            System.out.println("[0] Exit "
                    + "[1] Down page "
                    + "[2] Up page\n"
                    + "Your action:");
            get = input.nextInt();
            if (get >= 0 && get <=2) {
                switch (get) {
                    case 0:
                        exit = true;
                        break;
                    case 1:
                        if (page == 0) {
                            break;
                        }
                        page--;
                        break;
                    case 2:
                        if (page >= getPage() - 1) {
                            break;
                        }
                        page ++;
                        break;
                }
            } else {
                System.out.println("Invalid input, please select from 0-2\n" + "Your action:");
            }
        }
    }

    public void removeFromDictionary(String remove_word) {
        remove_word = fixing_input(remove_word);
        Iterator<Word> iterator = Dictionary.iterator();
        while (iterator.hasNext()) {
            Word word = iterator.next();
            if (word.getWord_target().equals(remove_word)) {
                iterator.remove();
                trie.delete(remove_word);
                System.out.println("Delete " + remove_word + " succesfully");
                return;
            }
        }
        System.out.println("There is no word");

    }
    public void dictionaryExportToFile()
    {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("dictionary.txt"));
            for (Word word : Dictionary) {
                writer.write(word.getWord_target() + "\t" + word.getWord_explain() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void dictionaryinsertFromFile(String path)
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            while ((line = reader.readLine()) != null) {
                String inputLine = line;
                String[] parts = inputLine.split("\t",2);
                if (parts.length >= 2) {
                    String Word_target = fixing_input(parts[0]);
                    String Word_explain = fixing_input(parts[1]);
                    add_word_to_dictionary(Word_target, Word_explain);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void dictionaryinsertFromFile()
    {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("dictionary.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String inputLine = line;
                String[] parts = inputLine.split("\t",2);
                if (parts.length >= 2) {
                    String Word_target = fixing_input(parts[0]);
                    String Word_explain = fixing_input(parts[1]);
                    add_word_to_dictionary(Word_target, Word_explain);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void dictionaryExportToFile(String path)
    {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            for (Word word : Dictionary) {
                writer.write(word.getWord_target() + "\t" + word.getWord_explain() + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void dictionaryUpdate(String update_word)
    {
        Scanner input1 = new Scanner(System.in);
        Iterator<Word> iterator = Dictionary.iterator();
        while (iterator.hasNext()) {
            Word word = iterator.next();
            if (word.getWord_target().equals(fixing_input(update_word))) {
                System.out.println("Input new explain");
                String inputLine = input1.next();
                String Word_explain = fixing_input(inputLine);
                word.setWord_explain(Word_explain);
                System.out.println("Update successfully");
                return;
            }
        }
        System.out.println("No Word");
    }

    public ObservableList<String> getdictionary(String searchWord) {
        TargetDictionary = trie.autoComplete(fixing_input(searchWord));
        return TargetDictionary;
    }

    public String dictionaryLookup(String look_up_word) {
        Iterator<Word> iterator = Dictionary.iterator();
        while (iterator.hasNext()) {
            Word word = iterator.next();
            if (word.getWord_target().equals(fixing_input(look_up_word))) {
                return word.getWord_explain();
            }
        }
        return "NO Word";
    }

    private static String translate(String langFrom, String langTo, String text) throws IOException {
        StringBuilder response = new StringBuilder();
        ExecutorService threadpool = Executors.newCachedThreadPool();
        ListeningExecutorService service = MoreExecutors.listeningDecorator(threadpool);
        ListenableFuture<String> future = service.submit(() -> {
            try {
                String urlStr = "https://script.google.com/macros/s/AKfycbz5XOgKRoSF8p45rgNUyMpvEIyiY7wTrLJypLVsuQDm7PbCap-y9yu8N0tu_EVeNOhU/exec" +
                        "?q=" + URLEncoder.encode(text, "UTF-8") +
                        "&target=" + langTo +
                        "&source=" + langFrom;
                URL url = new URL(urlStr);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestProperty("User-Agent", "Mozilla/5.0");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                return response.toString();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void dictionarySpeech(String speech) {
        ExecutorService threadpool = Executors.newCachedThreadPool();
        ListeningExecutorService service = MoreExecutors.listeningDecorator(threadpool);
        service.submit(
                () -> {
                    VoiceProvider tts = new VoiceProvider("78c77fb09be74d449cf7e451083b3621");
                    VoiceParameters params =
                            new VoiceParameters(speech, Languages.English_UnitedStates);
                    params.setCodec(AudioCodec.WAV);
                    params.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
                    params.setBase64(false);
                    params.setSSML(false);
                    params.setRate(0);
                    try {
                        byte[] voice = tts.speech(params);
                        FileOutputStream fos = new FileOutputStream("voice.mp3");
                        fos.write(voice, 0, voice.length);
                        fos.flush();
                        fos.close();
                    } catch (Exception e) {
                        System.out.println("Make mp3 file Error");
                    }
                    try {
                        AudioInputStream audioStream =
                                AudioSystem.getAudioInputStream(new File("voice.mp3").getAbsoluteFile());
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioStream);
                        clip.start();
                    } catch (Exception e) {
                        System.out.println("Use mp3 file Error");
                    }
                });
    }

    public String dictionaryTrans(String In, String Out, String text) throws IOException {
        return translate(In, Out, text);
    }

    public void clearDictionary() {
        Dictionary.clear();
    }
}
//--module-path "D:\Java\JFX\openjfx-21_windows-x64_bin-sdk\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml
