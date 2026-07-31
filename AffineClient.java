import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class AffineClient {
    static int modInverse(int a) {
        for (int i = 1; i < 26; i++) {
            if ((a * i) % 26 == 1)
                return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5001;

        try (Scanner sc = new Scanner(System.in);
             Socket socket = new Socket(host, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connected to the Affine Cipher Server.");

            System.out.print("Enter plaintext (lowercase only): ");
            String input = sc.nextLine();
            char[] text = input.toCharArray();

            System.out.print("Enter value of a: ");
            int a = sc.nextInt();

            System.out.print("Enter value of b: ");
            int b = sc.nextInt();

            if (modInverse(a) == -1) {
                System.out.println("Invalid value of a. It must be coprime to 26.");
                return;
            }

            char[] cipher = new char[text.length];
            for (int i = 0; i < text.length; i++) {
                if (Character.isLetter(text[i])) {
                    int x = text[i] - 'a';
                    int e = (a * x + b) % 26;
                    cipher[i] = (char) (e + 'a');
                } else {
                    cipher[i] = text[i]; // Leave spaces/punctuation intact
                }
            }

            String cipherText = new String(cipher);
            System.out.println("Locally Encrypted Text: " + cipherText);
            System.out.println("Sending keys and ciphertext to server...");

            writer.println(a);
            writer.println(b);
            writer.println(cipherText);

            System.out.println("Data sent successfully!");

        } catch (Exception e) {
            System.out.println("Client Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
