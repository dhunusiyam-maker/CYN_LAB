pip install pycryptodome
from Crypto.Cipher import DES
from Crypto.Util.Padding import pad, unpad
import binascii

def encrypt(plaintext, key):
    cipher = DES.new(key, DES.MODE_ECB)
    padded_text = pad(plaintext, 8)
    ciphertext = cipher.encrypt(padded_text)
    return ciphertext

def decrypt(ciphertext, key):
    cipher = DES.new(key, DES.MODE_ECB)
    decrypted = cipher.decrypt(ciphertext)
    return unpad(decrypted, 8)

def main():
    key = input("Enter 8-character key: ").encode()

    if len(key) != 8:
        print("Key must be exactly 8 characters.")
        return

    plaintext = input("Enter plaintext: ").encode()

    ciphertext = encrypt(plaintext, key)

    print("\nCiphertext (Hex):", binascii.hexlify(ciphertext).decode())

    decrypted = decrypt(ciphertext, key)

    print("Decrypted Text:", decrypted.decode())

if __name__ == "__main__":
    main()
