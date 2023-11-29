package application.app;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
public class DictionaryCommandline {

	private static DictionaryManagement d = new DictionaryManagement();


	public static void dictionaryAdvanced() {
		try {
			Scanner sc = new Scanner(System.in);
			boolean exit = false;

			int get;
			while (!exit) {
				System.out.print("Welcome to My Application!\n"
						+ "[0] Exit\n"
						+ "[1] Add\n"
						+ "[2] Remove\n"
						+ "[3] Update\n"
						+ "[4] Display\n"
						+ "[5] Lookup\n"
						+ "[6] Search\n"
						+ "[7] Game\n"
						+ "[8] Import from file\n"
						+ "[9] Export to file\n"
						+ "Your action: ");
				get = sc.nextInt();
				if (get <= 9 && get >= 0) {
					switch(get) {
						case 0:
							System.out.println("Exit successfully");
							exit = true;
							break;
						case 1:
							d.insertFromCommandline();
							break;
						case 2:
							System.out.print("Input delete word: ");
							String deleteWord;
							deleteWord = sc.next();
							d.removeFromDictionary(deleteWord);
							break;
						case 3:
							System.out.print("Input update word: ");
							String updateWord;
							updateWord = sc.next();
							d.dictionaryUpdate(updateWord);
							break;
						case 4:
							d.getdictionary("");
							d.showAllWords();
							break;
						case 5:
							System.out.print("Look up: ");
							String lookUpWord = sc.next();
							System.out.println("Look up return: " + d.dictionaryLookup(lookUpWord));
							break;
						case 6:
							System.out.print("Search word: ");
							String Search = sc.next();
							d.getdictionary(Search);
							d.showAllWords();
							break;
						case 7:
							System.out.println("Game Start");
							Gamecmd game = new Gamecmd();
							game.run();
							break;
						case 8:
							DictionaryManagement.dictionaryinsertFromFile("dictionaries.txt");
							System.out.println("Insert from file successfully");
							break;
						case 9:
							d.dictionaryExportToFile("output.txt");
							System.out.println("Exprot to file successfully");
							break;
					}
				} else {
					System.out.println("Invalid input, please select from 0-9\n" + "Your action:");
					continue;
				}
			}
			sc.close();
		}  catch(InputMismatchException e) {
			System.out.println("Wrong input");
			dictionaryAdvanced();
		}
	}
	public static void playGame() {
		// Implement your game logic here
		System.out.println("You selected the game option. Implement your game logic here.");
	}
	public static void main(String[] args) throws IOException {
		dictionaryAdvanced();
	}
}
