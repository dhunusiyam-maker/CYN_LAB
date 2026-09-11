import java.io.*;
import java.net.*;

public class Server {

    static long powerMod(long base, long exp, long q) {
        long result = 1;

        for (int i = 0; i < exp; i++) {
            result = (result * base) % q;
        }

        return result;
    }

    static long inverse(long K, long q) {
        for (long i = 1; i < q; i++) {
            if ((K * i) % q == 1) {
                return i;
            }
        }
        return -1;
    }

    public static void main(String[] args) throws Exception {

        ServerSocket serverSocket = new ServerSocket(5000);

        System.out.println("Server waiting...");

        Socket socket = serverSocket.accept();

        DataInputStream in =
                new DataInputStream(socket.getInputStream());

        DataOutputStream out =
                new DataOutputStream(socket.getOutputStream());

        // Public parameters
        long q = 23;
        long a = 5;

        // Server's private key
        long XA = 6;

        // Server's public key
        long YA = powerMod(a, XA, q);

        // Send public key to client
        out.writeLong(q);
        out.writeLong(a);
        out.writeLong(YA);

        // Receive ciphertext
        long C1 = in.readLong();
        long C2 = in.readLong();

        System.out.println("Received C1 = " + C1);
        System.out.println("Received C2 = " + C2);

        // K = C1^XA mod q
        long K = powerMod(C1, XA, q);

        // K^-1
        long KInverse = inverse(K, q);

        // M = C2 * K^-1 mod q
        long M = (C2 * KInverse) % q;

        System.out.println("K = " + K);
        System.out.println("K^-1 = " + KInverse);
        System.out.println("Decrypted Message = " + M);

        socket.close();
        serverSocket.close();
    }
}
