import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private float opacity = 1.0f;

    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = ImageIO.read(new File("/Users/sinem/Desktop/CNproject 2/src/kmoi.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setOpaque(false);
        setLayout(new BorderLayout());
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            g2d.dispose();
        }
    }
}
