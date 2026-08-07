import java.io.*;
import java.net.*;

public class ColumnarServer {

    // Encryption
    static String encrypt(String text, String key) {

        int cols = key.length();
        int rows = (int) Math.ceil((double) text.length() / cols);

        char[][] matrix = new char[rows][cols];

        int k = 0;

        // Fill matrix row-wise
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (k < text.length())
                    matrix[i][j] = text.charAt(k++);
                else
                    matrix[i][j] = 'X';
            }
        }

        String cipher = "";

        // Read columns according to alphabetical order of key
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            for (int j = 0; j < cols; j++) {
                if (key.charAt(j) == ch) {
                    for (int i = 0; i < rows; i++) {
                        cipher += matrix[i][j];
                    }
                }
            }
        }

        return cipher;
    }

    // Decryption
    static String decrypt(String cipher, String key) {

        int cols = key.length();
        int rows = cipher.length() / cols;

        char[][] matrix = new char[rows][cols];

        int k = 0;

        // Fill columns according to alphabetical order of key
        for (char ch = 'A'; ch <= 'Z'; ch++) {
            for (int j = 0; j < cols; j++) {
                if (key.charAt(j) == ch) {
                    for (int i = 0; i < rows; i++) {
                        matrix[i][j] = cipher.charAt(k++);
                    }
                }
            }
        }

        String plain = "";

        // Read row-wise
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                plain += matrix[i][j];
            }
        }

        return plain;
    }

    public static void main(String[] args) throws Exception {

        ServerSocket ss = new ServerSocket(5000);
        System.out.println("Server Waiting...");

        Socket s = ss.accept();
        System.out.println("Client Connected");

        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        String text = dis.readUTF().toUpperCase();
        String key = dis.readUTF().toUpperCase();
        int choice = dis.readInt();

        if (choice == 1) {
            String cipher = encrypt(text, key);
            dos.writeUTF(cipher);
            System.out.println("Encrypted Text : " + cipher);
        } else if (choice == 2) {
            String plain = decrypt(text, key);
            dos.writeUTF(plain);
            System.out.println("Decrypted Text : " + plain);
        } else {
            dos.writeUTF("Invalid Choice");
        }

        dis.close();
        dos.close();
        s.close();
        ss.close();
    }
}