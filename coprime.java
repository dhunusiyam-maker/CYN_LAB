import java.util.*;

public class coprime {

    // Function to find GCD
    public static int gcd(int a, int b) {

        // Handle negative numbers
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

        return a;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
	System.out.println("Enter first number:");
        int a = sc.nextInt();
	System.out.println("Enter second number:");
        int b = sc.nextInt();
	int result = gcd(a, b);

        if (result == 1)
            System.out.println("Co-Prime");
        else
            System.out.println("Not Co-Prime");

        sc.close();
    }
}