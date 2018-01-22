package independent_study.fields.network;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.nearby.connection.ConnectionInfo;

import java.util.Collection;
import java.util.Random;

import independent_study.fields.R;
import independent_study.fields.framework.AndroidAudio;
import independent_study.fields.framework.AndroidFastRenderView;
import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidInput;
import independent_study.fields.framework.Game;
import independent_study.fields.framework.Screen;

public abstract class NetworkedAndroidGame extends ConnectionsActivity implements Game
{
    private static final long ADVERTISING_DURATION = 30000;
    private static final String SERVICE_ID = "independent_study.fields.network.NetworkedAndroidGame.SERVICE_ID";

    /** States that the UI goes through. */
    public enum State
    {
        UNKNOWN,
        DISCOVERING,
        ADVERTISING,
        CONNECTED
    }

    protected AndroidFastRenderView renderView;
    protected AndroidGraphics graphics;
    protected AndroidAudio audio;
    protected AndroidInput input;
    protected Screen screen;
    protected WakeLock wakeLock;
    private String networkName;
    private volatile State networkState = State.UNKNOWN;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        //int frameBufferWidth = isPortrait ? independent_study.fields.game.Configuration.GAME_HEIGHT : independent_study.fields.game.Configuration.GAME_WIDTH;
        //int frameBufferHeight = isPortrait ? independent_study.fields.game.Configuration.GAME_WIDTH: independent_study.fields.game.Configuration.GAME_HEIGHT;
        Bitmap frameBuffer = Bitmap.createBitmap(independent_study.fields.game.Configuration.GAME_WIDTH,
                independent_study.fields.game.Configuration.GAME_HEIGHT, Config.RGB_565);
        
        float scaleX = (float) independent_study.fields.game.Configuration.GAME_WIDTH
                / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) independent_study.fields.game.Configuration.GAME_HEIGHT
                / getWindowManager().getDefaultDisplay().getHeight();

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        //fileIO = new AndroidFileIO(this);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        screen = getInitScreen();
        setContentView(renderView);
        
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "FieldMultiplayer");

        networkName = generateRandomName();
    }

    @Override
    public void onStart()
    {
        setState(State.UNKNOWN);
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    public AndroidInput getInput()
    {
        return input;
    }

    /*
    public FileIO getFileIO()
    {
        return fileIO;
    }
    */

    public AndroidGraphics getGraphics()
    {
        return graphics;
    }

    public AndroidAudio getAudio()
    {
        return audio;
    }

    public void setScreen(Screen screen)
    {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }
    
    public Screen getCurrentScreen()
    {
    	return screen;
    }

    public abstract Screen getInitScreen();

    public Activity getActivity()
    {
        return this;
    }

    /**
     * We've connected to Nearby Connections. We can now start calling {@link #startDiscovering()} and
     * {@link #startAdvertising()}.
     */
    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        super.onConnected(bundle);
        setState(State.DISCOVERING);
    }

    /** We were disconnected! Halt everything! */
    @Override
    public void onConnectionSuspended(int reason)
    {
        super.onConnectionSuspended(reason);
        setState(State.UNKNOWN);
    }

    /**
     *
     * @param endpoint
     */
    @Override
    protected void onEndpointDiscovered(Endpoint endpoint)
    {
        super.onEndpointDiscovered(endpoint);
        // We found an advertiser!
        connectToEndpoint(endpoint);
    }

    /**
     *
     * @param endpoint
     * @param connectionInfo
     */
    @Override
    protected void onConnectionInitiated(Endpoint endpoint, ConnectionInfo connectionInfo)
    {
        super.onConnectionInitiated(endpoint, connectionInfo);
        // A connection to another device has been initiated! We'll accept the connection immediately.
        acceptConnection(endpoint);
    }

    /**
     *
     * @param endpoint
     */
    @Override
    protected void onEndpointConnected(Endpoint endpoint)
    {
        super.onEndpointConnected(endpoint);
        Toast.makeText(this, getString(R.string.toast_connected) + endpoint.getName(), Toast.LENGTH_SHORT).show();
        setState(State.CONNECTED);
    }

    /**
     *
     * @param endpoint
     */
    @Override
    protected void onEndpointDisconnected(Endpoint endpoint)
    {
        super.onEndpointDisconnected(endpoint);
        Toast.makeText(this, getString(R.string.toast_disconnected) + endpoint.getName(), Toast.LENGTH_SHORT).show();

        // If we lost all our endpoints, then we should reset the state of our app and go back
        // to our initial state (discovering).
        if (getConnectedEndpoints().isEmpty())
        {
            setState(State.UNKNOWN);
            //setState(State.DISCOVERING);
        }
    }

    /**
     *
     * @param endpoint
     */
    @Override
    protected void onConnectionFailed(Endpoint endpoint)
    {
        super.onConnectionFailed(endpoint);
        // Let's try someone else.
        if (getState() == State.DISCOVERING && !getDiscoveredEndpoints().isEmpty())
        {
            connectToEndpoint(pickRandomElem(getDiscoveredEndpoints()));
        }
    }

    /**
     * The state has changed. I wonder what we'll be doing now.
     *
     * @param state The new state.
     */
    public void setState(State state)
    {
        if (networkState == state)
        {
            logW("State set to " + state + " but already in that state");
            return;
        }

        logD("State set to " + state);
        State oldState = networkState;
        networkState = state;
        onStateChanged(oldState, state);
    }

    /**
     *
     * @return
     */
    public State getState()
    {
        return networkState;
    }

    /**
     * State has changed.
     *
     * @param oldState The previous state we were in. Clean up anything related to this state.
     * @param newState The new state we're now in. Prepare the UI for this state.
     */
    private void onStateChanged(State oldState, State newState)
    {
        if(oldState == newState)
            return;

        // Update Nearby Connections to the new state.
        switch (newState)
        {
            case DISCOVERING:
                if (isAdvertising())
                {
                    stopAdvertising();
                }
                disconnectFromAllEndpoints();
                startDiscovering();
                break;
            case ADVERTISING:
                if (isDiscovering())
                {
                    stopDiscovering();
                }
                disconnectFromAllEndpoints();
                startAdvertising();
                break;
            case CONNECTED:
                if (isDiscovering())
                {
                    stopDiscovering();
                }
                else if (isAdvertising())
                {
                    stopAdvertising();
                }
                break;
            case UNKNOWN:
                //TODO: Is this case needed?  Added in Troubleshooting
                if (isDiscovering())
                {
                    stopDiscovering();
                }
                if (isAdvertising())
                {
                    stopAdvertising();
                }
                disconnectFromAllEndpoints();
            default:
                // no-op
                break;
        }
    }

    private static String generateRandomName()
    {
        StringBuilder builder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 5; i++)
        {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    private static <T> T pickRandomElem(Collection<T> collection)
    {
        return (T) collection.toArray()[new Random().nextInt(collection.size())];
    }

    @Override
    public String getServiceId()
    {
        return SERVICE_ID;
    }

    /**
     * Queries the phone's contacts for their own profile, and returns their name. Used when
     * connecting to another device.
     */
    @Override
    protected String getName()
    {
        return networkName;
    }
}
