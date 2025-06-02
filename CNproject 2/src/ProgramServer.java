import java.io.*;
import java.net.*;
import java.util.*;

public class ProgramServer {
    private static final int PORT = 4340;
    private static final String JOKER_HOST = "127.0.0.1";
    private static final int JOKER_PORT = 4340;

    private static List<Question> questions = new ArrayList<>();
    private static Map<Integer, String> rewardMessages = new HashMap<>();

    public static void main(String[] args) {
        loadQuestions();
        loadRewards();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Program Sunucusu dinleniyor (port " + PORT + ")...");

            while (true) {
                Socket playerSocket = serverSocket.accept();
                System.out.println("Yeni yarışmacı bağlandı: " + playerSocket.getInetAddress());

                handlePlayer(playerSocket);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadQuestions() {
        questions.add(new Question("Python hangi yıl geliştirilmiştir?",
                new String[]{"A) 1991", "B) 2000", "C) 1989", "D) 2010"}, "A"));
        questions.add(new Question("Java'nın yaratıcısı kimdir?",
                new String[]{"A) Guido van Rossum", "B) James Gosling", "C) Bjarne Stroustrup", "D) Dennis Ritchie"}, "B"));
        questions.add(new Question("HTML bir programlama dili midir?",
                new String[]{"A) Evet", "B) Hayır", "C) Kısmen", "D) Bilmiyorum"}, "B"));
        questions.add(new Question("En hızlı hayvan hangisidir?",
                new String[]{"A) Çita", "B) Kartal", "C) At", "D) Kanguru"}, "A"));
        questions.add(new Question("Dünyanın en büyük okyanusu hangisidir?",
                new String[]{"A) Hint Okyanusu", "B) Atlantik Okyanusu", "C) Pasifik Okyanusu", "D) Arktik Okyanusu"}, "C"));
    }

    private static void loadRewards() {
        rewardMessages.put(0, "Linç Yükleniyor");
        rewardMessages.put(1, "Önemli olan katılmaktı");
        rewardMessages.put(2, "İki birden büyüktür");
        rewardMessages.put(3, "Buralara kolay gelmedik");
        rewardMessages.put(4, "Sen bu işi biliyorsun");
        rewardMessages.put(5, "Harikasın");
    }

    private static void handlePlayer(Socket playerSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
                PrintWriter out = new PrintWriter(playerSocket.getOutputStream(), true);
                Socket jokerSocket = new Socket(JOKER_HOST, JOKER_PORT);
                BufferedReader jokerIn = new BufferedReader(new InputStreamReader(jokerSocket.getInputStream()));
                PrintWriter jokerOut = new PrintWriter(jokerSocket.getOutputStream(), true);
        ) {
            int currentQuestionIndex = 0;
            int correctCount = 0;
            int jokerCount = 0;

            while (currentQuestionIndex < questions.size()) {
                Question q = questions.get(currentQuestionIndex);
                String jokersAvailable = "Seyirciye Sorma (S), Yarı Yarıya (Y)";
                out.println((currentQuestionIndex + 1) + ". Soru: \"" + q.getQuestion() + "\"");
                out.println("Şıklar: " + String.join(" ", q.getOptions()));
                out.println("Jokerler: " + jokersAvailable);
                out.println("Cevabınızı girin (A, B, C, D) veya Joker kullanın (S, Y):");

                String response = in.readLine().trim().toUpperCase();

                if (response.equals("S") && jokerCount < 2) {
                    jokerOut.println("SEYIRCI");
                    String jokerResponse = jokerIn.readLine();
                    out.println(q.getQuestion());
                    out.println("Şıklar: " + jokerResponse);
                    out.println("Cevabınızı girin (A, B, C, D):");
                    response = in.readLine().trim().toUpperCase();
                    jokerCount++;
                } else if (response.equals("Y") && jokerCount < 2) {
                    jokerOut.println("YARI:" + q.getAnswer());
                    String jokerResponse = jokerIn.readLine();
                    out.println(q.getQuestion());
                    out.println("Şıklar: " + jokerResponse);
                    out.println("Cevabınızı girin (" + jokerResponse.replace(" ", ", ") + "):");
                    response = in.readLine().trim().toUpperCase();
                    jokerCount++;
                }

                if (response.equals(q.getAnswer())) {
                    correctCount++;
                    currentQuestionIndex++;
                    if (currentQuestionIndex == questions.size()) {
                        out.println("Tebrikler! " + rewardMessages.get(correctCount));
                        break;
                    }
                } else {
                    out.println("Yanlış cevap! " + rewardMessages.get(correctCount));
                    break;
                }
            }

            playerSocket.close();
            jokerSocket.close();
            System.out.println("Yarışma sona erdi.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class Question {
    private String question;
    private String[] options;
    private String answer;

    public Question(String question, String[] options, String answer) {
        this.question = question;
        this.options = options;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getOptions() {
        return options;
    }

    public String getAnswer() {
        return answer;
    }
}
