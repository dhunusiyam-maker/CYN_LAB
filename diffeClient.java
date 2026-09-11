import java.io.*;
import java.net.*;

public class Client {

    static long powerMod(long a, long x, long p) {
        long result = 1;

        for (int i = 0; i < x; i++) {
            result = (result * a) % p;
        }

        return result;
    }

    public static void main(String[] args) throws Exception {

        Socket socket = new Socket("localhost", 5000);

        DataInputStream in =
                new DataInputStream(socket.getInputStream());

        DataOutputStream out =
                new DataOutputStream(socket.getOutputStream());

        // Receive p, a and YA from server
        long p = in.readLong();
        long a = in.readLong();
        long YA = in.readLong();

        // Client chooses private key XB
        long XB = 15;

        // YB = a^XB mod p
        long YB = powerMod(a, XB, p);

        System.out.println("Client Private Key XB = " + XB);
        System.out.println("Client Public Key YB = " + YB);

        // Send YB to server
        out.writeLong(YB);

        // KB = YA^XB mod p
        long KB = powerMod(YA, XB, p);

        System.out.println("Client Shared Key = " + KB);

        socket.close();
    }
} 
