import java.io.*;
import java.net.*;

public class RailFenceServer {

    // Encryption
    static String encrypt(String text, int key) {

        char rail[][] = new char[key][text.length()];

        for (int i = 0; i < key; i++)
            for (int j = 0; j < text.length(); j++)
                rail[i][j] = '\n';

        boolean down = false;
        int row = 0, col = 0;

        for (int i = 0; i < text.length(); i++) {

            if (row == 0 || row == key - 1)
                down = !down;

            rail[row][col++] = text.charAt(i);

            if (down)
                row++;
            else
                row--;
        }

        String cipher = "";

        for (int i = 0; i < key; i++)
            for (int j = 0; j < text.length(); j++)
                if (rail[i][j] != '\n')
                    cipher += rail[i][j];

        return cipher;
    }

    // Decryption
    static String decrypt(String cipher, int key) {

        char rail[][] = new char[key][cipher.length()];

        for (int i = 0; i < key; i++)
            for (int j = 0; j < cipher.length(); j++)
                rail[i][j] = '\n';

        boolean down = false;
        int row = 0, col = 0;

        for (int i = 0; i < cipher.length(); i++) {

            if (row == 0 || row == key - 1)
                down = !down;

            rail[row][col++] = '*';

            if (down)
                row++;
            else
                row--;
        }

        int index = 0;

        for (int i = 0; i < key; i++) {
            for (int j = 0; j < cipher.length(); j++) {
                if (rail[i][j] == '*' && index < cipher.length()) {
                    rail[i][j] = cipher.charAt(index++);
                }
            }
        }

        String plain = "";

        down = false;
        row = 0;
        col = 0;

        for (int i = 0; i < cipher.length(); i++) {

            if (row == 0 || row == key - 1)
                down = !down;

            plain += rail[row][col++];

            if (down)
                row++;
            else
                row--;
        }

        return plain;
    }

    public static void main(String args[]) throws Exception {

        ServerSocket ss = new ServerSocket(5000);
        System.out.println("Server Waiting...");

        Socket s = ss.accept();
        System.out.println("Client Connected");

        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        String text = dis.readUTF();
        int key = dis.readInt();
        int choice = dis.readInt();

        if (choice == 1) {
            String cipher = encrypt(text, key);
            dos.writeUTF(cipher);
            System.out.println("Encrypted Text : " + cipher);
        } else {
            String plain = decrypt(text, key);
            dos.writeUTF(plain);
            System.out.println("Decrypted Text : " + plain);
        }

        dis.close();
        dos.close();
        s.close();
        ss.close();
    }
}