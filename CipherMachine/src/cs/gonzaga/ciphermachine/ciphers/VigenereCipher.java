package cs.gonzaga.ciphermachine.ciphers;

public class VigenereCipher {

	public static int r,c,r2,c2;
	
	private static String changeKey(String key, String message){
		// change the key to match the same size as the message
		String changedKey = new String();
		
		while(key.length() < message.length()){
			key += key;	
			changedKey = key;
			//repeat the word until key has the same size as message
		} 
		if (key.length() > message.length()){
			changedKey = key.substring(0, message.length()); 
			// cut the key to the size of the message
		} else{
			changedKey = key;
		}
		
		return changedKey.toUpperCase();
	}
	
	private static char[][] tabulaRecta(){
		char[][] table = new char[27][27];	
		char k = 65;
		
		for (int i = 0; i < 26; i++){
			for (int j = 0; j < 26; j++){
				table[i][j] = k;
				//System.out.printf("%s ",table[i][j]);
				
				k++;
				if(k > 90){
					k = 65;
				}
			}
			k++;
			//System.out.println();
		}
		
		System.out.println();
		return table;
	}
	
	public static String encode(String key, String message){
		String encodedMessage = new String();
		String changedKey = new String();
		char row, column; 
		
		
		
		char[][] table = tabulaRecta();
		
		changedKey = VigenereCipher.changeKey(key, message);
		
		char k = 65;
		
		for (int i = 0; i < changedKey.length(); i++){
			row = changedKey.charAt(i);
		//	System.out.println("row "+ row);
			column = message.toUpperCase().charAt(i);
		//	System.out.println("column " + column);
			
			for (k = 65; k < 74; k++){
				if (row == k){
					r = 0 + k-65;
				}
				if (column == k){
					c = 0 + k-65;
				}
			}
			
			for (k = 74; k < 91; k++){
				if (row == k){
					r = 9 + k-74;
				}
				if (column == k){
					c = 9 + k-74;
				}
			}
			
			
			if (!((column > 64)&&(column < 91))){
				
				table[r2][c2] = column;
				//System.out.println("table[r2][c2] inside non uppercase letters"+(table[r2][c2]));
				encodedMessage += ""+table[r2][c2];
			}else{
				encodedMessage += "" + table[r][c];
				//System.out.println("encoded message"+ encodedMessage);
			}
			// search rows and column in tabulaRecta
			// key is row, message is column
		}	
		
		
		return encodedMessage;
	}
	
	public static String decode(String key, String encoded){
		String decodedMessage = new String();
		
		String changedKey = new String();
		char row, letter; 
		
		
		char[][] table = tabulaRecta();
		changedKey = VigenereCipher.changeKey(key, encoded);
		
		char k = 65;
		char column = 0, column1 =0;
		
		
		for (int i = 0; i < changedKey.length(); i++){
			row = changedKey.charAt(i);
			//System.out.println("row "+ row);
			
			
			letter = encoded.toUpperCase().charAt(i);
			//System.out.println("column " + column);
			
			
			
			for (k = 65; k < 74; k++){
				if (row == k){
					r = 0 + k-65;
				}
			
			
				for (int y = 0; y < 26; y++){
					if (table[r][y] == letter){
						c = y;
						column = (char) (c + 65);
					}
				}
						
				
			}
			
			for (k = 74; k < 91; k++){
				if (row == k){
					r = 9 + k-74;
				}
				
				
				for (int y = 0; y < 26; y++){
					if (table[r][y] == letter){
						c = y;
						column = (char) (c + 65);
					}
				}
						
				
		
			}
			
		
			if (!((letter > 64)&&(letter < 91))){
				column1 = (char) (letter);
				decodedMessage += ""+ column1;
			}else{
			
				decodedMessage += "" + column;
			}
			
		}	
		
		return decodedMessage;
	}
	
	private static void main(String[] args){
		
		//System.out.println(changeKey("dalton","giovanna pediu pra eu fazer um teste. XD"));
		//System.out.println();

		System.out.println(VigenereCipher.encode("dalton", "giovanna pediu pra eu fazer um teste. XD"));
		System.out.println(VigenereCipher.decode("dalton", VigenereCipher.encode("dalton", "giovanna pediu pra eu fazer um teste. XD")));
		
		System.out.println(changeKey("dalton","eu eu eu eueueueu eu"));
		System.out.println("eu eu eu eueueueu eu");
		System.out.println(VigenereCipher.encode("dalton", "eu eu eu eueueueu eu"));
		System.out.println(VigenereCipher.decode("dalton", VigenereCipher.encode("dalton", "eu eu eu eueueueu eu")));
		//Vigenere.tabulaRecta();
		
	
		
		System.out.println(VigenereCipher.encode("l", "u.u"));
		System.out.println(VigenereCipher.decode("l", VigenereCipher.encode("l", "u.u")));
		
		System.out.println(changeKey("xadrez","xadrez amanha? com certeza!"));
		System.out.println("xad.r.ez amanha? com certeza!");
		System.out.println(VigenereCipher.encode("xadrez", "xad.r.ez amanha? com certeza!"));
		System.out.println(VigenereCipher.decode("xadrez", VigenereCipher.encode("xadrez", "xad.r.ez amanha? com certeza!")));
		
	
	} 
}
