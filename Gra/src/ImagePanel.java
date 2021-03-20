import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JComponent {
    // Klasa odpowiedzialna ze drukowanie tła, nic konkretnie specjalnego: konstruktor przyjmujący obiekt jako obrazek i następnie rysujący go na środku okna w paintComponent
    private Image image;

    public ImagePanel(Image image) {
        this.image = image;
    }
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        graphics.drawImage(image,0,0,this);
    }
}