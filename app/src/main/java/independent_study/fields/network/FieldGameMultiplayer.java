package independent_study.fields.network;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.Payload;

import independent_study.fields.framework.Screen;
import independent_study.fields.network.MultiGameScreen;
import independent_study.fields.network.NetworkSearchScreen;
import independent_study.fields.network.NetworkedAndroidGame;

/**
 * Created by Blaine Huey on 12/13/2017.
 */

public class FieldGameMultiplayer extends NetworkedAndroidGame
{
    private int objectiveScore;
    private int timeScore;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        clearGameScore();
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
    }

    @Override
    public void onConnectionSuspended(int reason)
    {
        super.onConnectionSuspended(reason);
    }

    @Override
    protected void onEndpointDiscovered(Endpoint endpoint)
    {
        super.onEndpointDiscovered(endpoint);
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
    }

    @Override
    protected void onEndpointConnected(Endpoint endpoint)
    {
        super.onEndpointConnected(endpoint);
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
    }

    /** {@see ConnectionsActivity#onReceive(Endpoint, Payload)} */
    @Override
    protected void onReceive(Endpoint endpoint, Payload payload)
    {
        super.onReceive(endpoint, payload);

    }
}
