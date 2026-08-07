import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ColumnarClient {

    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("localhost", 5000);

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

        Scanner sc = new Scanner(System.in);

        System.out.println("1. Encryption");
        System.out.println("2. Decryption");
        System.out.print("Enter Choice: ");
        int choice = sc.nextInt();

        System.out.print("Enter Text (without spaces): ");
        String text = sc.next().toUpperCase();

        System.out.print("Enter Key (uppercase): ");
        String key = sc.next().toUpperCase();

        // Send data to server
        dos.writeUTF(text);
        dos.writeUTF(key);
        dos.writeInt(choice);
        dos.flush();

        // Receive result from server
        String result = dis.readUTF();

        if (choice == 1) {
            System.out.println("Encrypted Text : " + result);
        } else if (choice == 2) {
            System.out.println("Decrypted Text : " + result);
        } else {
            System.out.println(result);
        }

        sc.close();
        dis.close();
        dos.close();
        socket.close();
    }
}