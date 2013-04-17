package cs.gonzaga.ciphermachine.ciphers;

public class CaesarShift {
	public static String encrypt(String plaintext, int shift) {
    	char[] ciphertext = plaintext.toCharArray();
    	for (int i = 0; i < ciphertext.length; i++) {
    		if (Character.getType(ciphertext[i]) == Character.LOWERCASE_LETTER) {
    			ciphertext[i] = (char) (((ciphertext[i] - (97-shift)) % 26) + 97);
    		}
    		if ((Character.getType(ciphertext[i]) == Character.UPPERCASE_LETTER)) {
    			ciphertext[i] = (char) (((ciphertext[i] - (65-shift)) % 26) + 65);
    		}
    	}
    	return String.copyValueOf(ciphertext);
	}
}
