import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class HillServer {
    static int modInverse(int n) {
        n = (n % 26 + 26) % 26;
        for (int i = 1; i < 26; i++) {
            if ((n * i) % 26 == 1) return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        int port = 5002;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Hill Server running. Waiting for connection...");

            try (Socket socket = serverSocket.accept()) {
                System.out.println("Client connected!");
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                int[][] K = new int[2][2];
                K[0][0] = Integer.parseInt(reader.readLine());
                K[0][1] = Integer.parseInt(reader.readLine());
                K[1][0] = Integer.parseInt(reader.readLine());
                K[1][1] = Integer.parseInt(reader.readLine());

                String cipherText = reader.readLine();

                System.out.println("\n--- Received Data ---");
                System.out.println("Matrix K: [["+K[0][0]+", "+K[0][1]+"], ["+K[1][0]+", "+K[1][1]+"]]");
                System.out.println("Ciphertext: " + cipherText);

                int det = (K[0][0] * K[1][1] - K[0][1] * K[1][0]) % 26;
                int detInverse = modInverse(det);

                if (detInverse == -1) {
                    System.out.println("Error: Key matrix is not invertible. Decryption impossible.");
                    return;
                }

                int[][] K_inv = new int[2][2];
                K_inv[0][0] = (K[1][1] * detInverse) % 26;
                K_inv[0][1] = (-K[0][1] * detInverse) % 26;
                K_inv[1][0] = (-K[1][0] * detInverse) % 26;
                K_inv[1][1] = (K[0][0] * detInverse) % 26;

                for(int i=0; i<2; i++) {
                    for(int j=0; j<2; j++) {
                        K_inv[i][j] = (K_inv[i][j] % 26 + 26) % 26;
                    }
                }

                StringBuilder plainText = new StringBuilder();
                char[] chars = cipherText.toCharArray();

                for (int i = 0; i < chars.length; i += 2) {
                    int c1 = chars[i] - 'a';
                    int c2 = chars[i+1] - 'a';

                    int p1 = (K_inv[0][0] * c1 + K_inv[0][1] * c2) % 26;
                    int p2 = (K_inv[1][0] * c1 + K_inv[1][1] * c2) % 26;

                    plainText.append((char) (p1 + 'a'));
                    plainText.append((char) (p2 + 'a'));
                }

                System.out.println("Decrypted Text: " + plainText.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
