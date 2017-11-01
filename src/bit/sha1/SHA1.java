package bit.sha1;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import static sun.security.pkcs11.wrapper.Functions.toHexString;

/**
 * Třída aplikace sloužící pro vytváření hashů ze zadaného řetězce.
 * @author Lukáš Haringer
 */
public class SHA1 {

    /**
     * Vytvoří ze vstupních dat hash SHA-1 za pomoci knihovní funkce.
     *
     * @param dataIn
     * @return String hash
     */
    String hashItLibrary(byte[] dataIn) {
        byte[] hashBytes = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            hashBytes = md.digest(dataIn);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Nenalezen algoritmus");
        }
        return toHexString(hashBytes);
    }

    /**
     * Vytvoří ze vstupních dat hash SHA-1 za pomoci vlastní funkce.
     *
     * @param dataIn
     * @return String hash
     */
    String hashIt(byte[] dataIn) {
        byte[] paddedData = padTheMessage(dataIn);
        int[] H = {0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0};

        int numberOfBlocks = paddedData.length / 64;
        byte[] block = new byte[64];

        for (int i = 0; i < numberOfBlocks; i++) {
            System.arraycopy(paddedData, 64 * i, block, 0, 64);
            processTheBlock(block, H);
        }
        
        return intArrayToHexStr(H);
    }

    /**
     * Doplnění zprávy a požadované informace a doplnění nulami tak aby byla
     * délka zprávy dělitelná 512.
     *
     * @param data
     * @return byte[] data
     */
    byte[] padTheMessage(byte[] data) {
        int origLength = data.length;
        int tailLength = origLength % 64;
        int padLength;
        if ((64 - tailLength >= 9)) {
            padLength = 64 - tailLength;
        } else {
            padLength = 128 - tailLength;
        }

        byte[] thePad = new byte[padLength];
        thePad[0] = (byte) 0x80;
        long lengthInBits = origLength * 8;

        //Doplnění zprávy o její délku v bitech
        for (int cnt = 0; cnt < 8; cnt++) {
            thePad[thePad.length - 1 - cnt] = (byte) ((lengthInBits >> (8 * cnt)) & 0x00000000000000FF);
        }

        byte[] output = new byte[origLength + padLength];

        System.arraycopy(data, 0, output, 0, origLength);
        System.arraycopy(thePad, 0, output, origLength, thePad.length);

        return output;

    }

    /**
     * Zajišťuje zpracování bloku zprávy. Rozloží blok na 80 slov a provede dané iterace.
     * @param block
     * @param H 
     */
    void processTheBlock(byte[] block, int H[]) {
        int A, B, C, D, E;
        int F = 0;
        int K = 0;
        int temp;
        int[] W = new int[80];

        //Rozložení bloku na 16 slov
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 4; j++) {
                temp = (block[i * 4 + j] & 0x000000FF) << (24 - j * 8);
                W[i] = W[i] | temp;
            }
        }

        //Vytvoření 80 slov z původních 16
        for (int j = 16; j < 80; j++) {
            W[j] = rotateLeft(W[j - 3] ^ W[j - 8] ^ W[j - 14] ^ W[j - 16], 1);
        }

        A = H[0];
        B = H[1];
        C = H[2];
        D = H[3];
        E = H[4];

        for (int j = 0; j < 80; j++) {

            if (0 <= j && j <= 19) {
                F = (B & C) | ((~B) & D);
                K = 0x5A827999;
            }

            if (20 <= j && j <= 39) {
                F = B ^ C ^ D;
                K = 0x6ED9EBA1;
            }

            if (40 <= j && j <= 59) {
                F = (B & C) | (B & D) | (C & D);
                K = 0x8F1BBCDC;
            }

            if (60 <= j && j <= 79) {
                F = B ^ C ^ D;
                K = 0xCA62C1D6;
            }

            temp = rotateLeft(A, 5) + F + E + K + W[j];
            E = D;
            D = C;
            C = rotateLeft(B, 30);
            B = A;
            A = temp;

        }
        H[0] += A;
        H[1] += B;
        H[2] += C;
        H[3] += D;
        H[4] += E;
    }

    /**
     * Slouží pro bitový posun do leva o bits míst.
     * @param value hodnota pro posun
     * @param bits počet bitů pro posun
     * @return int q
     */
    int rotateLeft(int value, int bits) {
        int q = (value << bits) | (value >>> (32 - bits));
        return q;
    }

   /**
    * Slouží pro převod int na řetězec
    * @param i
    * @return String
    */
    public static String byteToUnsignedHex(int i) {
        String hex = Integer.toHexString(i);
        while (hex.length() < 8) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * Slouží pro převod pole intů na řetězec
     * @param arr
     * @return String
     */
    public static String intArrayToHexStr(int[] arr) {
        StringBuilder builder = new StringBuilder(arr.length * 8);
        for (int b : arr) {
            builder.append(byteToUnsignedHex(b));
        }
        return builder.toString();
    }
}
