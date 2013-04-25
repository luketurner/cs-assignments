package cs.gonzaga.ciphermachine.ciphers;

public class RailFenceCipher {
/*
 * The Rail Fence Cipher writes the message in vertical "rails" of a predetermined length and reads it horizontally across the rails.
 * Example: "HelloWorld" railLength = 3
 * 
 * HLOD
 * EORA
 * LWLA
 * 		The 'A's are added to preserve the format of the rails.  Any character could be used as a placeholder.
 * Enciphered Text: HLODEORALWLA
 */

	public static String encode(String myMessage, int railLength, String myPlaceHolder)
	{
		String message = myMessage.toUpperCase();
        String placeHolder = myPlaceHolder.substring(0,1).toUpperCase();
		int numberOfRails;
		
		if((message.length() % railLength) == 0)
			numberOfRails = message.length() / railLength;
		else
			numberOfRails = (message.length() / railLength) + 1;
		
		String[][] rails = new String[numberOfRails][railLength];
		int messagePosition = 0;
		for(int i = 0; i < numberOfRails; i++)
			for(int j = 0; j < railLength; j++)
			{
				if(messagePosition < message.length())
				{
					rails[i][j] = message.substring(messagePosition,messagePosition + 1);
					messagePosition++;
				}
				else
					rails[i][j] = placeHolder;
			}
		
		String encipheredMessage = "";
		for(int j = 0; j < railLength; j++)
			for(int i = 0; i < numberOfRails; i++)
			{
				encipheredMessage = encipheredMessage + rails[i][j];
			}
		
		return encipheredMessage;
	}
	
    public static String decode(String myMessage, int railLength, String myPlaceHolder)
    {
        String message = myMessage.toUpperCase();
        String placeHolder = myPlaceHolder.substring(0,1).toUpperCase();
        int numberOfRails;
        
        if((message.length() % railLength) == 0)
        	numberOfRails = message.length() / railLength;
        else
        	numberOfRails = (message.length() / railLength) + 1;
        
        String[][] rails = new String[numberOfRails][railLength];
        int messagePosition = 0;
        
        for(int i = 0; i < railLength; i++)
            for(int j = 0; j < numberOfRails; j++)
            {
                if(messagePosition < message.length())
                    {
                        rails[j][i] = message.substring(messagePosition, messagePosition + 1);
                        messagePosition++;
                    }
                else
                    rails[j][i] = placeHolder;
            }
        
        String decipheredMessage = "";
        for(int i = 0; i < numberOfRails; i++)
            for(int j = 0; j < railLength; j++)
            {
                if((i == numberOfRails - 1) && rails[i][j].equals(placeHolder))
                    continue;
                else
                    decipheredMessage = decipheredMessage + rails[i][j];
            }
        return decipheredMessage;
    }
       /*
	public static void main(String[] args)
	{
		String myMessage = "Hello World";
		String myCipherMessage = RailFenceCipher.encode(myMessage, 3, "ABBBB");
		System.out.println("Message: " + myMessage);
		System.out.println("Cipher: " + myCipherMessage);
                //Expected: HLODEORALWLA
                String deciphered = RailFenceCipher.decode("HLODEORALWLA", 3, "ABBBB");
                System.out.println("Deciphered: " + deciphered);
	}
        */
}