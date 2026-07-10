import java.util.*;
public class Modulus
{
	public static int mod(int a, int b)
	{
		if(b==0)
		{
			System.out.println("Error:Division by zero");
			return 0;
		}
		else
		{
			return a%b;
		}
		
	}
	public static void main(String[] args)
	{
		Scanner sc= new Scanner(System.in);
		System.out.print("Enter first number:");
		int a=sc.nextInt();
		System.out.print("Enter second number:");
		int b=sc.nextInt();
		int result=mod(a,b);
		System.out.println("Remainder = " + result);
		sc.close();
	}	
}