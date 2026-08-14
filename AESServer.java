import java.io.*;
import java.net.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESServer {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            System.out.println("Server is waiting...");

            Socket socket = serverSocket.accept();
            System.out.println("Client Connected.");

            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

            int choice = dis.readInt();

            if (choice == 1) {

                String plaintext = dis.readUTF();

                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(128);
                SecretKey key = keyGen.generateKey();

                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, key);

                byte[] encrypted = cipher.doFinal(plaintext.getBytes());

                String cipherText = Base64.getEncoder().encodeToString(encrypted);
                String secretKey = Base64.getEncoder().encodeToString(key.getEncoded());

                dos.writeUTF(cipherText);
                dos.writeUTF(secretKey);

                System.out.println("Plain Text : " + plaintext);
                System.out.println("Cipher Text: " + cipherText);
                System.out.println("Secret Key : " + secretKey);

            } else if (choice == 2) {

                String cipherText = dis.readUTF();
                String secretKey = dis.readUTF();

                byte[] decodedKey = Base64.getDecoder().decode(secretKey);
                SecretKey key = new SecretKeySpec(decodedKey, "AES");

                Cipher cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, key);

                byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));

                String plainText = new String(decrypted);

                dos.writeUTF(plainText);

                System.out.println("Cipher Text : " + cipherText);
                System.out.println("Decrypted   : " + plainText);
            }

            dis.close();
            dos.close();
            socket.close();
            serverSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
