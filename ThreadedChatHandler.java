import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by gkwh on 09/04/2016.
 */
public class ThreadedChatHandler implements Runnable
{
    private String nameUser;
    private Socket incoming;
    Context globalContext;

    public ThreadedChatHandler(Socket i, Context theContext, String theNameUser)
    {
        incoming = i;
        globalContext =  theContext;
        nameUser = theNameUser;
    }

    public void run()
    {
        try
        {
            try
            {
                InputStream inStream = incoming.getInputStream();
                OutputStream outStream = incoming.getOutputStream();

                Scanner in = new Scanner(inStream, "UTF-8");
                PrintWriter out = new PrintWriter(
                        new OutputStreamWriter(outStream, "UTF-8"),
                        true /* autoFlush */);

                out.println( " *** Enter WHOAMI to display your nickname." );
                out.println( " *** Enter WHO to display current users connected to the system." );
                out.println( " *** Enter BYE to exit." );

                out.println(" ------------------------------\n\n");

                // echo client input
                boolean done = false;
                while (!done && in.hasNextLine())
                {
                    String line = in.nextLine();

                    if ( line.trim().equals("WHO") )
                    {
                        out.println("------------------------------\n");
                        out.println(" Users connected: \n");

                        Set<String> usersName = globalContext.getUsersAtSystem();
                        for (String x : usersName)
                            out.println("\t"+x);

                        out.println("\n------------------------------\n");

                        continue;
                    }

                    if ( line.trim().equals("WHOAMI") )
                    {
                        out.println("------------------------------\n");
                        out.println("  you are : " + nameUser );
                        out.println(" ------------------------------\n");

                        continue;
                    }

                    if (line.trim().equals("BYE")) {
                        done = true;
                        continue;
                    }

                    String[] parts =  line.split(":", 2);

                    OutputStream outStreamToUser = globalContext.getOutStream(  parts[0].trim() );

                    if( outStreamToUser ==  null )
                    {
                        out.println("\tThe user "+ parts[0] +" is unknown or is disconnected");
                        continue;
                    }
                    else{
                        PrintWriter outToUser = new PrintWriter(
                                new OutputStreamWriter(outStreamToUser, "UTF-8"),
                                true /* autoFlush */);

                        outToUser.println("<<< " + nameUser + " says : \n    " + parts[1]);
                    }
                }
            }
            finally
            {
                globalContext.removeUser( nameUser );
                incoming.close();
                System.out.println("The user " +  nameUser + " leave the chat");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


}
