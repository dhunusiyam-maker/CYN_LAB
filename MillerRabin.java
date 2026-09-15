import java.math.BigInteger;
import java.util.Scanner;

public class MillerRabin {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        BigInteger n;

        System.out.print("Enter number n: ");
        n = sc.nextBigInteger();

        // Check small cases
        if (n.compareTo(BigInteger.TWO) < 0) {
            System.out.println("n is composite.");
            sc.close();
            return;
        }

        if (n.equals(BigInteger.TWO)) {
            System.out.println("n is probably prime.");
            sc.close();
            return;
        }

        // 1. Find n - 1 = 2^k * m
        BigInteger nMinusOne = n.subtract(BigInteger.ONE);
        BigInteger m = nMinusOne;
        int k = 0;

        while (m.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            m = m.divide(BigInteger.TWO);
            k++;
        }

        System.out.println("\n----- STEP 1 -----");
        System.out.println("n - 1 = 2^" + k + " * " + m);


        // 2. Choose a such that 1 < a < n - 1
        BigInteger a;

        while (true) {

            System.out.print(
                "\nEnter value of a (1 < a < n-1): "
            );

            a = sc.nextBigInteger();

            if (a.compareTo(BigInteger.ONE) <= 0 ||
                a.compareTo(nMinusOne) >= 0) {

                System.out.println(
                    "Error: a must satisfy 1 < a < n-1."
                );

                System.out.println("Enter a again.\n");
                continue;
            }

            break;
        }


        // 3. Compute b0 = a^m mod n
        BigInteger b0 = a.modPow(m, n);

        System.out.println("\n----- STEP 3 -----");
        System.out.println("b0 = a^m mod n");
        System.out.println("b0 = " + b0);


        // If b0 = 1, n is probably prime
        if (b0.equals(BigInteger.ONE)) {

            System.out.println(
                "b0 = 1"
            );

            System.out.println(
                "n is probably prime."
            );

            sc.close();
            return;
        }


        // If b0 = -1 mod n, n is probably prime
        if (b0.equals(n.subtract(BigInteger.ONE))) {

            System.out.println(
                "b0 = -1 mod n"
            );

            System.out.println(
                "n is probably prime."
            );

            sc.close();
            return;
        }


        // Find bj = (bj-1)^2 mod n
        BigInteger bj = b0;

        boolean probablyPrime = false;

        for (int j = 1; j < k; j++) {

            bj = bj.multiply(bj).mod(n);

            System.out.println(
                "b" + j + " = " + bj
            );

            // If bj = -1 mod n
            if (bj.equals(n.subtract(BigInteger.ONE))) {

                probablyPrime = true;
                break;
            }

            // If bj = 1 before reaching -1
            if (bj.equals(BigInteger.ONE)) {

                probablyPrime = false;
                break;
            }
        }


        // Final result
        System.out.println("\n----- RESULT -----");

        if (probablyPrime) {

            System.out.println(
                "n is probably prime."
            );

        } else {

            System.out.println(
                "n is composite."
            );
        }

        sc.close();
    }
}
