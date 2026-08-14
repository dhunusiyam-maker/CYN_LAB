import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Scanner;

public class AES{

    // Encrypt method
    public static String encrypt(String plainText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Decrypt method
    public static String decrypt(String cipherText, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes);
    }

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);

            // Generate AES Key (128-bit)
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(128);
            SecretKey secretKey = keyGenerator.generateKey();

            System.out.println("1. Encryption");
            System.out.println("2. Decryption");
            System.out.print("Enter Choice: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                System.out.print("Enter Plain Text: ");
                String plainText = sc.nextLine();

                String encryptedText = encrypt(plainText, secretKey);

                System.out.println("\nGenerated AES Key (Base64):");
                System.out.println(Base64.getEncoder().encodeToString(secretKey.getEncoded()));

                System.out.println("\nEncrypted Text:");
                System.out.println(encryptedText);

                // Demonstration
                String decryptedText = decrypt(encryptedText, secretKey);
                System.out.println("\nDecrypted Text (Verification):");
                System.out.println(decryptedText);

            } else if (choice == 2) {

                System.out.print("Enter AES Key (Base64): ");
                String keyString = sc.nextLine();

                byte[] decodedKey = Base64.getDecoder().decode(keyString);
                SecretKey key = new javax.crypto.spec.SecretKeySpec(decodedKey, "AES");

                System.out.print("Enter Cipher Text: ");
                String cipherText = sc.nextLine();

                String decryptedText = decrypt(cipherText, key);

                System.out.println("\nDecrypted Text:");
                System.out.println(decryptedText);

            } else {
                System.out.println("Invalid Choice.");
            }

            sc.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
