import java.io.*;
import java.net.*;
import java.util.Scanner;

public class PlayerClient {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 4337;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in);
        ) {
            System.out.println("Yarışmaya Hoşgeldiniz!");

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);

                if (line.trim().endsWith(":") || line.trim().endsWith("):")) {
                    String answer = scanner.nextLine().trim().toUpperCase();
                    while (!isValidInput(answer)) {
                        System.out.println("Geçersiz giriş! Lütfen A, B, C, D, S veya Y girin:");
                        answer = scanner.nextLine().trim().toUpperCase();
                    }
                    out.println(answer);
                }
            }

            System.out.println("Oyun sona erdi. Teşekkürler!");

        } catch (IOException e) {
            System.out.println("Sunucuya bağlanırken hata oluştu: " + e.getMessage());
        }
    }

    private static boolean isValidInput(String input) {
        return input.equals("A") || input.equals("B") || input.equals("C") || input.equals("D")
                || input.equals("S") || input.equals("Y");
    }
}
