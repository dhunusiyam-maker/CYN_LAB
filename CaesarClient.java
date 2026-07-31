import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class CaesarClient {
    public static void main(String[] args) {
        String host = "127.0.0.1"; 
        int port = 5000;

        try (Scanner scanner = new Scanner(System.in);
             Socket socket = new Socket(host, port)) {

            System.out.println("Connected to the Caesar Cipher Server.");


            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());


            System.out.print("Enter plain text to encrypt: ");
            String plainText = scanner.nextLine();

            System.out.print("Enter shift key integer value: ");
            int shiftKey = scanner.nextInt();


            output.writeUTF(plainText);
            output.writeInt(shiftKey);
            output.flush(); // Ensure data bytes leave buffer immediately


            String encryptedText = input.readUTF();
            System.out.println("Server Response (Ciphertext): " + encryptedText);

        } catch (IOException e) {
            System.err.println("Client Exception: " + e.getMessage());
        }
    }
}
