import java.io.*;
import java.net.*;
import java.math.BigInteger;
import java.util.Scanner;

public class RSAClient {

    public static void main(String[] args) throws Exception {

        Socket s = new Socket("localhost", 5000);

        // RSA keys
        BigInteger p = BigInteger.valueOf(61);
        BigInteger q = BigInteger.valueOf(53);

        BigInteger n = p.multiply(q);              // n = 3233
        BigInteger e = BigInteger.valueOf(17);     // public key

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter message as number: ");
        BigInteger message = sc.nextBigInteger();

        // Encryption: C = M^e mod n
        BigInteger cipher = message.modPow(e, n);

        System.out.println("Original message: " + message);
        System.out.println("Encrypted message: " + cipher);

        DataOutputStream out =
                new DataOutputStream(s.getOutputStream());

        out.writeUTF(cipher.toString());
        out.flush();

        s.close();
        sc.close();
    }
}
