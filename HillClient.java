import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class HillClient {
    static int modInverse(int n) {
        n = (n % 26 + 26) % 26;
        for (int i = 1; i < 26; i++) {
            if ((n * i) % 26 == 1) return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        String host = "localhost";
        int port = 5002;

        try (Scanner sc = new Scanner(System.in);
             Socket socket = new Socket(host, port);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Connected to Hill Cipher Server.");

            System.out.print("Enter plaintext (lowercase only, no spaces): ");
            String plainText = sc.next();

            if (plainText.length() % 2 != 0) {
                plainText += "x";
            }

            int[][] K = new int[2][2];
            System.out.println("Enter 2x2 Key Matrix elements (row by row):");
            System.out.print("K[0][0]: "); K[0][0] = sc.nextInt();
            System.out.print("K[0][1]: "); K[0][1] = sc.nextInt();
            System.out.print("K[1][0]: "); K[1][0] = sc.nextInt();
            System.out.print("K[1][1]: "); K[1][1] = sc.nextInt();

            int det = (K[0][0] * K[1][1] - K[0][1] * K[1][0]) % 26;
            if (modInverse(det) == -1) {
                System.out.println("Invalid matrix! Determinant must be coprime to 26.");
                return;
            }

            StringBuilder cipherText = new StringBuilder();
            char[] chars = plainText.toCharArray();

            for (int i = 0; i < chars.length; i += 2) {
                int p1 = chars[i] - 'a';
                int p2 = chars[i+1] - 'a';

                int c1 = (K[0][0] * p1 + K[0][1] * p2) % 26;
                int c2 = (K[1][0] * p1 + K[1][1] * p2) % 26;

                cipherText.append((char) (c1 + 'a'));
                cipherText.append((char) (c2 + 'a'));
            }

            System.out.println("Locally Encrypted Text: " + cipherText.toString());

            writer.println(K[0][0]);
            writer.println(K[0][1]);
            writer.println(K[1][0]);
            writer.println(K[1][1]);
            writer.println(cipherText.toString());

            System.out.println("Payload transmitted successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
