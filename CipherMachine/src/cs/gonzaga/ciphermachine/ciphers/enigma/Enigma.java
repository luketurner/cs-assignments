package cs.gonzaga.ciphermachine.ciphers.enigma;

public class Enigma {
	
	// The default 5 rotors from the Enigma machine
	public static Rotor ROTOR_I = new Rotor("EKMFLGDQVZNTOWYHXUSPAIBRCJ", 'Q');
	public static Rotor ROTOR_II = new Rotor("AJDKSIRUXBLHWTMCQGZNPYFVOE", 'E');
	public static Rotor ROTOR_III = new Rotor("BDFHJLCPRTXVZNYEIWGAKMUSQO", 'V');
	public static Rotor ROTOR_IV = new Rotor("ESOVPZJAYQUIRHXLNFTGKDCMWB", 'J');
	public static Rotor ROTOR_V = new Rotor("VZBRGITYUPSDNHLXAWMJQOFECK", 'Z');
	
	// The 2 options of reflectos
	public static Reflector REFLECTOR_B = new Reflector("YRUHQSLDPXNGOKMIEBFZCWVJAT");
	public static Reflector REFLECTOR_C = new Reflector("FVPJIAOYEDRZXWGCTKUQSBNMHL");
	
	// Holds the actual parameters of the machine
	private static Reflector reflector;
	private static Rotor[] rotors = {null, null, null};
	private static int[] rings = {0, 0, 0};
	private static int[] position = {0, 0, 0};
	
	/**
	 * Encodes a String with the set machine parameters.
	 * 
	 * @param text
	 * 			String, the text to be encoded
	 * @return String
	 * 			the output of the encoder
	 */
	public static String encode(String text) {
		String output = "";
		
		// Encode char by char of the string
		for (char ch : text.toUpperCase().toCharArray()) {
			output += encodeChar(ch);
		}
		
		return output;
	}
	
	/**
	 * Decodes a String with the set machine parameters.
	 * 
	 * @param text
	 * 			String, the text to be decoded
	 * @return String
	 * 			the output of the decoder
	 */
	public static String decode(String text) {
		// Calls the encoder (Encode and decode are the same in the enigma machine)
		return encode(text);
	}
	
	/**
	 * Set the keys for the Enigma machine.
	 * 
	 * Rotors can be (I, II, III, IV and V).
	 * The start positions and rings are any char from "A" to "Z".
	 * The reflector can be either "B" or "C".
	 * 
	 * @param left
	 * 		String, the left rotor
	 * @param center
	 * 		String, the center rotor
	 * @param right
	 * 		String, the right rotor
	 * @param leftPosition
	 * 		String, the initial character for the left rotor
	 * @param centerPosition
	 * 		String, the initial character for the center rotor
	 * @param rightPosition
	 * 		String, the initial character for the right rotor
	 * @param leftRing
	 * 		String, the left ring character
	 * @param centerRing
	 * 		String, the center ring character
	 * @param rightRing
	 * 		String, the right ring character
	 * @param reflect
	 * 		String, the selected reflector (B or C)
	 */
	public static void setKeyValues(String left, String center, String right,
			String leftPosition, String centerPosition, String rightPosition,
			String leftRing, String centerRing, String rightRing, String reflect) {
		// Set the rotors
		rotors[0] = Enigma.getRotorFromName(left);
		rotors[1] = Enigma.getRotorFromName(center);
		rotors[2] = Enigma.getRotorFromName(right);
		// Set the reflector
		if (reflect.equals("B")) {
			reflector = REFLECTOR_B;
		} else {
			reflector = REFLECTOR_C;
		}
		// Set the initial position for each Rotor
		position[0] = getIFromCh(leftPosition.charAt(0));
		position[1] = getIFromCh(centerPosition.charAt(0));
		position[2] = getIFromCh(rightPosition.charAt(0));
		// Set the rings
        rings[0] = getIFromCh(leftRing.charAt(0));
        rings[1] = getIFromCh(centerRing.charAt(0));
        rings[2] = getIFromCh(rightRing.charAt(0));
	}
	
	/**
	 * Do the encoding and rotors increment for a char
	 * 
	 * @param ch
	 * 			char, the char to be encoded
	 * @return char
	 * 			the output char from the machine
	 */
	private static char encodeChar(char ch) {
		int i;
        
        // if its not a letter, just return it as it is
        if (ch < 'A' || ch > 'Z')
                return ch;
                
        // update the rotors
        incrementRotors();
        
        i = getIFromCh(ch);

        for (int r = 2; r >= 0; r--) {
        	int d = rotors[r].getMap()[(26 + i + position[r] - rings[r]) % 26];
            i = (i + d) % 26;
        }
                
        i = (i + reflector.getMap()[i]) % 26;
        
        for (int r = 0; r < 3; r++) {
        	int d = rotors[r].getMapRev()[(26 + i + position[r] - rings[r]) % 26];
            i = (i + d) % 26;
        }
                
        char chOut = getChFromI(i);
        
        return chOut;
	}
	
	/**
	 * Update the rotors after encoding a char.
	 */
	private static void incrementRotors() {
        // Middle notch - all rotors rotate
		if (position[1] == getIFromCh(rotors[1].getNotch())) {
			position[0] += 1;
            position[1] += 1;
        }
        // Right notch - right two rotors rotate
        else if (position[2] == getIFromCh(rotors[2].getNotch())) {
        	position[1] += 1;
        }
                
        position[2] += 1;
        
        for (int i=0; i < rotors.length; i++) {
        	position[i] = position[i] % 26; 
        }
	}

	/**
	 * Get an int value for a char.
	 * Its in alphabetical order and starting at 0.
	 * 
	 * @param ch
	 * 		char, the char to be transformed into an int
	 * @return int
	 * 		the integer value for the char
	 */
	public static int getIFromCh(char ch) {
		return ch - 'A';
	}
	
	/**
	 * Get the char value for an int.
	 * It starts at 0 and is in alphabetical order.
	 * 
	 * @param i
	 * 		int, the int to be transformed into a char
	 * @return char
	 * 		the alphabetical value for the integer
	 */
	public static char getChFromI(int i) {
		return new Character((char) (i + 'A'));
	}

	
	
	/**
	 * Return the right rotor by its name (I, II, III, IV and V).
	 * If the name doesnt match any rotor, return the rotor V.
	 * 
	 * @param name
	 * 			String, the name of the rotor
	 * @return Rotor
	 * 			the rotor with the specified configuration
	 */
	private static Rotor getRotorFromName(String name) {
		Rotor rotor = null;
		
		if (name.equals("I")) {
			rotor = ROTOR_I;
		} else if (name.equals("II")) {
			rotor = ROTOR_II;
		} else if (name.equals("III")) {
			rotor = ROTOR_III;
		} else if (name.equals("IV")) {
			rotor = ROTOR_IV;
		} else {
			rotor = ROTOR_V;
		}
		
		return rotor;
	}

}
