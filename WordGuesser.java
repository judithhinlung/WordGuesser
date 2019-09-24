import java.io.*;
import java.util.*;
import java.net.*;

public class WordGuesser {


    public static String[] getWordList() throws Exception {
	StringBuilder contentBuilder = new StringBuilder();
        URL url = new URL("http://app.linkedin-reach.io/words");
        BufferedReader br = new BufferedReader(
					       new InputStreamReader(url.openStream()));
		String sCurrentLine;
		while ((sCurrentLine = br.readLine()) != null) 
			{
			contentBuilder.append(sCurrentLine).append("\n");
		    }
		br.close();
        String[] patterns = contentBuilder.toString().split("\n");
	return patterns;
    }


    private static List<String> filterPatterns(String[] patterns, int numChars) {
	List<String> filteredList = new ArrayList<String>();
	for (int i = 0; i < patterns.length; i++) {
	    if (patterns[i].length() <= numChars) {
		filteredList.add(patterns[i]);
	    }
	}
	return filteredList;
    }

    private static String getOneWord(List<String> wordList) {
	Random rand = new Random();
	String word = wordList.get(rand.nextInt(wordList.size()));
	return word;
    }

    private static void printScore(char[] currentGuess, int score, int numGuesses, HashSet<Character> incorrectGuesses) {
	String currentWord = "";
	for (int i = 0; i < currentGuess.length; i++) {
	    currentWord += currentGuess[i];
	}
	System.out.printf("Secret word: %s\t Score: %d\tRemaining turns: %d", currentWord, score, numGuesses);
	System.out.println("Incorrect Guesses: " + incorrectGuesses);
	System.out.println();
    }

    private static int updateScore(String secretWord, char[] currentGuess, int score, HashSet<Character> correctGuesses, char input) {
	for (int i = 0; i < secretWord.length(); i++) {
	    if (secretWord.charAt(i) == input) {
		currentGuess[i] = input;
		correctGuesses.add(input);
		score+= 1;
	    }
	}
	return score;
    }

    private static boolean  contains(char c, String w) {
	for (int i = 0; i < w.length(); i++) {
	    if (w.charAt(i) == c) {
					    return true;
					}
	}
					return false;
				    }

    public static void playOneGame(String secretWord, int numGuesses) {
	System.out.println(secretWord);
	int score = 0;
	HashSet<Character> correctGuesses = new HashSet<Character>();
	HashSet<Character> incorrectGuesses = new HashSet<Character>();
	char[] currentGuess = new char[secretWord.length()];
	for (int i = 0; i < currentGuess.length; i++) {
	    currentGuess[i] = '_';
	}
	Scanner scanner = new Scanner(System.in);
	while ((score < secretWord.length()) && (numGuesses > 0)) {
	    printScore(currentGuess, score, numGuesses, incorrectGuesses);
	    System.out.println("Enter a letter: ");
	    String input = scanner.nextLine();
	    if ((input.equals("")) || (!Character.isLetter(input.charAt(0)))) {
		System.out.println("Invalid input");
	    }
            char answer = Character.toLowerCase(input.charAt(0));
	    if ((correctGuesses.contains(answer)) || (incorrectGuesses.contains(answer))) {
		System.out.println("You already guessed " + answer);
	    }
	    else if (contains(answer, secretWord)) {
		System.out.println("Correct!");
		score = updateScore(secretWord, currentGuess, score, correctGuesses,answer);
		numGuesses--;
	    }
	    else {
		incorrectGuesses.add(answer);
		System.out.print("Wrong answer, ");
		if (numGuesses > 1) {
		    System.out.println("try again!");
		}
		else {
		    System.out.println("You have no more turns.");
		}
		numGuesses--;
	    }
	}
	printScore(currentGuess, score, numGuesses, incorrectGuesses);
	if (score == secretWord.length()) {
	    System.out.println("Congratulations, you guessed the secret word!");
	}
	else {
	boolean validAnswer = false;
	    while (!validAnswer) {
	    System.out.println("Would you like to reveal the secret word? (y/n");
	    String input = scanner.nextLine();
	    if (input.equals("")) {
		continue;
	    }
	    char answer = Character.toLowerCase(input.charAt(0));
	    if ((answer == 'y') || (answer == 'n')) {
		validAnswer = true;
	        if (answer == 'y') {
                  System.out.println("Secret word: " + secretWord);
                }
	    }
	    else {
		System.out.println("Invalid input");
	    }
	    }
        }
    }

    private static void welcomeMessage(int numGuesses) {
	System.out.println("Welcome to word Guesser!");
	System.out.println("To win this game, you must correctly guess the word I am thinking of in " + numGuesses + "turns.");
    }

    private static boolean playAgain() {
	Scanner scanner = new Scanner(System.in);
	boolean validAnswer = false;
	while (!validAnswer) {
	    System.out.println("Would you like to play again? (y/n)");
	    char answer = Character.toLowerCase(scanner.nextLine().charAt(0));
	    if ((answer == 'y') || (answer == 'n')) {
		validAnswer = true;
		if (answer == 'y') {
		    return true;
		}
		else if (answer == 'n') {
		    System.out.println("Goodbye!");
		    return false;
		}
	    }
	    else {
		System.out.println("Invalid input.");
	    }
	}
	return true;
    }

    public static void main(String[] args) throws Exception {
String[] patterns = getWordList();
	List<String> wordList = filterPatterns(patterns, 6);
	int numGuesses = 6;
	boolean game_on = true;
	welcomeMessage(numGuesses);
	while (game_on) {
	    String secretWord = getOneWord(wordList);
	    playOneGame(secretWord, numGuesses);
	    if (!playAgain()) {
		game_on = false;
	    }
	}
    }
}
