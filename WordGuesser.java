import java.io.*;
import java.util.*;
import java.net.*;

public class WordGuesser {


    public static String[] getWordList(String urlPath) throws Exception {
	StringBuilder contentBuilder = new StringBuilder();
        URL url = new URL(urlPath);
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

    private static int uniqueLetters(String word) {
	HashSet<Character> set = new HashSet<Character>();
	for (int i = 0; i < word.length(); i++) {
	    set.add(word.charAt(i));
	}
	return set.size();
    }

    private static String getOneWord(String[] wordList) {
	Random rand = new Random();
	boolean found = false;
	String word = "";
	while (!found) {
	    word = wordList[rand.nextInt(wordList.length)];
	    if (word.length() <= 6) {
		found = true;
	    }
	    else if (uniqueLetters(word) <= 6) {
		found = true;
	    }
	}
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

    private static String welcomeMessage(int difficulty, int maxLength, int numGuesses) {
	Scanner scanner = new Scanner(System.in);
	System.out.println("Welcome to word Guesser!");
	System.out.println("To win this game, you must correctly guess the word I am thinking of in " + numGuesses + "turns.");
	System.out.println("Press s to adjust game settings, or press any other key to begin.");
	String urlPath = String.format("http://app.linkedin-reach.io/words?difficulty=%d&maxLength=%d", difficulty, maxLength);
	String input = scanner.nextLine();
	if ((!input.equals("")) && (Character.toLowerCase(input.charAt(0)) == 's')) {
	    urlPath = adjustSettings(urlPath, difficulty, maxLength);
	}
	return urlPath;
    }


    private static boolean playAgain() {
	Scanner scanner = new Scanner(System.in);
	boolean validAnswer = false;
	while (!validAnswer) {
	    System.out.println("Would you like to play again? (y/n)");
	    String input = scanner.nextLine();
	    if (input.equals("")) {
		System.out.println("Invalid input.");
		continue;
	    }	   
	    char answer = Character.toLowerCase(input.charAt(0));
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

    private static String adjustSettings(String urlPath, int difficulty, int maxLength) {
	Scanner scanner = new Scanner(System.in);
	boolean validAnswer = false;
	while (!validAnswer) {
	    System.out.println("Choose  a difficulty level (1-10): ");
	    int number = scanner.nextInt();
	    if ((number < 1) || (number > 10)) {
	        System.out.println("Invalid input.");
	    }
	    else {
	        difficulty = number;
	        validAnswer = true;
	    }
	}
	validAnswer = false;
	while (!validAnswer) {
	    System.out.println("Choose the length for the longest word (1-12): ");
	    int number = scanner.nextInt();
	    if ((number < 1) || (number > 12)) {
	        System.out.println("Invalid input.");
	    }
	    else {
	        maxLength = number;
	        validAnswer = true;
	    }
	}
	String result = String.format("%s?difficulty=%d&maxLength=%d", urlPath, difficulty, maxLength);
	return result;
    }

    public static void main(String[] args) throws Exception {
        String[] wordList = getWordList("http://app.linkedin-reach.io/words?difficluty=1&maxLength=6");
	int difficulty = 1;
	int maxLength = 6;
	int numGuesses = 6;
	boolean game_on = true;
	String urlPath = welcomeMessage(difficulty, maxLength, numGuesses);
	while (game_on) {
	    String secretWord = getOneWord(wordList);
	    playOneGame(secretWord, numGuesses);
	    if (!playAgain()) {
		game_on = false;
	    }
	}
    }
}
