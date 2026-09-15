import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;

public class RSAClient {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        // Connect to server
        Socket socket = new Socket("localhost", 5000);

        System.out.println("Connected to RSA Server.");

        // Input and output streams
        DataInputStream in = new DataInputStream(
                socket.getInputStream());

        DataOutputStream out = new DataOutputStream(
                socket.getOutputStream());

        // Receive public key
        BigInteger e = new BigInteger(in.readUTF());
        BigInteger n = new BigInteger(in.readUTF());

        System.out.println("Public Key received:");
        System.out.println("e = " + e);
        System.out.println("n = " + n);

        // Enter message
        System.out.print("\nEnter message m: ");
        BigInteger m = sc.nextBigInteger();

        // Check message range
        if (m.compareTo(BigInteger.ZERO) < 0 ||
            m.compareTo(n) >= 0) {

            System.out.println(
                "Error: Message must satisfy 0 <= m < n."
            );

            socket.close();
            sc.close();
            return;
        }

        // Encryption
        BigInteger c = m.modPow(e, n);

        System.out.println("Encrypted message: " + c);

        // Send encrypted message to server
        out.writeUTF(c.toString());

        System.out.println("Encrypted message sent to server.");

        // Receive decrypted message
        BigInteger decrypted = new BigInteger(in.readUTF());

        System.out.println(
            "Decrypted message from server: " + decrypted
        );

        // Close connections
        in.close();
        out.close();
        socket.close();
        sc.close();
    }
}
