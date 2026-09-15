import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;

public class ElGamalClient {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        // ==========================================
        // CONNECT TO SERVER
        // ==========================================

        Socket socket = new Socket("localhost", 5000);

        System.out.println("Connected to ElGamal Server.");

        DataInputStream in =
            new DataInputStream(socket.getInputStream());

        DataOutputStream out =
            new DataOutputStream(socket.getOutputStream());

        // ==========================================
        // RECEIVE PUBLIC KEY
        // ==========================================

        BigInteger q =
            new BigInteger(in.readUTF());

        BigInteger alpha =
            new BigInteger(in.readUTF());

        BigInteger YA =
            new BigInteger(in.readUTF());

        System.out.println("\nPublic Key received:");
        System.out.println("q = " + q);
        System.out.println("alpha = " + alpha);
        System.out.println("YA = " + YA);

        // ==========================================
        // 4. ENCRYPTION - PLAINTEXT
        // ==========================================

        BigInteger M;

        while (true) {

            System.out.print(
                "\nEnter plaintext M (M < q): "
            );

            M = sc.nextBigInteger();

            if (M.compareTo(BigInteger.ZERO) < 0 ||
                M.compareTo(q) >= 0) {

                System.out.println(
                    "Error: M must satisfy 0 <= M < q."
                );
                System.out.println("Enter M again.\n");
                continue;
            }

            break;
        }

        // ==========================================
        // 5. SELECT RANDOM INTEGER k
        // ==========================================

        BigInteger k;

        while (true) {

            System.out.print(
                "Enter random integer k (k < q): "
            );

            k = sc.nextBigInteger();

            if (k.compareTo(BigInteger.ONE) < 0 ||
                k.compareTo(q) >= 0) {

                System.out.println(
                    "Error: k must satisfy 1 <= k < q."
                );
                System.out.println("Enter k again.\n");
                continue;
            }

            break;
        }

        // ==========================================
        // 6. ENCRYPTION
        // ==========================================

        // K = (YA)^k mod q
        BigInteger K = YA.modPow(k, q);

        // C1 = alpha^k mod q
        BigInteger C1 = alpha.modPow(k, q);

        // C2 = K * M mod q
        BigInteger C2 = K.multiply(M).mod(q);

        System.out.println("\n----- ENCRYPTION -----");

        System.out.println("K  = " + K);
        System.out.println("C1 = " + C1);
        System.out.println("C2 = " + C2);

        System.out.println(
            "Ciphertext = (" + C1 + ", " + C2 + ")"
        );

        // ==========================================
        // SEND C1 AND C2 TO SERVER
        // ==========================================

        out.writeUTF(C1.toString());
        out.writeUTF(C2.toString());

        System.out.println(
            "Ciphertext sent to server."
        );

        // ==========================================
        // RECEIVE DECRYPTED MESSAGE
        // ==========================================

        BigInteger decryptedM =
            new BigInteger(in.readUTF());

        System.out.println(
            "\nDecrypted Plaintext M = " + decryptedM
        );

        // Close
        in.close();
        out.close();
        socket.close();
        sc.close();
    }
}
