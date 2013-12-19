package cs.gonzaga.ciphermachine.ciphers;


/*
 * The Route Cipher is similar to the Rail Fence cipher in that it arranges the input text into vertical rails.
 * However, the Route cipher lets you choose which corner of the box of Rails to start in,
 * as well as choose a path to take.
 *
 * Current Routes:
 *  Route 1: horizontal back-and-forth
 * 
 * The starting corner is determined by two booleans:  true and true means top-right, true and false is top and right, etc.
 */
public class RouteCipher {
    public static String encode(String myMessage, int railLength, 
            boolean top, boolean left, int route, String myPlaceHolder)
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
        int routePickerOne = 0, routePickerTwo = 0;
        if(top)
            routePickerOne = 1;
        if(left)
            routePickerTwo = 2;
        route = (route * 10) + routePickerOne + routePickerTwo;
        //thus, a route *0 starts bottom-right, *1 starts top-right, *2 starts bottom-left, *3 starts top-left
        //where * is the general route path.
        boolean goingLeft = true, goingDown = true;
        int counter;
        switch(route)
        {
            case 10: //Case 10: horizontal back-and-forth upwards starting bottom-right
            {
                counter = railLength - 1;
                while(counter >= 0)
                {
                    if(goingLeft)
                    {
                        for(int i = numberOfRails - 1; i >= 0; i--)
                            encipheredMessage = encipheredMessage + rails[i][counter];
                        counter--;
                        goingLeft = false;
                        continue;
                    }
                    else
                    {
                        for(int i = 0; i < numberOfRails; i++)
                            encipheredMessage = encipheredMessage + rails[i][counter];
                        counter--;
                        goingLeft = true;
                        continue;
                    }
                }
                return encipheredMessage;
            }
            case 11: //Case 11: horizontal back-and-forth downwards starting top-right
            {
                counter = 0;
                while(counter < railLength)
                {
                    if(goingLeft)
                    {
                        for(int i = numberOfRails - 1; i >= 0; i--)
                            encipheredMessage = encipheredMessage + rails[i][counter];
                        counter++;
                        goingLeft = false;
                        continue;
                    }
                    else
                    {
                        for(int i = 0; i < numberOfRails; i++)
                            encipheredMessage = encipheredMessage + rails[i][counter];
                        counter++;
                        goingLeft = true;
                        continue;
                    }
                }
                return encipheredMessage;
            }
            case 12: //Case12: horizontal back-and-forth upwards starting bottom-left
            {
                counter = railLength - 1;
                goingLeft = false;
                while(counter >= 0)
                {
                    if(goingLeft)
                    {
                        for(int i = numberOfRails - 1; i >= 0; i--)
                            encipheredMessage = encipheredMessage + rails[i][counter];
                        counter--;
                        goingLeft = false;
                        continue;
                    }
                    else
                    {
                        for(int i = 0; i < numberOfRails; i++)
                            encipheredMessage = encipheredMessage + rails[i][counter];
                        counter--;
                        goingLeft = true;
                        continue;
                    }
                }
                return encipheredMessage;
            }
            case 13: //Case 13: horizontal back-and-forth downwards starting top-left
            {
                counter = 0;
                goingLeft = false;
                while(counter < railLength)
                {
                    if(goingLeft)
                    {
                        for(int i = numberOfRails - 1; i >= 0; i--)
                            encipheredMessage = encipheredMessage + rails[i][counter];
                        counter++;
                        goingLeft = false;
                        continue;
                    }
                    else
                    {
                        for(int i = 0; i < numberOfRails; i++)
                            encipheredMessage = encipheredMessage + rails[i][counter];
                        counter++;
                        goingLeft = true;
                        continue;
                    }
                }
                return encipheredMessage;
            }
            case 20: //Case 20: vertical back-and-forth starting bottom right.
            {
                goingDown = false;
                counter = numberOfRails - 1;
                while(counter >= 0)
                {
                    if(goingDown)
                    {
                        for(int i = 0; i < railLength; i++)
                            encipheredMessage = encipheredMessage + rails[counter][i];
                        counter--;
                        goingDown = false;
                        continue;
                    }
                    else
                    {
                        for(int i = railLength - 1; i >= 0; i--)
                            encipheredMessage = encipheredMessage + rails[counter][i];
                        counter--;
                        goingDown = true;
                        continue;
                    }
                }
                return encipheredMessage;
            }
            case 21: //vertical back-and-forth starting top left
            {
                counter = numberOfRails - 1;
                while(counter >= 0)
                {
                    if(goingDown)
                    {
                        for(int i = 0; i < railLength; i++)
                            encipheredMessage = encipheredMessage + rails[counter][i];
                        counter--;
                        goingDown = false;
                        continue;
                    }
                    else
                    {
                        for(int i = railLength - 1; i >= 0; i--)
                            encipheredMessage = encipheredMessage + rails[counter][i];
                        counter--;
                        goingDown = true;
                        continue;
                    }
                }
                return encipheredMessage;
            }
            case 22: //vertical back-and-forth starting bottom right.
            {
                goingDown = false;
                counter = 0;
                while(counter < numberOfRails)
                {
                    if(goingDown)
                    {
                        for(int i = 0; i < railLength; i++)
                            encipheredMessage = encipheredMessage + rails[counter][i];
                        counter++;
                        goingDown = false;
                        continue;
                    }
                    else
                    {
                        for(int i = railLength - 1; i >= 0; i--)
                            encipheredMessage = encipheredMessage + rails[counter][i];
                        counter++;
                        goingDown = true;
                        continue;
                    }
                }
                return encipheredMessage;
            }
            case 23: //vertical back-and-forth starting top left.
                {
                counter = 0;
                while(counter < numberOfRails)
                {
                    if(goingDown)
                    {
                        for(int i = 0; i < railLength; i++)
                            encipheredMessage = encipheredMessage + rails[counter][i];
                        counter++;
                        goingDown = false;
                        continue;
                    }
                    else
                    {
                        for(int i = railLength - 1; i >= 0; i--)
                            encipheredMessage = encipheredMessage + rails[counter][i];
                        counter++;
                        goingDown = true;
                        continue;
                    }
                }
                return encipheredMessage;
            }
            default: //Default case: not a valid route; returns original text.
            {
                System.out.println("Invalid route!");
                return myMessage;
            }
        }   
    }
    public static String decode(String myMessage, int railLength, 
            boolean top, boolean left, int route, String myPlaceHolder)
    {
        String decipheredMessage = "";
        int isLeft = 0, isTop = 0;
        if(top)
            isTop = 1;
        if(left)
            isLeft = 2;
        route = (route * 10) + isTop + isLeft;
        String message = myMessage.toUpperCase();
        String placeHolder = myPlaceHolder.substring(0,1).toUpperCase();
        int numberOfRails;
	if((message.length() % railLength) == 0)
            numberOfRails = message.length() / railLength;
        else
            numberOfRails = (message.length() / railLength) + 1;
        String[][] rails = new String[numberOfRails][railLength];
        int messagePosition = 0;
        int counter;
        boolean goingLeft = true, goingDown = true;
        switch(route)
        {
            case 10: //Case 10: horizontal back-and-forth upwards starting bottom-right
            {
                counter = railLength - 1;
                while(counter >= 0)
                {
                    if(goingLeft)
                    {
                        for(int i = numberOfRails - 1; i >= 0; i--)
                        {
                            rails[i][counter] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter--;
                        goingLeft = false;
                        continue;
                    }
                    else
                    {
                        for(int i = 0; i < numberOfRails; i++)
                        {
                            rails[i][counter] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter--;
                        goingLeft = true;
                        continue;
                    }
                }
                break;
            }
            case 11: //Case 11: horizontal back-and-forth downwards starting top-right
            {
                counter = 0;
                while(counter < railLength)
                {
                    if(goingLeft)
                    {
                        for(int i = numberOfRails - 1; i >= 0; i--)
                        {
                            rails[i][counter] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter++;
                        goingLeft = false;
                        continue;
                    }
                    else
                    {
                        for(int i = 0; i < numberOfRails; i++)
                        {
                            rails[i][counter] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter++;
                        goingLeft = true;
                        continue;
                    }
                }
                break;
            }
                case 12: //Case12: horizontal back-and-forth upwards starting bottom-left
            {
                counter = railLength - 1;
                goingLeft = false;
                while(counter >= 0)
                {
                    if(goingLeft)
                    {
                        for(int i = numberOfRails - 1; i >= 0; i--)
                        {
                            rails[i][counter] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter--;
                        goingLeft = false;
                        continue;
                    }
                    else
                    {
                        for(int i = 0; i < numberOfRails; i++)
                        {
                            rails[i][counter] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter--;
                        goingLeft = true;
                        continue;
                    }
                }
                break;
            }
            case 13: //Case 13: horizontal back-and-forth downwards starting top-left
            {
                counter = 0;
                goingLeft = false;
                while(counter < railLength)
                {
                    if(goingLeft)
                    {
                        for(int i = numberOfRails - 1; i >= 0; i--)
                        {
                            rails[i][counter] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter++;
                        goingLeft = false;
                        continue;
                    }
                    else
                    {
                        for(int i = 0; i < numberOfRails; i++)
                        {
                            rails[i][counter] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter++;
                        goingLeft = true;
                        continue;
                    }
                }
                break;
            }
            case 20: //Case 20:  vertical back-and-forth starting bottom-right.
            {
                counter = numberOfRails - 1;
                goingDown = false;
                while(counter >= 0)
                {
                    if(goingDown)
                    {
                        for(int i = 0; i < railLength; i++)
                        {
                            rails[counter][i] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter--;
                        goingDown = false;
                        continue;
                    }
                    else
                    {
                        for(int i = railLength - 1; i >= 0; i--)
                        {
                            rails[counter][i] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter--;
                        goingDown = true;
                        continue;
                    }
                }
                break;
            }
            case 21: //vertical back-and-forth starting top right.
            {
                counter = numberOfRails - 1;
                while(counter >= 0)
                {
                    if(goingDown)
                    {
                        for(int i = 0; i < railLength; i++)
                        {
                            rails[counter][i] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter--;
                        goingDown = false;
                        continue;
                    }
                    else
                    {
                        for(int i = railLength - 1; i >= 0; i--)
                        {
                            rails[counter][i] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter--;
                        goingDown = true;
                        continue;
                    }
                }
                break;
            }
            case 22: //vertical back-and-forth starting bottom left.
            {
                counter = 0;
                goingDown = false;
                while(counter < numberOfRails)
                {
                    if(goingDown)
                    {
                        for(int i = 0; i < railLength; i++)
                        {
                            rails[counter][i] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter++;
                        goingDown = false;
                        continue;
                    }
                    else
                    {
                        for(int i = railLength - 1; i >= 0; i--)
                        {
                            rails[counter][i] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter++;
                        goingDown = true;
                        continue;
                    }
                }
                break;
            }
            case 23: //vertical back-and-forth starting top left.
                {
                counter = 0;
                goingDown = true;
                while(counter < numberOfRails)
                {
                    if(goingDown)
                    {
                        for(int i = 0; i < railLength; i++)
                        {
                            rails[counter][i] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter++;
                        goingDown = false;
                        continue;
                    }
                    else
                    {
                        for(int i = railLength - 1; i >= 0; i--)
                        {
                            rails[counter][i] = message.substring(messagePosition, messagePosition + 1);
                            messagePosition++;
                        }
                        counter++;
                        goingDown = true;
                        continue;
                    }
                }
                break;
            }
            default:
            {
                System.out.println("Invalid route!");
                return myMessage;
            }
        }
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
    public static void main(String[] args)
    {
        String myMessage = "HelloWorld";
        String myCipherMessage = RouteCipher.encode(myMessage, 3, true, true, 2, "ABBBB");
        //Top left expected: HLODAROELWLA
	System.out.println("Message: " + myMessage);
	System.out.println("Cipher: " + myCipherMessage);
        String myDecipheredMessage = RouteCipher.decode(myCipherMessage, 3, true, true, 2, "ABBBB");
        System.out.println("Decoded: " + myDecipheredMessage);
    }
}