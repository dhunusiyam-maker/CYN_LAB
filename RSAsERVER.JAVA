import java.io.*;
import java.net.*;

public class Server {

    static long powerMod(long base, long exp, long mod) {
        long result = 1;

        for (int i = 0; i < exp; i++) {
            result = (result * base) % mod;
        }

        return result;
    }

    static long inverse(long e, long phi) {
        for (long d = 1; d < phi; d++) {
            if ((e * d) % phi == 1) {
                return d;
            }
        }
        return -1;
    }

    public static void main(String[] args) throws Exception {

        ServerSocket ss = new ServerSocket(5000);

        System.out.println("Server waiting...");

        Socket socket = ss.accept();

        DataInputStream in =
                new DataInputStream(socket.getInputStream());

        DataOutputStream out =
                new DataOutputStream(socket.getOutputStream());

        // RSA values
        long p = 3;
        long q = 11;

        long n = p * q;
        long phi = (p - 1) * (q - 1);

        long e = 3;
        long d = inverse(e, phi);

        System.out.println("p = " + p);
        System.out.println("q = " + q);
        System.out.println("n = " + n);
        System.out.println("phi = " + phi);
        System.out.println("Public Key = (" + e + ", " + n + ")");
        System.out.println("Private Key = (" + d + ", " + n + ")");

        // Send public key to client
        out.writeLong(e);
        out.writeLong(n);

        // Receive encrypted message
        long C = in.readLong();

        System.out.println("Received Ciphertext = " + C);

        // Decryption
        long M = powerMod(C, d, n);

        System.out.println("Decrypted Message = " + M);

        socket.close();
        ss.close();
    }
}
