import javax.swing.*;
import java.awt.*;

public class CircleTimerPanel extends JPanel {
    private int totalCircles = 15;
    private int activeCircles;

    public CircleTimerPanel() {
        this.activeCircles = totalCircles;
        setPreferredSize(new Dimension(500, 50));
        setBackground(new Color(5, 0, 30));
    }

    public void setActiveCircles(int active) {
        this.activeCircles = active;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int circleDiameter = 12;
        int gap = 10;
        int startX = 100;
        int y = getHeight() / 2 - circleDiameter / 2;

        for (int i = 0; i < totalCircles; i++) {
            if (i < activeCircles) {
                g.setColor(new Color(138, 43, 226)); // Mor
            } else {
                g.setColor(Color.GRAY);
            }
            g.fillOval(startX + i * (circleDiameter + gap), y, circleDiameter, circleDiameter);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Circle Timer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CircleTimerPanel panel = new CircleTimerPanel();
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // ⏳ Süreyi Yavaşlatmak İçin Timer Kullanımı
        Timer timer = new Timer(1500, e -> {
            if (panel.activeCircles > 0) {
                panel.setActiveCircles(panel.activeCircles - 1);
            } else {
                ((Timer) e.getSource()).stop();
            }
        });

        timer.setInitialDelay(1000); // Başlamadan önce 1 saniye beklesin
        timer.start();
    }
}
