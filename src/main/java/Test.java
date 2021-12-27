import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;


public class Test implements KeyListener{
    public void keyPressed(KeyEvent e) {
        System.out.println("keyPressed");
        System.out.println(e.getKeyChar());
    }

    public void keyReleased(KeyEvent e) {
        System.out.println("keyReleased");

    }
    public void keyTyped(KeyEvent e) {
        System.out.println("keyTyped");
    }

    public static void main(String[] args) {
       JFrame jf = new JFrame("Key event");
       jf.setSize(400,400);
       jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       jf.addKeyListener(new Test());
       jf.setVisible(true);
    }
}
