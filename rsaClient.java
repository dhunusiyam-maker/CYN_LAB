import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {

    static long powerMod(long base, long exp, long mod) {
        long result = 1;

        for (int i = 0; i < exp; i++) {
            result = (result * base) % mod;
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
        long e = in.readLong();
        long n = in.readLong();

        System.out.println("Received Public Key:");
        System.out.println("e = " + e);
        System.out.println("n = " + n);

        // Enter message
        System.out.print("Enter message M: ");
        long M = sc.nextLong();

        // Encryption
        long C = powerMod(M, e, n);

        System.out.println("Encrypted Message = " + C);

        // Send ciphertext
        out.writeLong(C);

        socket.close();
        sc.close();
    }
} 
