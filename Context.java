import java.io.OutputStream;
import java.util.*;

/**
 * Created by gkwh on 09/04/2016.
 */
public class Context
{
    private Map<String, OutputStream> mapUserOutStream =  new HashMap<String,OutputStream>();

    boolean addUser( String theNameUser, OutputStream theOutStream )
    {
        if( !mapUserOutStream.containsKey( theNameUser )  ){
            mapUserOutStream.put( theNameUser, theOutStream );
            return true;
        }

        return false;
    }

    boolean removeUser( String theNameUser   )
    {
        mapUserOutStream.remove( theNameUser );
        return false;
    }

    OutputStream getOutStream( String theNameUser  )
    {
        if(  mapUserOutStream.containsKey( theNameUser) )
            return mapUserOutStream.get( theNameUser );

        return null;
    }

    Set<String> getUsersAtSystem()
    {
        return mapUserOutStream.keySet();
    }

}
