import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;


public class Test{


    public static void main(String[] args) {
        System.out.println("1-Hi");
        System.out.println("2-Hi");
        System.out.println("3-Hi");
        System.out.println("4-Hi");
        System.out.println("5-Hi");

        int count = 1;
        System.out.print(String.format("\033[%dA",3)); // Move up
        System.out.print("\033[2K"); // Erase line content

    }
}
