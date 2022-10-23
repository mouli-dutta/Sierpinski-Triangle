package wordgame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerVsBotGame {
    private final PlayerVsBot[] playerVsBots;
    private final List<String> dictionary;
    private final List<String> history;
    private String lastWord = "";

    private final Scanner sc = new Scanner(System.in);
    private String input = "";


    PlayerVsBotGame(PlayerVsBot[] playerVsBots, List<String> dictionary) {
        this.playerVsBots = playerVsBots;
        this.dictionary = dictionary;
        this.history = new ArrayList<>();
    }

    public static void main(String[] args) {
        String path = "C:\\Users\\mouli\\OneDrive\\Desktop\\Dicitonary\\dict.txt";
        ArrayList<String> dictionary = new ArrayList<>();

        try (var scanner = new Scanner(new FileReader(path));
              var sc = new Scanner(System.in)) {
            String line = scanner.next();
            while (scanner.hasNext()) {
                dictionary.add(line);
                line = scanner.next();
            }

            System.out.println("Enter your name:");
            String userName = sc.next();
            PlayerVsBot[] objects = {
                    new Player(userName),
                    new Bot("Bot", dictionary)
            };

            System.out.printf("Hello, %s!\nWelcome to Word Game.\nYour objective in this game is to enter a word that starts with the end letter of the previous word.\nAll the best!\n\n" , userName);

            PlayerVsBotGame game = new PlayerVsBotGame(objects, dictionary);
            game.start();

            System.out.println(objects[0].getStore());
            System.out.println(objects[1].getStore());


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void turn() {

        for (int i = 0; i < playerVsBots.length; i++) {
            if (playerVsBots[i] instanceof Bot) {
                playForBot(i);

            } else if (playerVsBots[i] instanceof Player) {
                playForPlayer(i);
            }
        }
    }

    private void playForPlayer(int i) {
        String nextWord = playerVsBots[i].play(input);

        int lastPos = i ^ 1;
        String lastPlayer = playerVsBots[lastPos].getName();

        if (lastWord.equals("quit") || nextWord.equals("quit")) {
            throw new RuntimeException(
                    String.format("You entered Quit.\n%s won!%n\nStopping the game.%n", lastPlayer));

        } else if (!dictionary.contains(nextWord)) {
            throw new RuntimeException(
                    String.format("`%s` is not an english word.\n%s won!%n\nEnding game%n", nextWord, lastPlayer));

        } else if (history.contains(nextWord)) {
            throw new RuntimeException(
                    String.format("`%s` is a repeated word.\n%s won!%n\nEnding game%n", nextWord, lastPlayer));

        } else if (lastWord.length() > 0 && !check(lastWord, nextWord)) {
            throw new RuntimeException(
                    String.format("Your word didn't start with end letter of previous word.\n%s won!%n\nStopping the game.%n", lastPlayer));

        } else {
            lastWord = nextWord;
            history.add(nextWord);
        }
    }

    private void playForBot(int i) {
        String word = playerVsBots[i].play(lastWord);
        int lastPos = i ^ 1;
        String lastPlayer = playerVsBots[lastPos].getName();

        if (history.contains(word)) {
            System.out.printf("Word `%s` was repeated.%n`%s` won%n", word, lastPlayer);
            throw new RuntimeException("Game Ended");

        } else if (!dictionary.contains(word)) {
            System.out.printf("Word `%s` is not an english word.%n`%s` won%n", word, lastPlayer);
            throw new RuntimeException("Game Ended");

        } else {
            lastWord = word;
            history.add(word);
        }
    }

    private static boolean check(String s1, String s2) {
        if (s1.length() == 0 || s2.length() == 0) return false;
        return s1.charAt(s1.length() - 1) == s2.charAt(0);
    }

    public void start() {
        int turnNumber = 1;
        try {

            while (true) {
                System.out.println("Turn " + turnNumber);

                System.out.println("Enter your word");
                input = sc.next();

                if (input.equals("\"quit\"")) {
                    System.out.println("You entered quit.\nStopping the game.");
                    break;
                }

                turn();
                turnNumber++;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


interface PlayerVsBot {
    String play(String word);
    String getName();
    List<String> getStore();
}
