package cs.gonzaga.ciphermachine.ciphers;

/**
 *
 * @author Dalton
 */
public class ShiftedCaesarsBox {
    private static String textCiphered;
    private int keyRotation;
    
    
    public ShiftedCaesarsBox(){
    }
    
    
    public static String encode(int key, String plainText){
        key = key%26;//make sure the key is in good interval [0,25]
        
        char[] cryptedText = new char[plainText.length()]; 
        for(int i = 0; i < plainText.length(); i++)
            cryptedText[i] = shiftLetter(plainText.charAt(i), key); 
        
        System.out.println("inside class " + new String(cryptedText));
        cryptedText = transposeMatrix(cryptedText);
        
        textCiphered = new String(cryptedText);
        return textCiphered;
    }
    
    
    public static String decode(int key, String plainText){
        textCiphered = encode(-key, plainText);
        return textCiphered;
    }
    
    
    private static char shiftLetter(char letterToRotate, int keyRotation){
        char letter;
        int ascii;
        letter = letterToRotate;
        ascii = (int)letter;
        
        //Lower-case letters
        if(ascii > 64 && ascii < 91){
            if((ascii + keyRotation) < 91 && (ascii + keyRotation) > 64)
                ascii = ascii + keyRotation;
            else if ((ascii + keyRotation) >= 91)
                ascii =  (ascii + keyRotation) - 26;
            else
                ascii = (ascii + keyRotation) + 26;
        }
        
        //Upper-case letters
        else if(ascii > 96 && ascii < 123){
            if((ascii + keyRotation) < 123 && (ascii + keyRotation) > 96)
                ascii = ascii + keyRotation;
            else if ((ascii + keyRotation) >= 123)
                ascii = (ascii + keyRotation) - 26;
            else
                ascii = (ascii + keyRotation) + 26;
        }
        
        letter = (char)ascii;
        
        return letter;
    }
    
    private static char[] transposeMatrix(char[] letters){
        char[] transposedText;
        double epsilon = 0.00000001;
        char[][] matrixText;
        
        double sqrtOfLength = Math.sqrt((double)letters.length);
        int matrixSize;
        
        if((sqrtOfLength-(int)sqrtOfLength)<epsilon && -epsilon<(sqrtOfLength-(int)sqrtOfLength))
            matrixSize = (int)Math.sqrt((double)letters.length);
        else
            matrixSize = (int)Math.sqrt((double)letters.length)+1;
        
        
        matrixText = new char[matrixSize][matrixSize];
        
        for(int line=0; line<matrixSize; line++){
            for(int column=0; column<matrixSize; column++){
                if((matrixSize*line+column)<letters.length)
                    matrixText[line][column] = letters[matrixSize*line+column];
                else
                    matrixText[line][column] = ' ';
            }
        }
        
        transposedText = new char[matrixSize*matrixSize];
        
        for(int line=0; line<matrixSize; line++){
            for(int column=0; column<matrixSize; column++){
                transposedText[matrixSize*line+column] = matrixText[column][line];
            }
        }
        
        return transposedText;
    }
}
