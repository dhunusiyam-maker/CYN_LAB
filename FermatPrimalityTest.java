import java.math.BigInteger;
import java.util.Scanner;

public class FermatPrimalityTest {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter p: ");
        BigInteger p = sc.nextBigInteger();

        boolean prime = true;

        // Check for every 1 <= a < p
        for (BigInteger a = BigInteger.ONE;
             a.compareTo(p) < 0;
             a = a.add(BigInteger.ONE)) {

            // Calculate a^p mod p
            BigInteger x = a.modPow(p, p);

            // Check whether a^p - a is divisible by p
            BigInteger remainder = x.subtract(a).mod(p);

            System.out.println(
                "a = " + a +
                " : (a^p - a) mod p = " + remainder
            );

            if (!remainder.equals(BigInteger.ZERO)) {
                prime = false;
                break;
            }
        }

        if (prime) {
            System.out.println("\n" + p + " is PRIME");
        } else {
            System.out.println("\n" + p + " is NOT PRIME");
        }

        sc.close();
    }
} socket
