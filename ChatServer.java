import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Created by gkwh on 09/04/2016.
 */
public class ChatServer
{
    public static void main(String[] args)
    {
        try
        {
            int i = 1;
            ServerSocket s = new ServerSocket(8189);
            Context context = new Context();

            System.out.println("The server is running ...");
            System.out.println("Waiting for users");

            while (true)
            {

                Socket incoming = s.accept();
                System.out.println("Spawning " + i);

                String currentNameUser = registerUserAtSystem( incoming, context );

                Runnable r = new ThreadedChatHandler(incoming,context, currentNameUser);
                Thread t = new Thread(r);
                t.start();
                i++;

                System.out.println("The user " + currentNameUser + " enter to the chat");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    static String registerUserAtSystem(  Socket incoming, Context theContext )
    {
        String nickName  = null;
        try
        {
            InputStream inStream = incoming.getInputStream();
            OutputStream outStream = incoming.getOutputStream();

            Scanner in = new Scanner(inStream, "UTF-8");
            PrintWriter out = new PrintWriter(
                    new OutputStreamWriter(outStream, "UTF-8"),
                    true /* autoFlush */);

            out.println( "------------------------------------" );
            out.println( "\tWelcome to the Chat!!!" );
            out.println( "------------------------------------" );

            while( true ) {
                out.println("Please insert your nickname: ");
                nickName = in.next().trim();

                if (theContext.addUser(nickName, outStream)) {
                    out.println("\n\nYour nickname registered is : " + nickName);
                    break;
                }else
                    out.println("\n\nnickname " + nickName + "is already registered,  pick  another one");
            }
            out.println("------------------------------\n\n");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return nickName;
    }
}
