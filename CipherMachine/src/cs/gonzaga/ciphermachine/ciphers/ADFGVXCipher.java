package cs.gonzaga.ciphermachine.ciphers;

import java.util.Arrays;

/**
 *
 * @author Dalton
 */
public class ADFGVXCipher {
    private static String textCiphered;
    private static String[] codeMatrix = {"AA","AD","AF","AG","AV","AX",
                                          "DA","DD","DF","DG","DV","DX",
                                          "FA","FD","FF","FG","FV","FX",
                                          "GA","GD","GF","GG","GV","GX",
                                          "VA","VD","VF","VG","VV","VX",
                                          "XA","XD","XF","XG","XV","XX"};
    
    private static char[] correspValue = {'b','t','a','l','p','0',
                                          'd','h','o','z','k','2',
                                          'q','f','v','s','n','5',
                                          'g','j','i','c','3','u',
                                          'x','4','1','m','7','r',
                                          '9','8','e','6','w','y'};
    
    public static String encode(String key, String plainText){
        
        plainText = plainText.toLowerCase();
        key = validateKey(key);
               
        char[] cryptedText = new char[2*plainText.length()];
        char[] returnedCode;
        for(int i = 0; i < plainText.length(); i++){
            returnedCode = replaceWithCode(plainText.charAt(i));
            cryptedText[2*i] = returnedCode[0];
            cryptedText[2*i+1] = returnedCode[1];
        }
               
        //Matrix operations come next
        char[][] shiftedColumnsMatrix;
        char[][] transposedMatrix;
        char[][] matrixText;
        
        double epsilon = 0.00000001;
        int nOfColumns = key.length();
        double nOfLines = ((double)cryptedText.length/key.length());
        if((nOfLines-(int)nOfLines)>epsilon)
            nOfLines = nOfLines + 1;
        
        matrixText = createMatrix(cryptedText, (int)nOfLines, nOfColumns);
                
        shiftedColumnsMatrix = shiftColumns(matrixText, key);
                
        transposedMatrix = transposeMatrix(shiftedColumnsMatrix);
        
        cryptedText = fromMatrixToArray(transposedMatrix);
        
        
        textCiphered = new String(cryptedText);
        return textCiphered;
    }
    
    
    
    public static String decode(String key, String plainText){
        
        plainText = plainText.toUpperCase();
        key = validateKey(key);
               
        char[] cryptedText = plainText.toCharArray();
        
        
        //Matrix operations come next
        char[][] shiftedColumnsMatrix;
        char[][] transposedMatrix;
        char[][] matrixText;
        
        double epsilon = 0.00000001;
        int nOfLines = key.length();
        double nOfColumns = ((double)cryptedText.length/key.length());
        if((nOfColumns-(int)nOfColumns)>epsilon)
            nOfColumns = nOfColumns + 1;
        
        matrixText = createMatrix(cryptedText, nOfLines, (int)nOfColumns);
        
        transposedMatrix = transposeMatrix(matrixText);
        /*for(int i = 0; i < transposedMatrix.length; i++){
            System.out.println(transposedMatrix[i]);
        }
        */
        shiftedColumnsMatrix = reShiftColumns(transposedMatrix, key);
        
        cryptedText = fromMatrixToArray(shiftedColumnsMatrix);
        
        
        char[] decryptedText = new char[cryptedText.length/2];
        char[] pairToEvaluate = new char[2];
        for(int i = 0; i < cryptedText.length/2; i++){
            pairToEvaluate[0]=cryptedText[2*i];
            pairToEvaluate[1]=cryptedText[2*i+1];
            
            decryptedText[i] = replaceWithCorrespValue(pairToEvaluate);
        }
                
        textCiphered = new String(decryptedText);
        return textCiphered;
    }
    
    
    
    private static String validateKey(String key){
        key = key.toLowerCase();
        key = key.trim();
        
        for(int i = 1; i < (key.length()); i++){
            for(int j=0; j<i; j++){
                if(key.charAt(i)==key.charAt(j)){
                    j=i;
                    i=i-1;
                    key = key.substring(0,j)+key.substring(j+1,key.length());
                }
            }
        }
        
        return key;
    }
    
    
    
    private static char[] replaceWithCode(char value){
        
        int i = 0;
        char[] codeMatrixValue = new char[2];
        
        while(i<36 && value != correspValue[i])
            i++;
        
        if(i<36){
            codeMatrixValue[0] = codeMatrix[i].charAt(0);
            codeMatrixValue[1] = codeMatrix[i].charAt(1);
        }
        else{
            codeMatrixValue[0] = value;
            codeMatrixValue[1] = value;
        }
        
        return codeMatrixValue;
    }
    
    
    
    private static char replaceWithCorrespValue(char[] valuePassed){
        
        int i = 0;
        char correspValueToReturn;
        String value = new String(valuePassed);
        
        while(i<36 && !codeMatrix[i].equals(value))
            i++;
        
        if(i<36){
            correspValueToReturn = correspValue[i];
        }
        else if(valuePassed[0] == 'Z'){
            correspValueToReturn = ' ';
        }
        else{
            correspValueToReturn = valuePassed[0];
        }
            
        
        return correspValueToReturn;
    }
    
   
    
    private static char[][] createMatrix(char[] letters, int nOfLines, int nOfColumns){
                
        char[][] matrixText = new char[(int)nOfLines][nOfColumns];
        
        for(int line=0; line < nOfLines; line++){
            for(int column=0; column < nOfColumns; column++){
                if((nOfColumns*line+column) < letters.length)
                    matrixText[line][column] = letters[nOfColumns*line+column];
                else
                    matrixText[line][column] = 'Z';
            }
        }
        
        return matrixText;
    }
    
    
    private static char[][] transposeMatrix(char[][] matrixText){
        int nOfLines = matrixText.length;
        int nOfColumns = matrixText[0].length;
             
        char [][] transposedMatrix = new char[nOfColumns][nOfLines];
        
        for(int line=0; line<nOfLines; line++){
            for(int column=0; column<nOfColumns; column++){
                transposedMatrix[column][line] = matrixText[line][column];
            }
        }
        
        return transposedMatrix;
    }
    
    
    private static char[][] shiftColumns(char[][] matrixText, String key){
        
        int nOfLines = matrixText.length; 
        int nOfColumns = matrixText[0].length; //equals to key.length
        char[][] shiftedMatrix = new char[nOfLines][nOfColumns];
        
        char[] keyShifted;
        int[] newLetterPosition = new int[key.length()];
        
        keyShifted = key.toCharArray();
        Arrays.sort(keyShifted);
        
        for(int i=0; i<key.length(); i++){
            for(int j=0; j<keyShifted.length; j++){
                if(keyShifted[i]==key.charAt(j))
                    newLetterPosition[i] = j;
            }
        }
        
        for(int i=0; i<nOfColumns; i++){
            for(int j=0; j<nOfLines; j++){
                shiftedMatrix[j][i] = matrixText[j][newLetterPosition[i]];
            }
            
        }
        
        return shiftedMatrix;
    }
    
    
    private static char[][] reShiftColumns(char[][] matrixText, String key){
        
        int nOfLines = matrixText.length; 
        int nOfColumns = matrixText[0].length; //equals to key.length
        char[][] shiftedMatrix = new char[nOfLines][nOfColumns];
        
        char[] keyShifted;
        int[] newLetterPosition = new int[key.length()];
        
        keyShifted = key.toCharArray();
        Arrays.sort(keyShifted);
        
        for(int i=0; i<key.length(); i++){
            for(int j=0; j<keyShifted.length; j++){
                //The next if is the only thing that changes from shiftColumns
                if(keyShifted[j]==key.charAt(i))
                    newLetterPosition[i] = j;
            }
        }
        
        /*for(int i=0; i<newLetterPosition.length; i++)
            System.out.println(newLetterPosition[i]);
        */
        
        for(int i=0; i<nOfColumns; i++){
            for(int j=0; j<nOfLines; j++){
                shiftedMatrix[j][i] = matrixText[j][newLetterPosition[i]];
            }
        }
        
        return shiftedMatrix;
    }
    
    
    private static char[] fromMatrixToArray(char[][] matrix){
        char[] array = new char[matrix[0].length*matrix.length];
        
        for(int i=0; i<matrix.length; i++){
            for(int j=0; j<matrix[i].length; j++)
                array[matrix[i].length*i + j] = matrix[i][j];
        }
        return array;
    }
   
}
