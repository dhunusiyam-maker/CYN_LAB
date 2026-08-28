import java.io.*;
import java.net.*;
import java.math.BigInteger;

public class RSAServer {

    public static void main(String[] args) throws Exception {

        ServerSocket ss = new ServerSocket(5000);
        System.out.println("Server waiting for connection...");

        Socket s = ss.accept();
        System.out.println("Client connected.");

        // RSA keys
        BigInteger p = BigInteger.valueOf(61);
        BigInteger q = BigInteger.valueOf(53);

        BigInteger n = p.multiply(q);              // n = 3233
        BigInteger phi = p.subtract(BigInteger.ONE)
                          .multiply(q.subtract(BigInteger.ONE));

        BigInteger e = BigInteger.valueOf(17);
        BigInteger d = e.modInverse(phi);          // d = 2753

        DataInputStream in = new DataInputStream(s.getInputStream());

        String encryptedText = in.readUTF();

        // Convert ciphertext back to number
        BigInteger cipher = new BigInteger(encryptedText);

        // Decryption: M = C^d mod n
        BigInteger message = cipher.modPow(d, n);

        System.out.println("Encrypted message: " + cipher);
        System.out.println("Decrypted message: " + message);

        s.close();
        ss.close();
    }
}
