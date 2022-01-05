import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.regex.Pattern;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;


public class WPM implements KeyListener {
    static final int WORDCOUNTPERLINE = 12;

    static final String RESET = "\033[0m";

    static final String GREEN = "\033[0;32m";
    static final String RED = "\033[0;31m";

    static final String WHITE_BACKGROUND = "\033[47m";
    static final String YELLOW_BACKGROUND = "\033[43m";
    static final String BLUE_BACKGROUND = "\033[44m";

    int currentWordIndex = 0;
    String typedWord = null;

    static List words;
    static int lineCounter = 0, latestWordIndex = 0;;

    static ArrayList<String> wordsFromFile = new ArrayList<>();




    // Listening methods
    public void keyPressed(KeyEvent e) {
        //System.out.println("keyPressed");
        char character = e.getKeyChar();

        if(character == KeyEvent.VK_SPACE && typedWord.length() != 0){
            ++currentWordIndex;

            if(currentWordIndex == WORDCOUNTPERLINE)
                test(currentWordIndex = 0, typedWord = null);
            else
                test(currentWordIndex, typedWord = "");
        }

        if(character == KeyEvent.VK_BACK_SPACE && typedWord.length() != 0)
            test(currentWordIndex, typedWord = typedWord.substring(0, typedWord.length() - 1));


        if(Character.isAlphabetic(character)){
            typedWord = typedWord == null ? "" : typedWord;
            typedWord += Character.toString(character);
            test(currentWordIndex, typedWord);
        }
    }

    public void keyReleased(KeyEvent e) {
        //System.out.println("keyReleased");
    }

    public void keyTyped(KeyEvent e) {
        //System.out.println("keyTyped");
    }

    public static void main(String[] args) throws IOException {
        JFrame jf = new JFrame("Key event");
        jf.setSize(400,400);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jf.addKeyListener(new WPM());
        jf.setVisible(true);

        test(0, null);
    }

    private static void welcome() {
        cleanScreen();
        System.out.println(colorize("=====", WHITE_TEXT()) + colorize("Real Time WPS Calculator", YELLOW_TEXT()) + colorize("=====", WHITE_TEXT()));
        System.out.println("Press \"Enter\" to start.");
        Scanner readInput = new Scanner(System.in);
        if (readInput.nextLine().equals(""))
            countDown();
        else
            welcome();
    }

    static void test(int currentWordIndex, String typedWord) {
        cleanScreen();
        printWords(currentWordIndex, typedWord);

        System.out.println("\nTyped: " + (typedWord == null ? "" : typedWord));
        System.out.print("-------------------------------------\n");
        System.out.print("Time: 60 | ");
        System.out.print("Net WPS: 150 | ");
        System.out.println("Accuracy: 100%\n");
        System.out.print("Index: " + currentWordIndex);
    }

     static void printWords(int currentWordIndex, String typedWord){
        String previousWord="";

        //New line gets
        if(currentWordIndex == 0 && typedWord == null){
            lineCounter += WORDCOUNTPERLINE;
            words = readWordsFromFile(lineCounter);

            words.set(currentWordIndex, cleanAnsiAndSetColor((String) words.get(currentWordIndex), BLUE_BACKGROUND));

            // System.out.println("New line");
        }
        else {
            //Still same word
            if(latestWordIndex == currentWordIndex){
                String currentWord =(String) words.get(currentWordIndex);
                currentWord = currentWord.replaceAll("\u001B\\[[\\d;]*[^\\d;]","");

                if(currentWord.startsWith(typedWord))
                    words.set(currentWordIndex, cleanAnsiAndSetColor((String) words.get(currentWordIndex), GREEN));
                else
                    words.set(currentWordIndex, cleanAnsiAndSetColor((String) words.get(currentWordIndex), RED));
            }
            //New word
            else {
                latestWordIndex = currentWordIndex;
                words.set(currentWordIndex, cleanAnsiAndSetColor((String) words.get(currentWordIndex), BLUE_BACKGROUND));
            }
        }

        String showingWords= words.toString().replace(",", "");
        System.out.println(showingWords.substring(1, showingWords.length() - 1));
    }

    static String cleanAnsiAndSetColor(String word, String color){
        return color + word.replaceAll("\u001B\\[[\\d;]*[^\\d;]","") + RESET;
    }


    static ArrayList<String> readWordsFromFile(int lineCounter){
        if(wordsFromFile.size() == 0){
            try {
                File file = new File("words.txt");
                Scanner words = new Scanner(file);

                while (words.hasNext()) {
                    wordsFromFile.add(words.next().toLowerCase(Locale.ROOT));
                }

            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
        }
        ArrayList<String> words = new ArrayList<>(wordsFromFile.subList(lineCounter - WORDCOUNTPERLINE, (lineCounter - WORDCOUNTPERLINE) + WORDCOUNTPERLINE * 2));
        words.set(WORDCOUNTPERLINE-1, words.get(WORDCOUNTPERLINE-1) + "\n");

        return words;
    }





     static void countDown() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int count = 3;

            public void run() {
                cleanScreen();

                if (count == 0) {
                    timer.cancel();
                    test(0, null);
                } else
                    System.out.println("=== " + count + " ===");

                count--;
            }
        }, 0, 1000);
    }

     static void cleanScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}