import java.util.Scanner;

public class ElGamal {

    // Calculate (base^exp) mod q
    static long powerMod(long base, long exp, long q) {
        long result = 1;

        for (int i = 0; i < exp; i++) {
            result = (result * base) % q;
        }

        return result;
    }

    // Calculate modular inverse of K
    static long inverse(long K, long q) {
        for (long i = 1; i < q; i++) {
            if ((K * i) % q == 1) {
                return i;
            }
        }

        return -1;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // q = prime
        System.out.print("Enter q: ");
        long q = sc.nextLong();

        // a < q
        System.out.print("Enter a: ");
        long a = sc.nextLong();

        // XA < q - 1
        System.out.print("Enter XA (private key): ");
        long XA = sc.nextLong();

        // YA = a^XA mod q
        long YA = powerMod(a, XA, q);

        System.out.println("YA (public key) = " + YA);

        // Message
        System.out.print("Enter message M: ");
        long M = sc.nextLong();

        // Random k
        System.out.print("Enter k: ");
        long k = sc.nextLong();

        // K = YA^k mod q
        long K = powerMod(YA, k, q);

        // C1 = a^k mod q
        long C1 = powerMod(a, k, q);

        // C2 = K * M mod q
        long C2 = (K * M) % q;

        System.out.println("\n--- Encryption ---");
        System.out.println("K  = " + K);
        System.out.println("C1 = " + C1);
        System.out.println("C2 = " + C2);

        // ----- Decryption -----

        // K = C1^XA mod q
        long K2 = powerMod(C1, XA, q);

        // K^-1 mod q
        long KInverse = inverse(K2, q);

        // M = C2 * K^-1 mod q
        long decryptedM = (C2 * KInverse) % q;

        System.out.println("\n--- Decryption ---");
        System.out.println("K = " + K2);
        System.out.println("K^-1 = " + KInverse);
        System.out.println("Decrypted M = " + decryptedM);

        sc.close();
    }
}
