import java.io.*;
import java.net.*;

public class FermatServer {

    public static void main(String[] args) throws Exception {

        ServerSocket ss = new ServerSocket(5000);

        System.out.println("Server waiting...");

        Socket socket = ss.accept();

        DataInputStream in =
                new DataInputStream(socket.getInputStream());

        DataOutputStream out =
                new DataOutputStream(socket.getOutputStream());

        // Receive p from client
        int p = in.readInt();

        boolean prime = true;

        for (int a = 1; a < p; a++) {

            long x = (long) Math.pow(a, p) - a;

            if (x % p != 0) {
                prime = false;
                break;
            }
        }

        String result;

        if (prime)
            result = p + " is probably prime";
        else
            result = p + " is composite";

        System.out.println("Received p = " + p);
        System.out.println(result);

        // Send result to client
        out.writeUTF(result);

        socket.close();
        ss.close();
    }
}
