import java.io.*;
import java.net.*;
import java.util.*;

public class SDESClient {

    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("localhost",5000);

        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter 10-bit Key: ");
        String key = sc.next();

        System.out.print("Enter 8-bit Plaintext: ");
        String pt = sc.next();

        out.writeUTF(key);
        out.writeUTF(pt);

        String cipher = in.readUTF();

        System.out.println("Ciphertext from Server: " + cipher);

        socket.close();
        sc.close();
    }
}
