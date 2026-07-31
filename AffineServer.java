import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class AffineServer {
    static int modInverse(int a) {
        for (int i = 1; i < 26; i++) {
            if ((a * i) % 26 == 1)
                return i;
        }
        return -1;
    }

    public static void main(String[] args) {
        int port = 5001;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running. Waiting for a client to connect...");

            try (Socket socket = serverSocket.accept()) {
                System.out.println("Client connected!");

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                int a = Integer.parseInt(reader.readLine());
                int b = Integer.parseInt(reader.readLine());
                String cipherText = reader.readLine();

                System.out.println("\n--- Received Data ---");
                System.out.println("Key a: " + a);
                System.out.println("Key b: " + b);
                System.out.println("Ciphertext: " + cipherText);

                int inverse = modInverse(a);
                if (inverse == -1) {
                    System.out.println("Error: Received an invalid key 'a'. Decryption aborted.");
                    return;
                }

                char[] cipherChars = cipherText.toCharArray();
                char[] plainChars = new char[cipherChars.length];

                for (int i = 0; i < cipherChars.length; i++) {
                    if (Character.isLetter(cipherChars[i])) {
                        int y = cipherChars[i] - 'a';
                        
                        int d = (inverse * (y - b)) % 26;
                        if (d < 0) {
                            d += 26;
                        }
                        plainChars[i] = (char) (d + 'a');
                    } else {
                        plainChars[i] = cipherChars[i]; 
                    }
                }

                System.out.println("Decrypted Text: " + new String(plainChars));
            }
        } catch (Exception e) {
            System.out.println("Server Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
