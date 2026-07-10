import java.util.Scanner;

public class gcd{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter first number: ");
        int a = sc.nextInt();

        System.out.print("Enter second number: ");
        int b = sc.nextInt();

        // Convert negative numbers to positive
        if (a < 0)
            a = -a;

        if (b < 0)
            b = -b;

        // Euclidean Algorithm
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }

        System.out.println("GCD = " + a);

        sc.close();
    }
}