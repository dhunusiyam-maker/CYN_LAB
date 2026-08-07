import java.io.*;
import java.net.*;
import java.util.Scanner;

public class RailFenceClient {

    public static void main(String args[]) throws Exception {

        Socket s = new Socket("localhost", 5000);

        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        Scanner sc = new Scanner(System.in);

        System.out.print("1. Encryption\n2. Decryption\nEnter Choice: ");
        int choice = sc.nextInt();

        System.out.print("Enter Text: ");
        String text = sc.next();

        System.out.print("Enter Number of Rails: ");
        int key = sc.nextInt();

        dos.writeUTF(text);
        dos.writeInt(key);
        dos.writeInt(choice);

        String result = dis.readUTF();

        if (choice == 1)
            System.out.println("Encrypted Text : " + result);
        else
            System.out.println("Decrypted Text : " + result);

        sc.close();
        dis.close();
        dos.close();
        s.close();
    }
}