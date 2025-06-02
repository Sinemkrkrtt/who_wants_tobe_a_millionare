import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class YarismaciClientGUI extends JFrame {
    private JLabel soruLabel;
    private JButton secimA, secimB, secimC, secimD;
    private JButton jokerS, jokerY;
    private JTextArea mesajAlani;

    public YarismaciClientGUI() {
        setTitle("Kim Milyoner Olmak İster - Yarışmacı");
        setSize(500, 400);
        setBackground(new Color(0, 0, 80));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        soruLabel = new JLabel("Soru: Buraya soru gelecek");
        soruLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(soruLabel, BorderLayout.NORTH);

        JPanel secenekPaneli = new JPanel(new GridLayout(2, 2));
        secimA = new JButton("A");
        secimB = new JButton("B");
        secimC = new JButton("C");
        secimD = new JButton("D");

        secenekPaneli.add(secimA);
        secenekPaneli.add(secimB);
        secenekPaneli.add(secimC);
        secenekPaneli.add(secimD);
        add(secenekPaneli, BorderLayout.CENTER);

        JPanel jokerPaneli = new JPanel(new FlowLayout());
        jokerS = new JButton("Seyirciye Sor");
        jokerY = new JButton("Yarı Yarıya");
        jokerPaneli.add(jokerS);
        jokerPaneli.add(jokerY);
        add(jokerPaneli, BorderLayout.SOUTH);

        mesajAlani = new JTextArea(5, 40);
        mesajAlani.setEditable(false);
        add(new JScrollPane(mesajAlani), BorderLayout.EAST);

        ActionListener cevapListener = e -> {
            JButton tiklanan = (JButton) e.getSource();
            mesajAlani.append("Seçilen cevap: " + tiklanan.getText() + "\n");
        };

        secimA.addActionListener(cevapListener);
        secimB.addActionListener(cevapListener);
        secimC.addActionListener(cevapListener);
        secimD.addActionListener(cevapListener);

        jokerS.addActionListener(e -> mesajAlani.append("Seyirciye Sor jokeri kullanıldı\n"));
        jokerY.addActionListener(e -> mesajAlani.append("Yarı Yarıya jokeri kullanıldı\n"));

        setVisible(true);
    }

    public static void main(String[] args) {
        new YarismaciClientGUI();
    }
}
