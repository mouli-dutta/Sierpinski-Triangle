package wordgame;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlayerVsPlayerGame {

    public static void main(String[] args) {
        String path = "C:\\Users\\mouli\\Desktop\\Dicitonary\\dict.txt";
        ArrayList<String> dictionary = new ArrayList<>();

        try (var fileScanner = new Scanner(new FileReader(path))) {
            String line = fileScanner.next();
            while (fileScanner.hasNext()) {
                dictionary.add(line);
                line = fileScanner.next();
            }

            Player[] players = {
                    new Player("Mouli"),
                    new Player("Mousumi")
            };

            GameForPlayer gameForPlayer = new GameForPlayer(players, dictionary);
            gameForPlayer.start();

            System.out.println("Words used by " + players[0].getName() + " are :\n" + players[0].getStore());
            System.out.println("Words used by " + players[1].getName() + " are :\n" + players[1].getStore());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}

class Player implements PlayerVsBot {
    private final String name;
    private final List<String> store;

    Player(String name) {
        this.name = name;
        store = new ArrayList<>();
    }

    public String play(String word) {
        System.out.printf("Player %s said word `%s`%n", getName(), word);
        store.add(word);
        return word;
    }

    public String getName() {
        return name;
    }

    public List<String> getStore() {
        return store;
    }
}

class GameForPlayer {
    private final Player[] players;
    private final List<String> dictionary;
    private final List<String> history;
    private String lastWord = "";

    GameForPlayer(Player[] players, List<String> dictionary) {
        this.players = players;
        this.dictionary = dictionary;
        history = new ArrayList<>();
    }

    private void turn() {
        for (int i = 0; i < players.length; i++) {
            System.out.println("Enter your word");
            String nextWord = players[i].play(getNext());

            int lastPos = i ^ 1;
            String lastPlayer = players[lastPos].getName();

            if (lastWord.equals("quit") || nextWord.equals("quit")) {
                throw new RuntimeException(
                        String.format("You entered Quit.\n%s won!%n\nStopping the game.%n", lastPlayer));

            } else if (!dictionary.contains(nextWord)) {
                throw new RuntimeException(
                        String.format("%s is not an english word.\n%s won!%n\nEnding game%n", nextWord, lastPlayer));

            } else if (history.contains(nextWord)) {
                throw new RuntimeException(
                        String.format("%s is a repeated word.\n%s won!%n\nEnding game%n", nextWord, lastPlayer));

            } else if (lastWord.length() > 0 && !check(lastWord, nextWord)) {
                throw new RuntimeException(
                        String.format("Your word didn't start with end letter\n%s won!%n\nStopping the game.%n", lastPlayer));

            } else {
                lastWord = nextWord;
                history.add(nextWord);
            }
        }
    }

    private String getNext() {
        return new Scanner(System.in).next();
    }

    public void start() {
        int gameNumber = 1;
        try {

            while (true) {
                System.out.println("Game #" + gameNumber);
                turn();
                gameNumber++;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static boolean check(String s1, String s2) {
        if (s1.length() == 0 || s2.length() == 0) return false;
        return s1.charAt(s1.length() - 1) == s2.charAt(0);
    }
}
