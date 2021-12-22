import com.diogonunes.jcolor.Ansi;
import com.diogonunes.jcolor.Attribute;
import com.diogonunes.jcolor.Command;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static com.diogonunes.jcolor.Ansi.RESET;
import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;


public class WPM {
    static Scanner readInput = new Scanner(System.in);
    static Timer timer = new Timer();

    static int WORDCOUNTPERLINE=12;


    //!!!!!!
    static List words;




    public static void main(String[] args) {
        test();
    }

    private static void welcome() {
        cleanScreen();
        System.out.println(colorize("=====", WHITE_TEXT()) + colorize("Real Time WPS Calculator", YELLOW_TEXT()) + colorize("=====", WHITE_TEXT()));
        System.out.println("Press \"Enter\" to start.");

        if (readInput.nextLine().equals(""))
            countDown();
        else
            welcome();
    }

    private static void test() {
        int wordIndexCounter = 0, lineCounter = WORDCOUNTPERLINE;

        while (true) {

            if(wordIndexCounter == WORDCOUNTPERLINE) {
                wordIndexCounter = 0;
                lineCounter += WORDCOUNTPERLINE;
            }


            cleanScreen();

            printWords(wordIndexCounter, lineCounter);

            System.out.print("\n-------------------------------------\n");
            System.out.print("Time: 60 | ");
            System.out.print("Net WPS: 150 | ");
            System.out.println("Accuracy: 100%");
            System.out.print(wordIndexCounter);

            String command = readInput.nextLine();

            switch (command) {
                case "": {
                   wordIndexCounter++;
                } break;

                default: {

                    wordIndexCounter--;
                }
            }

            System.out.print("\n-------------------------------------\n");
            System.out.print("Time: 60 | ");
            System.out.print("Net WPS: 150 | ");
            System.out.print("Accuracy: 100%");

        }
    }

    private static void printWords(int currentWordIndex, int lineCounter){
        if(currentWordIndex == 0){
            words = readWordsFromFile(lineCounter - WORDCOUNTPERLINE, (lineCounter - WORDCOUNTPERLINE) + WORDCOUNTPERLINE * 2);
            words.set(WORDCOUNTPERLINE-1, words.get(WORDCOUNTPERLINE-1)+ "\n");
        }
        else {
            // String text=(String) words.get(currentWordIndex - 1);
            //words.set(currentWordIndex - 1, text.replaceAll("\\d{1,2}(;\\d{1,2})?", ""));


            words.set(currentWordIndex - 1, RESET + colorize((String) words.get(currentWordIndex - 1), RED_TEXT()));
        }

        words.set(currentWordIndex, colorize((String) words.get(currentWordIndex), GREEN_TEXT()));

        String showingWords= words.toString().replace(",", "");

        System.out.println(showingWords.substring(1, showingWords.length() - 1));



        /*String showingWords="";
        for(int i=0; i < words.size(); i++){
            showingWords += words.get(i)+" ";

            if(i+1 == WORDCOUNTPERLINE)
                showingWords += "\n";

            if(i+1 == words.size())
                showingWords = showingWords.substring(0, showingWords.length() - 1);
        }
        System.out.println(showingWords);*/
    }

    private static void colorizeWordsAndLetters(){

    }


    private static List<String> readWordsFromFile(int startIndex, int endIndex){
        ArrayList<String> wordList= new ArrayList<>();

        try {
            File file = new File("words.txt");
            Scanner words = new Scanner(file);

            while (words.hasNext()) {
                wordList.add(words.next());
            }

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return wordList.subList(startIndex, endIndex);
    }





    private static void countDown() {
        timer.scheduleAtFixedRate(new TimerTask() {
            int count = 3;

            public void run() {
                cleanScreen();

                if (count == 0) {
                    timer.cancel();
                    test();
                } else
                    System.out.println("=== " + count + " ===");

                count--;
            }
        }, 0, 1000);
    }

    private static void cleanScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}