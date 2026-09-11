import java.io.*;
import java.net.*;

public class Server {

    static long powerMod(long a, long x, long p) {
        long result = 1;

        for (int i = 0; i < x; i++) {
            result = (result * a) % p;
        }

        return result;
    }

    public static void main(String[] args) throws Exception {

        ServerSocket ss = new ServerSocket(5000);
        System.out.println("Server waiting...");

        Socket socket = ss.accept();
        System.out.println("Client connected.");

        DataInputStream in =
                new DataInputStream(socket.getInputStream());

        DataOutputStream out =
                new DataOutputStream(socket.getOutputStream());

        // Server chooses p, a and private key XA
        long p = 23;
        long a = 5;
        long XA = 6;

        // YA = a^XA mod p
        long YA = powerMod(a, XA, p);

        System.out.println("Server Private Key XA = " + XA);
        System.out.println("Server Public Key YA = " + YA);

        // Send p, a and YA to client
        out.writeLong(p);
        out.writeLong(a);
        out.writeLong(YA);

        // Receive client's public key YB
        long YB = in.readLong();

        System.out.println("Client Public Key YB = " + YB);

        // KA = YB^XA mod p
        long KA = powerMod(YB, XA, p);

        System.out.println("Server Shared Key = " + KA);

        socket.close();
        ss.close();
    }
}
