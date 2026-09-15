import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;

public class RSAServer {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        // Step 1: Select two prime numbers
        System.out.print("Enter prime number p: ");
        BigInteger p = sc.nextBigInteger();

        System.out.print("Enter prime number q: ");
        BigInteger q = sc.nextBigInteger();

        // Step 2: Calculate n = p * q
        BigInteger n = p.multiply(q);

        // Step 3: Calculate phi(n)
        BigInteger phi = p.subtract(BigInteger.ONE)
                           .multiply(q.subtract(BigInteger.ONE));

        System.out.println("n = " + n);
        System.out.println("phi(n) = " + phi);

        // Step 4: Select e
        BigInteger e;

        while (true) {
            System.out.print("Enter encryption key e: ");
            e = sc.nextBigInteger();

            if (e.compareTo(BigInteger.ONE) <= 0 ||
                e.compareTo(phi) >= 0) {

                System.out.println(
                    "Error: e must satisfy 1 < e < phi(n)."
                );
                continue;
            }

            if (!e.gcd(phi).equals(BigInteger.ONE)) {

                System.out.println(
                    "Error: e and phi(n) are not coprime."
                );

                System.out.println(
                    "gcd(e, phi(n)) = " + e.gcd(phi)
                );

                System.out.println("Please enter e again.");
                continue;
            }

            break;
        }

        // Step 5 & 6: Calculate d
        BigInteger d = e.modInverse(phi);

        System.out.println("\nPublic Key  = {" + e + ", " + n + "}");
        System.out.println("Private Key = {" + d + ", " + n + "}");

        // Start server
        ServerSocket serverSocket = new ServerSocket(5000);

        System.out.println("\nServer started...");
        System.out.println("Waiting for client...");

        Socket socket = serverSocket.accept();

        System.out.println("Client connected!");

        // Input and output streams
        DataInputStream in = new DataInputStream(
                socket.getInputStream());

        DataOutputStream out = new DataOutputStream(
                socket.getOutputStream());

        // Send public key to client
        out.writeUTF(e.toString());
        out.writeUTF(n.toString());

        System.out.println("Public key sent to client.");

        // Receive encrypted message
        String encryptedMessage = in.readUTF();

        BigInteger c = new BigInteger(encryptedMessage);

        System.out.println("Encrypted message received: " + c);

        // Decryption
        BigInteger decrypted = c.modPow(d, n);

        System.out.println("Decrypted message: " + decrypted);

        // Send decrypted message back to client
        out.writeUTF(decrypted.toString());

        // Close connections
        in.close();
        out.close();
        socket.close();
        serverSocket.close();
        sc.close();
    }
}
