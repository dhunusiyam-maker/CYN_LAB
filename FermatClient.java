import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        Socket socket = new Socket("localhost", 5000);

        DataInputStream in =
                new DataInputStream(socket.getInputStream());

        DataOutputStream out =
                new DataOutputStream(socket.getOutputStream());

        // Enter p
        System.out.print("Enter p: ");
        int p = sc.nextInt();

        // Send p to server
        out.writeInt(p);

        // Receive result
        String result = in.readUTF();

        System.out.println("Server: " + result);

        socket.close();
        sc.close();
    }
}
