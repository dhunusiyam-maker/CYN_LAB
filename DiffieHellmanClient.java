import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.Scanner;

public class DiffieHellmanClient {

    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);

        // ---------------- SOCKET ----------------

        Socket socket = new Socket("localhost", 5000);

        DataInputStream in =
            new DataInputStream(socket.getInputStream());

        DataOutputStream out =
            new DataOutputStream(socket.getOutputStream());

        System.out.println("Connected to User A.");


        // Receive global parameters
        BigInteger q =
            new BigInteger(in.readUTF());

        BigInteger a =
            new BigInteger(in.readUTF());

        // Receive User A public key
        BigInteger YA =
            new BigInteger(in.readUTF());


        System.out.println("\n----- GLOBAL PARAMETERS -----");
        System.out.println("q = " + q);
        System.out.println("a = " + a);

        System.out.println(
            "User A public key YA = " + YA
        );


        // ---------------- USER B ----------------

        BigInteger XB;

        while (true) {

            System.out.print(
                "\nUser B - Enter private key XB (XB < q): "
            );

            XB = sc.nextBigInteger();

            if (XB.compareTo(BigInteger.ONE) < 0 ||
                XB.compareTo(q) >= 0) {

                System.out.println(
                    "Error: XB must satisfy 1 <= XB < q."
                );

                System.out.println("Enter XB again.\n");
                continue;
            }

            break;
        }

        // YB = a^XB mod q
        BigInteger YB = a.modPow(XB, q);

        System.out.println(
            "User B public key YB = " + YB
        );


        // Send User B public key
        out.writeUTF(YB.toString());


        // ---------------- SHARED KEY ----------------

        // User B:
        // K = (YA)^XB mod q
        BigInteger KB = YA.modPow(XB, q);

        System.out.println("\n----- SHARED KEY -----");

        System.out.println(
            "Key calculated by User B = " + KB
        );


        // Receive User A's shared key
        BigInteger KA =
            new BigInteger(in.readUTF());

        System.out.println(
            "Key calculated by User A = " + KA
        );


        // Send User B's shared key
        out.writeUTF(KB.toString());


        if (KA.equals(KB)) {

            System.out.println(
                "Shared Secret Key = " + KB
            );

        } else {

            System.out.println(
                "Error: Shared keys do not match."
            );
        }


        in.close();
        out.close();
        socket.close();
        sc.close();
    }
}
