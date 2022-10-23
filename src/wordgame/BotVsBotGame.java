package wordgame;
/*
    Word game :
        The game starts with a word.
        To continue the game one has to come up a word
         that starts with the letter that the previous word ended with.

    Conditions for stopping the game :
        If one repeats a word.
        If the word doesn't belong to the english dictionary.
        If the user enter the word "Quit".


        infinite loop
            Enter a word.
            check if the word is valid.
                Valid : the bot will give a random word which starts with the end letter of the previous word.
                Not valid : break out of loop and display the winner.

 */

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BotVsBotGame {
    public static void main(String[] args) {
        String path = "C:\\Users\\mouli\\Desktop\\Dicitonary\\dict.txt";
        ArrayList<String> dictionary = new ArrayList<>();

        try (var fileScanner = new Scanner(new FileReader(path))) {

            String line = fileScanner.next();

            while (fileScanner.hasNext()) {
                dictionary.add(line);
                line = fileScanner.next();
            }

            Bot[] bots = {
                    new Bot("Mouli", dictionary),
                    new Bot("Mousumi", dictionary)
            };

            Game game = new Game(bots, dictionary);
            game.start();

            List<String> store = bots[0].getStore();
            System.out.println(store);

            List<String> store1 = bots[1].getStore();
            System.out.println(store1);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


/**
 * {@code Bot} class generates a random valid english word
 * which starts with the end letter of the previous word.
 */
class Bot implements PlayerVsBot {
    private final String name;
    private final List<String> dictionary;
    private final Random rand;
    private final List<String> store;

    Bot(String name, List<String> dictionary) {
        this.name = name;
        this.dictionary = dictionary;
        rand = new Random();
        store = new ArrayList<>();
    }

    public String play(String lastWord) {
        String word = "";
        List<String> selected = new ArrayList<>();

        if (lastWord.length() > 0) {
            for (String d : dictionary) {
                if (d.charAt(0) == lastWord.charAt(lastWord.length() - 1)) {
                    selected.add(d);
                }
            }

            if (selected.size() == 0) throw new RuntimeException("Game ended");

            word = selected.get(rand.nextInt(selected.size()));
        }

        if (word.length() == 0) {
            word = dictionary.get(rand.nextInt(dictionary.size()));
        }

        System.out.printf("Player `%s` said word `%s`%n", name, word);

        store.add(word);
        return word;
    }

    public List<String> getStore() {
        return store;
    }

    public String getName() {
        return name;
    }
}


class Game {
    private final Bot[] bots;
    private final List<String> dictionary;
    private final List<String> history;
    private String lastWord = "";


    Game(Bot[] bots, List<String> dictionary) {
        this.bots = bots;
        this.dictionary = dictionary;
        this.history = new ArrayList<>();
    }

    private void turn() {

        for (int i = 0; i < bots.length; i++) {
            String word = bots[i].play(lastWord);

            // 1st player = 0 and 2nd player = 1
            // if game stops at 2nd player (1) then the winner is 1st player (0)
            // i.e. flip 1 to 0 and 0 to 1
            int lastPos = i ^ 1;
            String lastPlayer = bots[lastPos].getName();

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
    }

    public void start() {
        int turnNumber = 1;
        try {

            while (true) {
                System.out.println("Turn " + turnNumber);
                turn();
                turnNumber++;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}

