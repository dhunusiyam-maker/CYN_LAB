import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;

public class ElGamalServer {

    // Check whether q is prime
    static boolean isPrime(BigInteger q) {
        return q.isProbablePrime(100);
    }

    // Check whether alpha is a primitive root modulo q
    static boolean isPrimitiveRoot(BigInteger alpha, BigInteger q) {

        BigInteger phi = q.subtract(BigInteger.ONE);
        BigInteger temp = phi;

        for (BigInteger i = BigInteger.TWO;
             i.multiply(i).compareTo(temp) <= 0;
             i = i.add(BigInteger.ONE)) {

            if (temp.mod(i).equals(BigInteger.ZERO)) {

                if (alpha.modPow(phi.divide(i), q)
                        .equals(BigInteger.ONE)) {
                    return false;
                }

                while (temp.mod(i).equals(BigInteger.ZERO)) {
                    temp = temp.divide(i);
                }
            }
        }

        if (temp.compareTo(BigInteger.ONE) > 0) {

            if (alpha.modPow(phi.divide(temp), q)
                    .equals(BigInteger.ONE)) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        // ==========================================
        // 1. GLOBAL PUBLIC SYSTEM
        // ==========================================

        BigInteger q;
        BigInteger alpha;

        // Get prime q
        while (true) {

            System.out.print("Enter prime number q: ");
            q = sc.nextBigInteger();

            if (!isPrime(q)) {
                System.out.println(
                    "Error: q must be a prime number."
                );
                System.out.println("Enter q again.\n");
                continue;
            }

            break;
        }

        // Get primitive root alpha
        while (true) {

            System.out.print("Enter primitive root alpha: ");
            alpha = sc.nextBigInteger();

            if (alpha.compareTo(BigInteger.ONE) < 0 ||
                alpha.compareTo(q) >= 0) {

                System.out.println(
                    "Error: alpha must satisfy 1 <= alpha < q."
                );
                System.out.println("Enter alpha again.\n");
                continue;
            }

            if (!isPrimitiveRoot(alpha, q)) {

                System.out.println(
                    "Error: alpha is not a primitive root modulo q."
                );
                System.out.println("Enter alpha again.\n");
                continue;
            }

            break;
        }

        System.out.println("\nGlobal Public System:");
        System.out.println("q = " + q);
        System.out.println("alpha = " + alpha);

        // ==========================================
        // 2. KEY GENERATION
        // ==========================================

        BigInteger XA;

        while (true) {

            System.out.print(
                "\nEnter private key XA (XA < q-1): "
            );

            XA = sc.nextBigInteger();

            if (XA.compareTo(BigInteger.ONE) < 0 ||
                XA.compareTo(q.subtract(BigInteger.ONE)) >= 0) {

                System.out.println(
                    "Error: XA must satisfy 1 <= XA < q-1."
                );
                System.out.println("Enter XA again.\n");
                continue;
            }

            break;
        }

        // YA = alpha^XA mod q
        BigInteger YA = alpha.modPow(XA, q);

        // ==========================================
        // 3. PUBLIC AND PRIVATE KEY
        // ==========================================

        System.out.println("\nPublic Key:");
        System.out.println(
            "PU = {" + q + ", " + alpha + ", " + YA + "}"
        );

        System.out.println("Private Key:");
        System.out.println("XA = " + XA);

        // ==========================================
        // SOCKET SERVER
        // ==========================================

        ServerSocket serverSocket = new ServerSocket(5000);

        System.out.println("\nServer started...");
        System.out.println("Waiting for client...");

        Socket socket = serverSocket.accept();

        System.out.println("Client connected!");

        DataInputStream in =
            new DataInputStream(socket.getInputStream());

        DataOutputStream out =
            new DataOutputStream(socket.getOutputStream());

        // ==========================================
        // SEND PUBLIC KEY TO CLIENT
        // ==========================================

        out.writeUTF(q.toString());
        out.writeUTF(alpha.toString());
        out.writeUTF(YA.toString());

        System.out.println("Public key sent to client.");

        // ==========================================
        // RECEIVE C1 AND C2
        // ==========================================

        BigInteger C1 =
            new BigInteger(in.readUTF());

        BigInteger C2 =
            new BigInteger(in.readUTF());

        System.out.println("\n----- RECEIVED CIPHERTEXT -----");

        System.out.println("C1 = " + C1);
        System.out.println("C2 = " + C2);

        // ==========================================
        // 7. DECRYPTION
        // ==========================================

        // K = (C1)^XA mod q
        BigInteger K2 = C1.modPow(XA, q);

        // K^-1 mod q
        BigInteger KInverse = K2.modInverse(q);

        // M = (C2 * K^-1) mod q
        BigInteger decryptedM =
            C2.multiply(KInverse).mod(q);

        System.out.println("\n----- DECRYPTION -----");

        System.out.println("K = " + K2);
        System.out.println("K^-1 = " + KInverse);

        System.out.println(
            "Plaintext M = " + decryptedM
        );

        // Send decrypted plaintext to client
        out.writeUTF(decryptedM.toString());

        // Close
        in.close();
        out.close();
        socket.close();
        serverSocket.close();
        sc.close();
    }
}
