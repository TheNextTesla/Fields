package independent_study.fields.network;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.Payload;

import independent_study.fields.framework.Screen;

/**
 * Created by Blaine Huey on 12/13/2017.
 */

/**
 * @see "https://developer.android.com/training/connect-devices-wirelessly/index.html"
 * @see "https://developers.google.com/android/guides/setup"
 */
public class FieldGameMultiplayer extends NetworkedAndroidGame
{
    private static final String LOG_TAG = "FieldGameMultiplayer";
    private static final String HOST_MESSAGE = "Confirming Connection";

    private int objectiveScore;
    private int timeScore;

    private boolean isHost;
    private boolean connectionConfirmed;
    private String lastReceivedString;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        clearGameScore();

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
        lastReceivedString = null;
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

        Log.d(LOG_TAG, "onConnectionFailed");
    }

    /** {@see ConnectionsActivity#onReceive(Endpoint, Payload)} */
    @Override
    protected void onReceive(Endpoint endpoint, Payload payload)
    {
        Log.d(LOG_TAG, "onReceive");
        super.onReceive(endpoint, payload);

        //https://developers.google.com/nearby/connections/android/exchange-data
        if(payload.getType() == Payload.Type.BYTES)
        {
            String receivedString = null;
            try
            {
                receivedString = new String(payload.asBytes());
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            if(receivedString != null)
            {
                if(receivedString.equals(HOST_MESSAGE))
                {
                    connectionConfirmed = true;
                    Toast.makeText(getApplicationContext(), HOST_MESSAGE, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    lastReceivedString = receivedString;
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
    }

    public boolean getHostStatus()
    {
        return isHost;
    }

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

    /**
     *
     * @see "https://developers.google.com/android/reference/com/google/android/gms/nearby/connection/Payload"
     * @param string
     */
    public void sendString(String string)
    {
        if(getState() == State.CONNECTED && mEstablishedConnections.size() != 0)
            send(Payload.fromBytes(string.getBytes()));
    }
}
