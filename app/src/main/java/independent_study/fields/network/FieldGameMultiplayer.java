package independent_study.fields.network;

import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.Payload;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import independent_study.fields.framework.Screen;
import independent_study.fields.network.MultiGameScreen;
import independent_study.fields.network.NetworkSearchScreen;
import independent_study.fields.network.NetworkedAndroidGame;

/**
 * Created by Blaine Huey on 12/13/2017.
 */

public class FieldGameMultiplayer extends NetworkedAndroidGame
{
    private static final String LOG_TAG = "FieldGameMultiplayer";
    private static final String HOST_MESSAGE = "Confirming Connection";

    private int objectiveScore;
    private int timeScore;

    //private ParcelFileDescriptor[] payloadPipe;
    //private InputStream inputStream;
    //private OutputStream outputStream;
    private boolean isHost;
    private boolean connectionConfirmed;
    private String lastReceivedString;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        clearGameScore();

        /*
        payloadPipe = null;
        inputStream = null;
        outputStream = null;
        */
        isHost = false;
        connectionConfirmed = false;
        lastReceivedString = null;
    }

    @Override
    public Screen getInitScreen()
    {
        return new NetworkSearchScreen(this);
    }

    @Override
    public void onBackPressed()
    {
        getCurrentScreen().backButton();
    }

    public int getGameScore()
    {
        return objectiveScore + timeScore;
    }

    public void setTimeScore(int newScore)
    {
        timeScore = newScore;
    }

    public void incrementObjectiveScore(int increase)
    {
        objectiveScore += increase;
    }

    public void clearGameScore()
    {
        objectiveScore = 0;
        timeScore = 0;
    }

    /**
     * We've connected to Nearby Connections. We can now start calling {@link #startDiscovering()} and
     * {@link #startAdvertising()}.
     */
    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        super.onConnected(bundle);
        Log.d(LOG_TAG, "onConnected");
    }

    @Override
    public void onConnectionSuspended(int reason)
    {
        super.onConnectionSuspended(reason);
        Log.d(LOG_TAG, "onEndpointSuspended");
    }

    @Override
    protected void onEndpointDiscovered(Endpoint endpoint)
    {
        super.onEndpointDiscovered(endpoint);
        Log.d(LOG_TAG, "onEndpointDiscovered");
    }

    @Override
    protected void onConnectionInitiated(Endpoint endpoint, ConnectionInfo connectionInfo)
    {
        super.onConnectionInitiated(endpoint, connectionInfo);
        if(screen instanceof NetworkSearchScreen)
        {
            ((NetworkSearchScreen) screen).connected();
        }
        else if(screen instanceof MultiGameScreen)
        {
            ((MultiGameScreen) screen).disconnected();
        }
        Log.d(LOG_TAG, "onConnectionInitiated");
    }

    @Override
    protected void onEndpointConnected(Endpoint endpoint)
    {
        super.onEndpointConnected(endpoint);
        Log.d(LOG_TAG, "onEndpointConnected");
    }

    @Override
    protected void onEndpointDisconnected(Endpoint endpoint)
    {
        super.onEndpointDisconnected(endpoint);
        if(screen instanceof NetworkSearchScreen)
        {
            ((NetworkSearchScreen) screen).disconnected();
        }
        else if(screen instanceof MultiGameScreen)
        {
            ((MultiGameScreen) screen).disconnected();
        }

        //closeStreams();

        /*
        payloadPipe = null;
        inputStream = null;
        outputStream = null;
        */
        Log.d(LOG_TAG, "onEndpointDisconnected");
    }

    @Override
    protected void onConnectionFailed(Endpoint endpoint)
    {
        super.onConnectionFailed(endpoint);
        if(screen instanceof NetworkSearchScreen)
        {
            ((NetworkSearchScreen) screen).disconnected();
        }
        else if(screen instanceof MultiGameScreen)
        {
            ((MultiGameScreen) screen).disconnected();
        }

        closeStreams();

        /*
        payloadPipe = null;
        inputStream = null;
        outputStream = null;
        */
        Log.d(LOG_TAG, "onConnectionFailed");
    }

    /** {@see ConnectionsActivity#onReceive(Endpoint, Payload)} */
    @Override
    protected void onReceive(Endpoint endpoint, Payload payload)
    {
        Log.d(LOG_TAG, "onReceive");
        super.onReceive(endpoint, payload);

        if(payload.getType() == Payload.Type.STREAM)
        {
            /*
            try
            {
                inputStream = payload.asStream().asInputStream();

                if (!isHost)
                {
                    payloadPipe = ParcelFileDescriptor.createPipe();
                    send(Payload.fromStream(payloadPipe[0]));
                    outputStream = new ParcelFileDescriptor.AutoCloseOutputStream(payloadPipe[1]);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
            */
        }
        else if(payload.getType() == Payload.Type.BYTES)
        {
            String recievedString = null;
            try
            {
                recievedString = new String(payload.asBytes());
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            if(recievedString != null)
            {
                if(recievedString.equals(HOST_MESSAGE))
                {
                    connectionConfirmed = true;
                    Toast.makeText(getApplicationContext(), HOST_MESSAGE, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    lastReceivedString = recievedString;
                }
            }
            else
            {
                Log.d(LOG_TAG, "Packet Dropped");
            }
        }
    }

    public void startGameHosting()
    {
        send(Payload.fromBytes(HOST_MESSAGE.getBytes()));
        connectionConfirmed = true;

        /*
        if(isHost)
        {
            try
            {
                Log.d(LOG_TAG, "Start Game Hosting -1");
                payloadPipe = ParcelFileDescriptor.createPipe();
                Log.d(LOG_TAG, "Start Game Hosting -2");
                send(Payload.fromStream(payloadPipe[0]));
                Log.d(LOG_TAG, "Start Game Hosting -3");
                outputStream = new ParcelFileDescriptor.AutoCloseOutputStream(payloadPipe[1]);
                Log.d(LOG_TAG, "Start Game Hosting -4");
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        */
    }

    private void closeStreams()
    {
        /*
        try
        {
            inputStream.close();
            outputStream.close();
            for(ParcelFileDescriptor parcelFileDescriptor : payloadPipe)
            {
                parcelFileDescriptor.close();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        Log.d(LOG_TAG, "closeStreams");
        */
    }

    public boolean getHostStatus()
    {
        return isHost;
    }

    /*
    public InputStream getInputStream()
    {
        return inputStream;
    }

    public OutputStream getOutputStream()
    {
        return outputStream;
    }
    */

    public void setHostStatus(boolean shouldBeHost)
    {
       isHost = shouldBeHost;
    }

    public boolean isConnectionConfirmed()
    {
        return connectionConfirmed;
    }

    public String getLastReceivedString()
    {
        return lastReceivedString;
    }

    public void sendString(String string)
    {
        if(getState() == State.CONNECTED && mEstablishedConnections.size() != 0)
            send(Payload.fromBytes(string.getBytes()));
    }
}
