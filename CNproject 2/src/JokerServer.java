import java.io.*;
import java.net.*;
import java.util.*;

public class JokerServer {
    public static void main(String[] args) {
        final int PORT = 4339; // Örneğin farklı bir port
        ;

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Joker Sunucusu dinleniyor (port " + PORT + ")...");

            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleClient(socket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String request;

            while ((request = reader.readLine()) != null) {
                if (request.startsWith("SEYIRCI")) {
                    writer.println(generateAudienceHelp());
                } else if (request.startsWith("YARI")) {
                    String correct = request.split(":")[1];
                    writer.println(generateFiftyFifty(correct));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateAudienceHelp() {
        Random rand = new Random();
        int[] percentages = new int[4];
        int total = 100;
        for (int i = 0; i < 3; i++) {
            percentages[i] = rand.nextInt(total);
            total -= percentages[i];
        }
        percentages[3] = total;

        String[] options = {"A", "B", "C", "D"};
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            result.append(options[i]).append(" (%").append(percentages[i]).append(") ");
        }

        return result.toString().trim();
    }

    private static String generateFiftyFifty(String correct) {
        List<String> options = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        options.remove(correct);
        Collections.shuffle(options);
        options.remove(0); // iki yanlış kaldır
        options.add(correct);
        Collections.sort(options); // sıralı görünsün
        return String.join(" ", options);
    }
}
