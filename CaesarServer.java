import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class CaesarServer {

    public static String encrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();

        shift = (shift % 26 + 26) % 26; 

        for (char ch : text.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                char encrypted = (char) (((ch - 'A' + shift) % 26) + 'A');
                result.append(encrypted);
            } else if (Character.isLowerCase(ch)) {
                char encrypted = (char) (((ch - 'a' + shift) % 26) + 'a');
                result.append(encrypted);
            } else {
                result.append(ch); 
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {
        int port = 5000;
        System.out.println("Server is starting and listening on port " + port + "...");

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            Socket socket = serverSocket.accept();
            System.out.println("Client connected smoothly.");


            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());


            String plainText = input.readUTF();
            int shiftKey = input.readInt();
            System.out.println("Received Plaintext: " + plainText);
            System.out.println("Received Shift Key: " + shiftKey);

            String cipherText = encrypt(plainText, shiftKey);

            output.writeUTF(cipherText);
            System.out.println("Encrypted text sent back successfully.");


            socket.close();
        } catch (IOException e) {
            System.err.println("Server Exception: " + e.getMessage());
        }
    }
}
