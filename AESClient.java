import java.io.*;
import java.net.*;
import java.util.Scanner;

public class AESClient {

    public static void main(String[] args) {

        try {
            Socket socket = new Socket("localhost", 5000);

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            Scanner sc = new Scanner(System.in);

            System.out.println("1. Encryption");
            System.out.println("2. Decryption");
            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            dos.writeInt(choice);

            if (choice == 1) {

                System.out.print("Enter Plain Text: ");
                String plaintext = sc.nextLine();

                dos.writeUTF(plaintext);

                String cipherText = dis.readUTF();
                String secretKey = dis.readUTF();

                System.out.println("\nEncrypted Text: " + cipherText);
                System.out.println("Secret Key    : " + secretKey);

            } else if (choice == 2) {

                System.out.print("Enter Cipher Text: ");
                String cipherText = sc.nextLine();

                System.out.print("Enter Secret Key: ");
                String secretKey = sc.nextLine();

                dos.writeUTF(cipherText);
                dos.writeUTF(secretKey);

                String plainText = dis.readUTF();

                System.out.println("\nDecrypted Text: " + plainText);
            }

            sc.close();
            dis.close();
            dos.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
