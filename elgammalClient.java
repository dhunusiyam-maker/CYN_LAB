import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    static long powerMod(long base, long exp, long q) {
        long result = 1;

        for (int i = 0; i < exp; i++) {
            result = (result * base) % q;
        }

        return result;
    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        Socket socket = new Socket("localhost", 5000);

        DataInputStream in =
                new DataInputStream(socket.getInputStream());

        DataOutputStream out =
                new DataOutputStream(socket.getOutputStream());

        // Receive public key
        long q = in.readLong();
        long a = in.readLong();
        long YA = in.readLong();

        System.out.println("q = " + q);
        System.out.println("a = " + a);
        System.out.println("YA = " + YA);

        // Enter message
        System.out.print("Enter message M: ");
        long M = sc.nextLong();

        // Choose random k
        System.out.print("Enter k: ");
        long k = sc.nextLong();

        // K = YA^k mod q
        long K = powerMod(YA, k, q);

        // C1 = a^k mod q
        long C1 = powerMod(a, k, q);

        // C2 = K * M mod q
        long C2 = (K * M) % q;

        System.out.println("K = " + K);
        System.out.println("C1 = " + C1);
        System.out.println("C2 = " + C2);

        // Send ciphertext
        out.writeLong(C1);
        out.writeLong(C2);

        socket.close();
        sc.close();
    }
}
