import jaco.mp3.player.MP3Player;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {
    public Main() throws IOException { new GraWToku(); }
    //wywoluje piosenkę w tle
    public static final String pathSong="Muza.mp3";
    public static MP3Player mp3Player =new MP3Player(new File(pathSong));

    public static void main(String[] args) throws IOException {

        JFrame window= new JFrame();
        GraWToku graWToku= new GraWToku();
        window.setBounds(10,10,702,600);
        window.setTitle("Breakout");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(graWToku);
        window.setVisible(true);

    }
}
