import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;

public class DiffieHellmanServer {

    static boolean isPrime(BigInteger q) {
        return q.isProbablePrime(100);
    }

    static boolean isPrimitiveRoot(BigInteger a, BigInteger q) {

        BigInteger phi = q.subtract(BigInteger.ONE);
        BigInteger temp = phi;

        for (BigInteger i = BigInteger.TWO;
             i.multiply(i).compareTo(temp) <= 0;
             i = i.add(BigInteger.ONE)) {

            if (temp.mod(i).equals(BigInteger.ZERO)) {

                if (a.modPow(phi.divide(i), q)
                        .equals(BigInteger.ONE)) {
                    return false;
                }

                while (temp.mod(i).equals(BigInteger.ZERO)) {
                    temp = temp.divide(i);
                }
            }
        }

        if (temp.compareTo(BigInteger.ONE) > 0) {

            if (a.modPow(phi.divide(temp), q)
                    .equals(BigInteger.ONE)) {
                return false;
            }
        }

        return true;
    }

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        BigInteger q;
        BigInteger a;

        // ---------------- GLOBAL PARAMETERS ----------------

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

        while (true) {

            System.out.print("Enter primitive root a: ");
            a = sc.nextBigInteger();

            if (a.compareTo(BigInteger.ONE) < 0 ||
                a.compareTo(q) >= 0) {

                System.out.println(
                    "Error: a must satisfy 1 <= a < q."
                );

                System.out.println("Enter a again.\n");
                continue;
            }

            if (!isPrimitiveRoot(a, q)) {

                System.out.println(
                    "Error: a is not a primitive root of q."
                );

                System.out.println("Enter a again.\n");
                continue;
            }

            break;
        }

        System.out.println("\n----- GLOBAL PARAMETERS -----");
        System.out.println("q = " + q);
        System.out.println("a = " + a);


        // ---------------- USER A ----------------

        BigInteger XA;

        while (true) {

            System.out.print(
                "\nUser A - Enter private key XA (XA < q): "
            );

            XA = sc.nextBigInteger();

            if (XA.compareTo(BigInteger.ONE) < 0 ||
                XA.compareTo(q) >= 0) {

                System.out.println(
                    "Error: XA must satisfy 1 <= XA < q."
                );

                System.out.println("Enter XA again.\n");
                continue;
            }

            break;
        }

        // YA = a^XA mod q
        BigInteger YA = a.modPow(XA, q);

        System.out.println("User A public key YA = " + YA);


        // ---------------- SOCKET ----------------

        ServerSocket serverSocket = new ServerSocket(5000);

        System.out.println("\nWaiting for User B...");

        Socket socket = serverSocket.accept();

        System.out.println("User B connected.");


        DataInputStream in =
            new DataInputStream(socket.getInputStream());

        DataOutputStream out =
            new DataOutputStream(socket.getOutputStream());


        // Send global parameters and User A public key
        out.writeUTF(q.toString());
        out.writeUTF(a.toString());
        out.writeUTF(YA.toString());


        // Receive User B public key
        BigInteger YB =
            new BigInteger(in.readUTF());

        System.out.println(
            "User B public key YB = " + YB
        );


        // ---------------- SHARED KEY ----------------

        // User A:
        // K = (YB)^XA mod q
        BigInteger KA = YB.modPow(XA, q);

        System.out.println("\n----- SHARED KEY -----");

        System.out.println(
            "Key calculated by User A = " + KA
        );


        // Send User A's shared key for verification
        out.writeUTF(KA.toString());


        // Receive User B's shared key
        BigInteger KB =
            new BigInteger(in.readUTF());

        System.out.println(
            "Key calculated by User B = " + KB
        );

        if (KA.equals(KB)) {

            System.out.println(
                "Shared Secret Key = " + KA
            );

        } else {

            System.out.println(
                "Error: Shared keys do not match."
            );
        }


        in.close();
        out.close();
        socket.close();
        serverSocket.close();
        sc.close();
    }
}
