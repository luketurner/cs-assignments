package cs.gonzaga.ciphermachine.ciphers;


import java.util.ArrayList;
import java.util.Scanner;

public class PlayfairCipher {
	
	public static ArrayList<Integer> k = new ArrayList<Integer>();
	
	private String pairs(String message){
		String s = new String();
		int i = 0;
		
		String temp = new String();
		
		for(int j = 0; j < message.length(); j++){
			if (message.charAt(j) == 'J'){
				temp += 'I';
			}else
			if (!(message.charAt(j) == 32)){
				temp += message.charAt(j);
			} else {
				k.add(j);			
			}
			
		}
		
	///	System.out.println("temp " +temp);
		for(int x = 0; x < k.size(); x++){
	///		System.out.println("k = " + k);
		}
		
		while(i < (temp.length())-1){
			
			
			if (temp.charAt(i) != temp.charAt(i+1)){
				s += temp.substring(i, i+2);
				//s += "" + message.charAt(i) + message.charAt(i+1);
				i++;
				i++;
			} else {
				s += temp.charAt(i) + "Q";
				i++;
			}
		}
		
		if (i == temp.length()-1){
			if (s.length()%2 == 0){
				s += temp.charAt(temp.length()-1) + "Q";
			} else {
			}
		}

	//	System.out.println("s = " + s);
		
	
		return s; 
	}
	
	private static String AdjustKey(String key){
		String newKey = new String ();
		boolean flag = false;
		String t = new String();
		
		for (int i = 0; i < key.length(); i++){
			if (key.charAt(i) != (char)(32)){
				t += key.charAt(i);
			}
			else {
				
			}
		}
	//	System.out.println(t);
		key = t.toUpperCase();
		

		
		for(int k = 0; k < 26; k++){
			key += (char) (k + 65);
		}
		
		
		for (int i = 0; i < key.length(); i++){
			for (int j = 0; j < newKey.length(); j++){
		
				if (key.charAt(i) == newKey.charAt(j)){
					flag = true;
		//			System.out.println("newkey length " + newKey.length());
				
				}
			}
			
			if (flag == false){
				newKey += key.charAt(i);
		//		System.out.println("newkey length " + newKey.length());
			} 
			
			flag = false;
		} 
		

		
	
		return newKey;
	}

	private static char[][] matrix(String key){
		char[][] m = new char[5][5];
		//char k = 65;
		String adjustedKey = new String();
		
		key = AdjustKey(key);
	//	System.out.println(key);
		
		for (int i = 0; i < key.length(); i++){
			if (key.charAt(i) == 'J'){
				adjustedKey += 'I';
				//System.out.println("key com j i " + key);
			}
			else {
				adjustedKey += key.charAt(i);
			}
		}
		
		
		key = AdjustKey(adjustedKey);
	//	System.out.println("key " + key);
		
	///	System.out.println("adjustedKey " + adjustedKey);
		
		
		for (int i = 0; i < 5; i++){
			for(int j = 0; j < 5; j++){
				if (i*5+j < key.length()){
				m[i][j] = key.toUpperCase().charAt(i*5+j);
		///		System.out.printf("%s ",m[i][j]);
				}
			}
	///		System.out.println();
		}

		return m;
	}
	
	
	 public int[] GetDiminsions(String k, char letter){
	        int []key = new int[2];
	        char[][] table = new char[5][5];
			
			
			table = matrix(k);
			
	        for (int i=0 ; i<5 ;i++){
	            for (int j=0 ; j<5 ; j++){
	            	
	                if(table[i][j] == letter){
	                	
	                    key[0]=i;
	                    key[1]=j;
	                    
	                    break;
	                }
	                
	            }
	        }
	        
	        
	        return key;
	    }
	
	public static String encode(String key, String message){
		String s = new String();
		char[][] table = new char[5][5];
		String encodedMessage = new String();
		PlayfairCipher p = new PlayfairCipher();
		table = matrix(key);
		s = message.toUpperCase();
		s = p.pairs(s);
		//System.out.println("k " + p.k);
	
		
	    char one;
	    char two;
	    
	    int part1[] = new int[2];
	    int part2[] = new int[2];
		
		

	    for (int i = 0 ; i < s.length(); i++ ){
		        one = s.charAt(i);
		 ///       System.out.println("one x[i].charAt(0) = " + one);
		        two = s.charAt(i+1);
		///        System.out.println("two x[i].charAt(0) = " + two);
		        part1 = p.GetDiminsions(key,one);
		        part2 = p.GetDiminsions(key,two);
		        
		        if(part1[0]==part2[0])
		        {
		        	if (part1[1]<4)
		        		part1[1]++;
		        	else
		        		part1[1]=0;
		        	if(part2[1]<4)
		        		part2[1]++;
		        	else
		        		part2[1]=0;
		        }else if (part1[1]==part2[1]){
		        	if (part1[0]<4)
		        		part1[0]++;
		        	else
		        		part1[0]=0;

		        	if(part2[0]<4)
		        		part2[0]++;
		        	else
		        		part2[0]=0;
		        }else{
		        	int temp=part1[1];
		        	part1[1]=part2[1];
		        	part2[1]=temp;
		        }
		        
	        encodedMessage = encodedMessage + table[part1[0]][part1[1]] + table[part2[0]][part2[1]];
	        i++;
	    }
	    
	    System.out.println(encodedMessage);

	   
	    return encodedMessage;
	
	}
	
	public static String decode(String key, String encodedMessage){
		String s = new String();
		char[][] table = new char[5][5];
		String tdecodedMessage = new String();
		char[] decodedMessage = new char[encodedMessage.length() + k.size()];
		String h = new String();
		PlayfairCipher p = new PlayfairCipher();
		table = matrix(key);
	
		s = p.pairs(encodedMessage);
	
		String decoded = new String();
		
	    char one;
	    char two;
	    
	    int part1[] = new int[2];
	    int part2[] = new int[2];
	    
	    for (int i = 0 ; i < s.length(); i++ ){
	    	one = s.charAt(i);
	 ///       System.out.println("one x[i].charAt(0) = " + one);
	        two = s.charAt(i+1);
	 ///       System.out.println("two x[i].charAt(0) = " + two);
	        part1 = p.GetDiminsions(key,one);
	        part2 = p.GetDiminsions(key,two);
	    
	        if(part1[0]==part2[0])
	        {
	            if (part1[1]>0)
	                part1[1]--;
	            else
	                part1[1]=4;
	            if(part2[1]>0)
	                part2[1]--;
	            else
	                part2[1]=4;
	        }

	        else if (part1[1]==part2[1])
	        {
	            if (part1[0]>0)
	                part1[0]--;
	            else
	                part1[0]=4;
	            if(part2[0]>0)
	                part2[0]--;
	            else
	                part2[0]=4;
	        }
	        else
	        {
	            int temp=part1[1];
	            part1[1]=part2[1];
	            part2[1]=temp;
	        }
	       
	        tdecodedMessage = tdecodedMessage + table[part1[0]][part1[1]] + table[part2[0]][part2[1]];
	        i++;
	    }
	    
	///    System.out.println("decodedMessage before take Q off " + tdecodedMessage);
	    for(int i = 0; i < tdecodedMessage.length()-1; i++){
	    	if ((tdecodedMessage.charAt(i) == 'Q')&&(tdecodedMessage.charAt(i+1) != 'U')){
	    	} else {
	    		h += tdecodedMessage.charAt(i);
	    	}
	    }
	    
	    if (!(tdecodedMessage.charAt(tdecodedMessage.length()-1) == 'Q')){
	    	h += tdecodedMessage.charAt(tdecodedMessage.length()-1);
	    } else {
	    	
	    }
	 ///   System.out.println("h " + h);
	    
	    k.toString();
	//    System.out.println("k " + k);
	    
	//    System.out.println("k.size " + k.size());
	    

	    int t = 0;
	    int i = 0;
	 
	    	for (int j = 0; j < (h.length() + k.size()); j++){
	    		if (t <= k.size()-1){
	    			if (j == (k.get(t))){
	    				decodedMessage[j] = (char) 32;
	    		
	    				t++;
	    				continue;
	    			
	    			}else if ((j != (k.get(t)))&&(i < h.length())) {
	    				decodedMessage[j] = h.charAt(i);
	    				i++;
	    			}
	    		}
	    		if ((t >= k.size()) && (i < h.length())) {
	    			decodedMessage[j] = h.charAt(i);
	    			i++;
	    		}
	    	}
	    
	    System.out.println(decodedMessage);
	    
	
	 //   System.out.println(h);
	    for (int y = 0; y < decodedMessage.length; y++){
	    	decoded += decodedMessage[y];
	    }
	 //   System.out.println("decodedMessage " + decoded);
	    
	    PlayfairCipher.k.clear();
		
	    return decoded;
	}
	
	public static void main(String[] args){
		
	
	//	Playfair.decode("Giovanna", Playfair.encode("Giovanna", "HELLO ONE AND ALL"));
	//	Playfair.decode("Giovanna", Playfair.encode("Giovanna", "quotes question"));
	//	Playfair.encode("PlayfairExample", "HideTheGoldInTheTreeStump");
	//	Playfair.decode("PlayfairExample", Playfair.encode("Playfair Example", "Hide The Gold In The Tree Stump"));
	//	System.out.println("BMODZBXDNABEKUDMUIXOMOUVIF");
		PlayfairCipher.decode("Giovanna Bonafe Bernardi", PlayfairCipher.encode("Giovanna Bonafe Bernardi", "Eu comi o queijo"));
		
	}

}
