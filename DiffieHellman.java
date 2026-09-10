import java.util.Scanner;

public class DiffieHellman {

    static long powerMod(long a, long x, long p) {
        long result = 1;

        for (int i = 0; i < x; i++) {
            result = (result * a) % p;
        }

        return result;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter p: ");
        long p = sc.nextLong();

        System.out.print("Enter a: ");
        long a = sc.nextLong();

        System.out.print("Enter XA (Alice private key): ");
        long XA = sc.nextLong();

        System.out.print("Enter XB (Bob private key): ");
        long XB = sc.nextLong();

        // Calculate public keys
        long YA = powerMod(a, XA, p);
        long YB = powerMod(a, XB, p);

        // Calculate shared keys
        long KA = powerMod(YB, XA, p);
        long KB = powerMod(YA, XB, p);

        System.out.println("\nAlice Public Key YA = " + YA);
        System.out.println("Bob Public Key YB = " + YB);

        System.out.println("Alice Shared Key = " + KA);
        System.out.println("Bob Shared Key = " + KB);

        if (KA == KB) {
            System.out.println("Shared Key = " + KA);
        }

        sc.close();
    }
}
