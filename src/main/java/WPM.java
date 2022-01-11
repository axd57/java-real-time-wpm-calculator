import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Pattern;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;


public class WPM implements KeyListener {
    static final int WORDCOUNTPERLINE = 12;
    static int correctCharacterCount = 0, wrongCharacterCount = 0;

    static CalculationThread thread= new CalculationThread();

    static int[] threadDatas = new int[2];



    static final String RESET = "\033[0m";

    static final String GREEN = "\033[0;32m";
    static final String RED = "\033[0;31m";

    static final String WHITE_BACKGROUND = "\033[47m";
    static final String YELLOW_BACKGROUND = "\033[43m";
    static final String BLUE_BACKGROUND = "\033[44m";

    int currentWordIndex = 0;
    String typedWord = null;

    static boolean startControl = false;

    static List words;
    static int lineCounter = 0, latestWordIndex = 0;;

    static ArrayList<String> wordsFromFile = new ArrayList<>();




    // Listening methods
    public void keyPressed(KeyEvent e) {
        //System.out.println("keyPressed");
        char character = e.getKeyChar();

        if(character == KeyEvent.VK_SPACE && (typedWord == null ? false : typedWord.length() != 0 ? true : false)){
            ++currentWordIndex;


            if(currentWordIndex == WORDCOUNTPERLINE)
                test(currentWordIndex = 0, typedWord = null);
            else{
                // Ture word control after space
              if(cleanAnsiAndSetColor((String) words.get(latestWordIndex),"").equals(typedWord))
                  correctCharacterCount += typedWord.length() + 1;
              else{
                  words.set(latestWordIndex, cleanAnsiAndSetColor((String) words.get(latestWordIndex), RED));
                  wrongCharacterCount += cleanAnsiAndSetColor((String) words.get(latestWordIndex),"").length() + 1;
              }


                test(currentWordIndex, typedWord = "");
            }

        }

        if(character == KeyEvent.VK_BACK_SPACE && (typedWord == null ? false : typedWord.length() != 0 ? true : false))
            test(currentWordIndex, typedWord = typedWord.substring(0, typedWord.length() - 1));


        if(Character.isAlphabetic(character)){
            typedWord = typedWord == null ? "" : typedWord;
            typedWord += Character.toString(character);
            test(currentWordIndex, typedWord);
        }


        threadDatas[0] = correctCharacterCount;
        threadDatas[1] = wrongCharacterCount;
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
        cleanScreen();
        test(0, null);

        Thread threada= new Thread(thread);
        threada.start();




    }

    private static void welcome() {
        System.out.println(colorize("=====", WHITE_TEXT()) + colorize("Real Time WPS Calculator", YELLOW_TEXT()) + colorize("=====", WHITE_TEXT()));
        System.out.println("Press \"Enter\" to start.");
        Scanner readInput = new Scanner(System.in);
        if (readInput.nextLine().equals(""))
            countDown();
        else
            welcome();
    }

    static void test(int currentWordIndex, String typedWord) {
       if(typedWord != null || startControl){
          startControl = true;
           for(int i =3; i < 7; i++){
               System.out.print(String.format("\033[%dA", i));
               System.out.print("\033[2K");
           }
       }

        printWords(currentWordIndex, typedWord);
        System.out.println("\nTyped: " + (typedWord == null ? "" : typedWord));

        System.out.print("-------------------------------------\n\n");


    }

     static void printWords(int currentWordIndex, String typedWord){
        //New line gets
        if(currentWordIndex == 0 && typedWord == null){
            lineCounter += WORDCOUNTPERLINE;
            words = readWordsFromFile(lineCounter);

            words.set(currentWordIndex, cleanAnsiAndSetColor((String) words.get(currentWordIndex), BLUE_BACKGROUND));

            // System.out.println("New line");
        }
        //Same line
        else {
            //Still same word
            if(latestWordIndex == currentWordIndex){
                String currentWord = cleanAnsiAndSetColor((String) words.get(currentWordIndex),"");

                if(typedWord.length() != 0){
                    if(currentWord.startsWith(typedWord)) {
                        if(currentWord.length() == typedWord.length())
                            words.set(currentWordIndex, cleanAnsiAndSetColor((String) words.get(currentWordIndex), GREEN));
                        else
                            words.set(currentWordIndex, cleanAnsiAndSetColor((String) words.get(currentWordIndex), YELLOW_BACKGROUND));

                    }else{

                        words.set(currentWordIndex, cleanAnsiAndSetColor((String) words.get(currentWordIndex), RED));
                    }

                    /*threadDatas[0] = correctCharacterCount;
                    threadDatas[1] = wrongCharacterCount;*/
                }
                else
                    words.set(currentWordIndex, cleanAnsiAndSetColor((String) words.get(currentWordIndex), BLUE_BACKGROUND));
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
        if(color.equals(""))
            return word.replaceAll("\u001B\\[[\\d;]*[^\\d;]","");
        else
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

    static void calculation(){

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

class CalculationThread implements Runnable{
    int cCC=0, wCC=0, grossWPM = 0, netWPM = 0, time = 59, x=0;
    float accurasy = 0f;
    @Override
    public void run() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                cCC = WPM.threadDatas[0];
                wCC = WPM.threadDatas[1];

                if(cCC !=0 || wCC !=0 ){
                    grossWPM =Math.round((cCC+wCC) / 5f / ((60 - time)/60f));
                    netWPM = grossWPM - Math.round((wCC / 5f / ((60 - time)/60f)));
                    accurasy = (float) cCC / (cCC + wCC) * 100;
                }




                System.out.print(String.format("\033[%dA", 1));
                System.out.print("\033[2K");



                if (time == 0) {
                    timer.cancel();

                    System.out.print("Complated.");
                    System.out.println(grossWPM + " " + netWPM + " " + accurasy + " " + cCC + " " + wCC);
                }else
                    System.out.print("Time: "+ time + " | Net WPS: " + netWPM + " | Accuracy: " + accurasy + "%\n");


                time--;
            }
        }, 0, 1000);
    }
}