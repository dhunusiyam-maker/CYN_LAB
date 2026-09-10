import java.util.Scanner;

public class FermatTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter p: ");
        int p = sc.nextInt();

        boolean prime = true;

        for (int a = 1; a < p; a++) {

            long x = (long) Math.pow(a, p) - a;

            if (x % p != 0) {
                prime = false;
                break;
            }
        }

        if (prime)
            System.out.println(p + " is probably prime");
        else
            System.out.println(p + " is composite");

        sc.close();
    }
}
