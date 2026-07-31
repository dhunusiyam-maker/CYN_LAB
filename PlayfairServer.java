import java.io.*;
import java.net.*;

public class PlayfairServer {
    static char[][] m = new char[5][5];

    static void matrix(String key) {
        boolean[] used = new boolean[26];
        key = (key.toUpperCase() + "ABCDEFGHIKLMNOPQRSTUVWXYZ").replace("J", "I");

        int k = 0;
        for (char c : key.toCharArray())
            if (Character.isLetter(c) && !used[c - 'A']) {
                used[c - 'A'] = true;
                m[k / 5][k % 5] = c;
                k++;
            }
    }

    static int[] pos(char c) {
        if (c == 'J') c = 'I';
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                if (m[i][j] == c)
                    return new int[]{i, j};
        return null;
    }

    static String process(String s, boolean encrypt) {
        s = s.toUpperCase().replace("J", "I").replaceAll("[^A-Z]", "");
        
        if (encrypt) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                sb.append(s.charAt(i));
                if (i + 1 < s.length() && s.charAt(i) == s.charAt(i + 1)) {
                    sb.append("X"); 
                }
            }
            s = sb.toString();
            if (s.length() % 2 != 0) s += "X";
        }

        StringBuilder r = new StringBuilder();

        for (int i = 0; i < s.length(); i += 2) {
            int[] a = pos(s.charAt(i));
            int[] b = pos(s.charAt(i + 1));

            if (a[0] == b[0]) {
                r.append(m[a[0]][(a[1] + (encrypt ? 1 : 4)) % 5]);
                r.append(m[b[0]][(b[1] + (encrypt ? 1 : 4)) % 5]);
            }
            else if (a[1] == b[1]) {
                r.append(m[(a[0] + (encrypt ? 1 : 4)) % 5][a[1]]);
                r.append(m[(b[0] + (encrypt ? 1 : 4)) % 5][b[1]]);
            }
            else {
                r.append(m[a[0]][b[1]]);
                r.append(m[b[0]][a[1]]);
            }
        }
        return r.toString();
    }

    public static void main(String[] args) {
        int port = 5000;
        String cipherKey = "SECRETKEY"; 
        matrix(cipherKey);

        System.out.println("Server initialized. Awaiting connection...");
        try (ServerSocket serverSocket = new ServerSocket(port);
             Socket socket = serverSocket.accept();
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println("Client node linked successfully.");
            String incomingCipher;

            while ((incomingCipher = input.readLine()) != null) {
                if (incomingCipher.equalsIgnoreCase("EXIT")) {
                    System.out.println("Client requested termination.");
                    break;
                }
                
                System.out.println("\n[Received Ciphertext]: " + incomingCipher);
                String decryptedText = process(incomingCipher, false);
                System.out.println("[Decrypted Plaintext]: " + decryptedText);
                
                output.println("Server processed: " + decryptedText);
            }

        } catch (IOException e) {
            System.err.println("Networking operational failure: " + e.getMessage());
        }
    }
}
