import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Timer;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;


public class WPM implements KeyListener {
    static final int WORDCOUNTPERLINE = 12;

    static final String RESET = "\033[0m";

    static final String GREEN = "\033[0;32m";
    static final String RED = "\033[0;31m";

    static final String WHITE_BACKGROUND = "\033[47m";


    int currentWordIndex = 0;
    String typedText = "";

    static List words;
    static int lineCounter = 0;

    static ArrayList<String> wordsFromFile = new ArrayList<>();




    // Listening methods
    public void keyPressed(KeyEvent e) {
        //System.out.println("keyPressed");
        char character = e.getKeyChar();

        if(character == KeyEvent.VK_SPACE){
            ++currentWordIndex;
            typedText = "";

            if(currentWordIndex == WORDCOUNTPERLINE)
                test(currentWordIndex = 0, ' ', typedText);
            else
                test(currentWordIndex, character, typedText);
        }

        if(Character.isAlphabetic(character)){
            typedText += Character.toString(character);
            test(currentWordIndex, character, typedText);
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

        test(0, ' ', "");
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

    static void test(int currentWordIndex, char currentChar, String typedText) {
        cleanScreen();
        printWords(currentWordIndex, currentChar);

        System.out.println("\nTyped: " + typedText);
        System.out.print("-------------------------------------\n");
        System.out.print("Time: 60 | ");
        System.out.print("Net WPS: 150 | ");
        System.out.println("Accuracy: 100%\n");
        System.out.print("Index: " + currentWordIndex + " Character: " + currentChar);
    }

     static void printWords(int currentWordIndex, char currentChar){
        String previousWord="", nextWord ="";

        if(currentWordIndex == 0 && currentChar == ' '){
            lineCounter += WORDCOUNTPERLINE;
            words = readWordsFromFile(lineCounter);

            words.set(currentWordIndex, "\033[4;37m" + words.get(currentWordIndex) + RESET);

            System.out.println("New line");
        }
        else {
            if(currentWordIndex != 0 ){
                previousWord = (String) words.get(currentWordIndex-1);
                previousWord=previousWord.replace("\033[4;37m", "");
                words.set(currentWordIndex - 1, previousWord);
            }

            words.set(currentWordIndex, "\033[4;37m" + words.get(currentWordIndex) + RESET);

            /*nextWord= (String) words.get(currentWordIndex+1);
            nextWord=nextWord.replace("\033[0;32m", "");
            words.set(currentWordIndex + 1, nextWord);*/

            System.out.println("Skiped word: " + previousWord + " Next word: " + nextWord);
        }




        String showingWords= words.toString().replace(",", "");
        System.out.println(showingWords.substring(1, showingWords.length() - 1));
    }

    private static void colorizeWordsAndLetters(){

    }


    static ArrayList<String> readWordsFromFile(int lineCounter){
        if(wordsFromFile.size() == 0){
            try {
                File file = new File("words.txt");
                Scanner words = new Scanner(file);

                while (words.hasNext()) {
                    wordsFromFile.add(words.next());
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
                    test(0, ' ', "");
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