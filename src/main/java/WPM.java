import com.diogonunes.jcolor.Attribute;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.*;


public class WPM {
    static Scanner readInput = new Scanner(System.in);
    static Timer timer = new Timer();

    static int WORDCOUNTPERLINE=12;

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
        int x=0;

        while (true) {
            cleanScreen();

            printWords(0, WORDCOUNTPERLINE*2, x, BOLD());

            System.out.print("\n-------------------------------------\n");
            System.out.print("Time: 60 | ");
            System.out.print("Net WPS: 150 | ");
            System.out.print("Accuracy: 100%");

            String command = readInput.nextLine();

            switch (command) {
                case "": {
                    x++;
                } break;

                default: {
                    x--;
                }
            }

            System.out.print("\n-------------------------------------\n");
            System.out.print("Time: 60 | ");
            System.out.print("Net WPS: 150 | ");
            System.out.print("Accuracy: 100%");

        }
    }

    private static void printWords(int sWordsStartIndex, int sWordsEndIndex, int currentWordIndex, Attribute wordColor){
        List words= readWordsFromFile(sWordsStartIndex, sWordsEndIndex);
        words.set(WORDCOUNTPERLINE-1, words.get(WORDCOUNTPERLINE-1)+ "\n");

        words.set(currentWordIndex, colorize((String) words.get(currentWordIndex), wordColor));

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