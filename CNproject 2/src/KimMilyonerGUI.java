import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.List;

public class KimMilyonerGUI extends JFrame {
    private int totalSeconds = 15;
    private int intervalMs = 1600;
    private JTextArea soruAlani;
    private JButton btnA, btnB, btnC, btnD;
    private JButton jokerSeyirci, jokerYariYariya;
    private JLabel odulMesajLabel;
    private JButton btnYenidenBasla;
    private JPanel panel;
    private JPanel sagPanel;
    private JPanel jokerPanel;
    private JPanel paraAgaciPanel;
    private List<JLabel> paraBasamaklari;
    private List<Soru> sorular;
    private int soruIndex = 0;
    private boolean seyirciJokerKullanildi = false;
    private boolean yariYariyaJokerKullanildi = false;
    private Timer countdownTimer;
    private CircleTimerPanel timerA;
    private int secondsLeft;
    private int currentQuestionIndex = 0;
    private Map<Integer, String> rewardMessages = new HashMap<>();  //

    private final String[] oduller = {
            "100 TL", "200 TL", "300 TL", "500 TL", "1.000 TL",
            "2.000 TL", "4.000 TL", "8.000 TL", "16.000 TL", "32.000 TL",
            "64.000 TL", "125.000 TL", "250.000 TL", "500.000 TL", "1.000.000 TL"
    };

    public KimMilyonerGUI() {
        setTitle("Kim Milyoner Olmak İster?");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(5, 0, 30));

        odulMesajLabel = new JLabel("Hoşgeldiniz!");

        timerA = new CircleTimerPanel();

        BackgroundPanel soruPanel = new BackgroundPanel("/Users/sinem/Desktop/java/CNproject/src/kmoi.jpeg");
        soruPanel.setLayout(new BorderLayout());
        soruPanel.setOpacity(0.6f);
        soruPanel.setPreferredSize(new Dimension(700, 150));

        soruAlani = new JTextArea(6, 25);
        soruAlani.setFont(new Font("Arial", Font.BOLD, 26));
        soruAlani.setEditable(false);
        soruAlani.setLineWrap(true);
        soruAlani.setWrapStyleWord(true);
        soruAlani.setOpaque(false);
        soruAlani.setBorder(null);
        soruAlani.setForeground(Color.WHITE);

        soruPanel.add(soruAlani, BorderLayout.CENTER);

        JPanel solPanel = new JPanel(new BorderLayout(10, 10));
        solPanel.setBackground(new Color(5, 0, 30));
        solPanel.add(soruPanel, BorderLayout.CENTER);

        JPanel cevapPanel = new JPanel(new GridLayout(3, 3, 20, 20));
        cevapPanel.setBackground(new Color(5, 0, 30));

        btnA = new JButton("A");
        btnB = new JButton("B");
        btnC = new JButton("C");
        btnD = new JButton("D");

        JButton[] cevapButonlari = {btnA, btnB, btnC, btnD};
        for (JButton btn : cevapButonlari) {
            btn.setOpaque(true);
            btn.setContentAreaFilled(true);
            btn.setBackground(new Color(230, 230, 250));
            btn.setForeground(Color.BLACK);
            cevapPanel.add(btn);
        }

        solPanel.add(cevapPanel, BorderLayout.SOUTH);

        sagPanel = new JPanel(new BorderLayout(20, 20));
        sagPanel.setPreferredSize(new Dimension(200, getHeight()));
        sagPanel.setBackground(new Color(5, 0, 30));

        jokerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        jokerPanel.setBackground(new Color(5, 0, 30));

        ImageIcon seyirciIcon = new ImageIcon(getClass().getResource("/assets/customer.png"));
        ImageIcon yariyariyaIcon = new ImageIcon(getClass().getResource("/assets/pie-chart.png"));

        Image scaledSeyirciImage = seyirciIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
        Image scaledYariYariyaImage = yariyariyaIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);

        jokerSeyirci = new JButton(new ImageIcon(scaledSeyirciImage));
        jokerYariYariya = new JButton(new ImageIcon(scaledYariYariyaImage));

        makeButtonRound(jokerSeyirci);
        makeButtonRound(jokerYariYariya);

        jokerPanel.add(jokerSeyirci);
        jokerPanel.add(jokerYariYariya);

        this.add(jokerPanel);

        sagPanel.add(jokerPanel, BorderLayout.NORTH);

        paraAgaciPanel = new JPanel(new GridLayout(oduller.length, 3, 7, 10));
        paraAgaciPanel.setBackground(new Color(5, 0, 30));
        paraBasamaklari = new ArrayList<>();

        for (int i = oduller.length - 1; i >= 0; i--) {
            JLabel label = new JLabel(oduller[i], SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setOpaque(true);
            label.setBackground(Color.LIGHT_GRAY);
            label.setBorder(new LineBorder(Color.DARK_GRAY, 1));
            paraBasamaklari.add(label);
            paraAgaciPanel.add(label);
        }

        sagPanel.add(paraAgaciPanel, BorderLayout.CENTER);

        add(solPanel, BorderLayout.CENTER);
        add(sagPanel, BorderLayout.EAST);

        loadRewards();

        sorular = new ArrayList<>();
        sorular.add(new Soru("                      Python hangi yıl geliştirilmiştir?",
                new String[]{"1991", "2000", "1989", "2010"}, 'A'));
        sorular.add(new Soru("                            Java'nın yaratıcısı kimdir?",
                new String[]{"Dennis Ritchie", "James Gosling", "Guido van Rossum", "Bjarne Stroustrup"}, 'B'));
        sorular.add(new Soru("                                      HTML nedir?",
                new String[]{"Programlama dili", "Veritabanı", "İşletim sistemi", "İşaretleme dili"}, 'D'));
        sorular.add(new Soru("                          En büyük gezegen hangisidir?",
                new String[]{"Dünya", "Satürn", "Jüpiter", "Mars"}, 'C'));
        sorular.add(new Soru("                              Elektronun yükü nedir?",
                new String[]{"Pozitif", "Negatif", "Nötr", "Değişken"}, 'B'));

        guncelleSoru();

        btnA.addActionListener(e -> cevapKontrol('A'));
        btnB.addActionListener(e -> cevapKontrol('B'));
        btnC.addActionListener(e -> cevapKontrol('C'));
        btnD.addActionListener(e -> cevapKontrol('D'));



        jokerSeyirci.addActionListener(e -> seyirciJoker());
        jokerYariYariya.addActionListener(e -> yariYariyaJoker());

        JPanel timerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        timerPanel.setBackground(new Color(5, 0, 30));
        timerPanel.add(timerA);
        solPanel.add(timerPanel, BorderLayout.NORTH);

        panel = new JPanel(); // ya da zaten varsa, tekrar oluşturmayın
        btnYenidenBasla = new JButton("Yeniden Başla");
        btnYenidenBasla.setVisible(false);
        panel.add(btnYenidenBasla); // panelinize ekleyin
        btnYenidenBasla.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                oyunSifirla();
            }
        });


        secondsLeft = totalSeconds;
        countdownTimer = new Timer(intervalMs, e -> {
            secondsLeft--;
            timerA.setActiveCircles(secondsLeft);

            if (secondsLeft <= 0) {
                countdownTimer.stop();
                JOptionPane.showMessageDialog(null, "Süre doldu! Oyun bitti.");
                System.exit(0);
            }
        });
        countdownTimer.start();

        System.out.println(new File("/Users/sinem/Desktop/java/CNproject/src/assets/customer.png").exists());
    }


    private void loadRewards() {
        rewardMessages.put(0, "Yanlış cevap! Oyun bitti.Linç Yükleniyor...");
        rewardMessages.put(1, "Yanlış cevap! Oyun bitti.Önemli olan katılmaktı:)");
        rewardMessages.put(2, "Yanlış cevap! Oyun bitti.İki birden büyüktür:)");
        rewardMessages.put(3, "Yanlış cevap! Oyun bitti.Buralara kolay gelmedik:)");
        rewardMessages.put(4, "Yanlış cevap! Oyun bitti.Sen bu işi biliyorsun:)");
        rewardMessages.put(5, "Yanlış cevap! Oyun bitti.Harikasın...");
    }

    public void baslatZamanlayici() {
        if (countdownTimer != null) {
            countdownTimer.stop();
        }
        secondsLeft = totalSeconds;
        timerA.setActiveCircles(secondsLeft);

        countdownTimer = new Timer(intervalMs, e -> {
            secondsLeft--;
            timerA.setActiveCircles(secondsLeft);

            if (secondsLeft <= 0) {
                countdownTimer.stop();
                int secim = JOptionPane.showConfirmDialog(null, "Süre doldu! Oyunu yeniden başlatmak ister misiniz?", "Oyun Bitti", JOptionPane.YES_NO_OPTION);
                if (secim == JOptionPane.YES_OPTION) {
                    yenidenBaslat();
                } else {
                    System.exit(0);
                }
            }
        });
        countdownTimer.start();
    }

    private void yenidenBaslat() {
        soruIndex = 0;
        currentQuestionIndex = 0;
        seyirciJokerKullanildi = false;
        yariYariyaJokerKullanildi = false;

        // Tüm butonları tekrar görünür yap
        btnA.setVisible(true);
        btnB.setVisible(true);
        btnC.setVisible(true);
        btnD.setVisible(true);

        // Jokerleri aktif hale getir
        jokerSeyirci.setEnabled(true);
        jokerYariYariya.setEnabled(true);

        // Para basamaklarını sıfırla
        for (JLabel label : paraBasamaklari) {
            label.setBackground(Color.LIGHT_GRAY);
        }

        // İlk sorudan başla
        guncelleSoru();
        baslatZamanlayici();
    }

    private void makeButtonRound(JButton btn) {
        btn.setPreferredSize(new Dimension(60, 60));
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setFocusPainted(false);
        btn.setForeground(new Color(230, 230, 250));
        btn.setContentAreaFilled(false);
        btn.setOpaque(false);
        btn.setBorderPainted(false);
        btn.setBackground(new Color(219, 183, 0));

        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int width = btn.getWidth();
                int height = btn.getHeight();

                g2.setColor(btn.getBackground());
                g2.fillOval(0, 0, width, height);

                Icon icon = btn.getIcon();
                if (icon != null) {
                    int iconWidth = icon.getIconWidth();
                    int iconHeight = icon.getIconHeight();
                    int x = (width - iconWidth) / 2;
                    int y = (height - iconHeight) / 2;
                    icon.paintIcon(btn, g2, x, y);
                } else {
                    FontMetrics fm = g2.getFontMetrics();
                    String text = btn.getText();
                    int textWidth = fm.stringWidth(text);
                    int textHeight = fm.getAscent();
                    g2.setColor(btn.getForeground());
                    g2.drawString(text, (width - textWidth) / 2, (height + textHeight) / 2 - 2);
                }

                g2.dispose();
            }

            @Override
            public Dimension getPreferredSize(JComponent c) {
                return new Dimension(60, 60);
            }
        });
    }


    private void oyunSifirla() {
        soruIndex = 0;
        Collections.shuffle(sorular); // istersen soruları karıştır
        btnYenidenBasla.setVisible(false);
        jokerSeyirci.setEnabled(true);
        jokerYariYariya.setEnabled(true);
        odulMesajLabel.setText("");
        guncelleSoru(); // ilk soruyu yükle
    }

    private void guncelleParaAgaci() {
        for (JLabel label : paraBasamaklari) {
            label.setBackground(new Color(75, 0, 120));
            label.setForeground(new Color(230, 230, 250));
        }
        int idx = paraBasamaklari.size() - 1 - soruIndex;
        if (idx >= 0 && idx < paraBasamaklari.size()) {
            JLabel label = paraBasamaklari.get(idx);
            label.setBackground(new Color(204, 173, 0));
            label.setForeground(new Color(230, 230, 250));
        }
    }

    private void guncelleSoru() {
        if (soruIndex >= sorular.size()) {
            odulMesajLabel.setText("Tebrikler! Tüm soruları doğru cevapladınız!");
            soruAlani.setText("");
            cevapButonlariniPasifYap();
            jokerSeyirci.setEnabled(false);
            jokerYariYariya.setEnabled(false);
            countdownTimer.stop();
            btnYenidenBasla.setVisible(true); // burada görünür yap
            return;
        }

        String[] odulMesajlari = {
                "Linç Yükleniyor",
                "Önemli olan katılmaktı",
                "İki birden büyüktür",
                "Buralara kolay gelmedik",
                "Sen bu işi biliyorsun",
                "Harikasın"
        };
        String mesaj = (soruIndex < odulMesajlari.length) ? odulMesajlari[soruIndex] : "Cevabınızı seçin.";
        odulMesajLabel.setText(mesaj);

        Soru s = sorular.get(soruIndex);
        String metin = s.soruMetni + "\n\n" +
                "\n   A) " + s.siklar[0] + "\n\n" +
                "   B) " + s.siklar[1] + "\n\n" +
                "   C) " + s.siklar[2] + "\n\n" +
                "   D) " + s.siklar[3];
        soruAlani.setText(metin);

        cevapButonlariniAktifYap();
        guncelleParaAgaci();
        baslatZamanlayici();
    }


    private void cevapButonlariniAktifYap() {
        btnA.setEnabled(true);
        btnB.setEnabled(true);
        btnC.setEnabled(true);
        btnD.setEnabled(true);
        btnA.setBackground(null);
        btnB.setBackground(null);
        btnC.setBackground(null);
        btnD.setBackground(null);
    }
    private void cevapButonlariniPasifYap() {
        btnA.setEnabled(false);
        btnB.setEnabled(false);
        btnC.setEnabled(false);
        btnD.setEnabled(false);
    }

    private void cevapKontrol(char secilen) {
        countdownTimer.stop();
        Soru s = sorular.get(soruIndex);
        System.out.println("Seçilen: " + secilen + ", Doğru Cevap: " + s.dogruCevap);

        if (secilen == s.dogruCevap) {
            JOptionPane.showMessageDialog(this, "Doğru cevap! Tebrikler.");
            soruIndex++;
            if (soruIndex < sorular.size()) {
                guncelleSoru();
                countdownTimer.restart();
            } else {
                JOptionPane.showMessageDialog(this, "Tebrikler! Tüm soruları doğru cevapladınız.");
                System.exit(0);
            }
        } else {
            String mesaj = rewardMessages.getOrDefault(soruIndex, "Yanlış cevap! Oyun bitti.");
            JOptionPane.showMessageDialog(this, mesaj);
            cevapButonlariniPasifYap();
            jokerSeyirci.setEnabled(false);
            jokerYariYariya.setEnabled(false);
            System.exit(0);
        }

    }


    private void seyirciJoker() {
        if (seyirciJokerKullanildi) {
            JOptionPane.showMessageDialog(this, "Seyirci jokeri zaten kullanıldı!");
            return;
        }

        seyirciJokerKullanildi = true;
        jokerSeyirci.setEnabled(false);

        Soru s = sorular.get(soruIndex);
        char dogruCevap = s.dogruCevap;

        Random rand = new Random();
        int[] yuzdeler = new int[4];
        int dogruIndex = dogruCevap - 'A';

        yuzdeler[dogruIndex] = rand.nextInt(31) + 50; // 50-80 arası doğru şık
        int kalan = 100 - yuzdeler[dogruIndex];

        List<Integer> digerIndeksler = new ArrayList<>(List.of(0, 1, 2, 3));
        digerIndeksler.remove(Integer.valueOf(dogruIndex));

        yuzdeler[digerIndeksler.get(0)] = rand.nextInt(kalan + 1);
        yuzdeler[digerIndeksler.get(1)] = rand.nextInt(kalan - yuzdeler[digerIndeksler.get(0)] + 1);
        yuzdeler[digerIndeksler.get(2)] = kalan - yuzdeler[digerIndeksler.get(0)] - yuzdeler[digerIndeksler.get(1)];

        JDialog grafikPencere = new JDialog(this, "Seyirci Oylaması", true);
        grafikPencere.setLayout(new BorderLayout());
        grafikPencere.setSize(350, 400);
        grafikPencere.setLocationRelativeTo(this);

        JPanel barPanel = new JPanel(new GridLayout(1, 4, 15, 10));
        String[] secenekler = {"A", "B", "C", "D"};

        for (int i = 0; i < 4; i++) {
            JPanel dikeyPanel = new JPanel();
            dikeyPanel.setLayout(new BorderLayout());

            JProgressBar bar = new JProgressBar(SwingConstants.VERTICAL);
            bar.setMinimum(0);
            bar.setMaximum(100);
            bar.setValue(yuzdeler[i]);
            bar.setStringPainted(false);
            bar.setForeground(i == dogruIndex ? Color.GREEN.darker() : Color.GRAY);
            bar.setPreferredSize(new Dimension(50, 250));

            JLabel yuzdeLabel = new JLabel(yuzdeler[i] + "%", JLabel.CENTER);
            yuzdeLabel.setFont(new Font("Arial", Font.BOLD, 14));
            yuzdeLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

            JLabel secenekLabel = new JLabel(secenekler[i], JLabel.CENTER);
            secenekLabel.setFont(new Font("Arial", Font.BOLD, 16));
            secenekLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

            dikeyPanel.add(bar, BorderLayout.CENTER);
            dikeyPanel.add(yuzdeLabel, BorderLayout.SOUTH);
            dikeyPanel.add(secenekLabel, BorderLayout.NORTH);

            barPanel.add(dikeyPanel);
        }

        grafikPencere.add(barPanel, BorderLayout.CENTER);
        grafikPencere.setVisible(true);

        jokerSeyirci.setBackground(new Color(200, 200, 200));
        jokerSeyirci.setForeground(Color.DARK_GRAY);
        jokerSeyirci.repaint();
    }


    private void yariYariyaJoker() {
        if (yariYariyaJokerKullanildi) {
            JOptionPane.showMessageDialog(this, "Yarı Yarıya jokeri zaten kullanıldı!");
            return;
        }

        Soru s = sorular.get(soruIndex);

        List<Character> yanlislar = new ArrayList<>();
        for (char c : new char[]{'A', 'B', 'C', 'D'}) {
            if (c != s.dogruCevap) yanlislar.add(c);
        }
        Collections.shuffle(yanlislar);
        char e1 = yanlislar.get(0);
        char e2 = yanlislar.get(1);

        Map<Character, JButton> butonlar = new HashMap<>();
        butonlar.put('A', btnA);
        butonlar.put('B', btnB);
        butonlar.put('C', btnC);
        butonlar.put('D', btnD);

        butonlar.get(e1).setEnabled(false);
        butonlar.get(e1).setBackground(Color.GRAY);
        butonlar.get(e2).setEnabled(false);
        butonlar.get(e2).setBackground(Color.GRAY);

        jokerYariYariya.setEnabled(false);
        jokerYariYariya.setBackground(new Color(200, 200, 200));
        jokerYariYariya.setForeground(Color.DARK_GRAY);
        jokerYariYariya.repaint();

        yariYariyaJokerKullanildi = true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            KimMilyonerGUI gui = new KimMilyonerGUI();
            gui.setVisible(true);
        });
    }
    class Soru {
        String soruMetni;
        String[] siklar;
        char dogruCevap;
        public Soru(String soruMetni, String[] siklar, char dogruCevap) {
            this.soruMetni = soruMetni;
            this.siklar = siklar;
            this.dogruCevap = dogruCevap;
        }
    }
}