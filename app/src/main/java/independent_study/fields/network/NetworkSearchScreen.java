package independent_study.fields.network;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidInput;
import independent_study.fields.framework.Game;
import independent_study.fields.framework.Screen;
import independent_study.fields.game.Configuration;
import independent_study.fields.game.FieldGame;
import independent_study.fields.game.GameScreen;
import independent_study.fields.game.TitleScreen;
import independent_study.fields.settings.SettingsActivity;

/**
 * Created by Blaine Huey on 12/11/2017.
 */

public class NetworkSearchScreen extends Screen implements Networked
{
    private static final String LOG_TAG = "NetworkSearchScreen";

    private FieldGameMultiplayer gameMultiplayer;
    private AndroidGraphics graphics;
    private Rect hostButton;
    private Rect simpleButton;
    private Paint textPaint;
    private boolean isSearching;
    private boolean isSearchingHost;

    public NetworkSearchScreen(Game game)
    {
        super(game);

        gameMultiplayer = (FieldGameMultiplayer) game;
        graphics = game.getGraphics();

        hostButton = new Rect(Configuration.GAME_WIDTH / 3,
                Configuration.GAME_HEIGHT / 5,
                Configuration.GAME_WIDTH /3 * 2,
                Configuration.GAME_HEIGHT / 5 * 2);
        simpleButton = new Rect(Configuration.GAME_WIDTH / 3,
                Configuration.GAME_HEIGHT / 5 * 3,
                Configuration.GAME_WIDTH / 3 * 2,
                Configuration.GAME_HEIGHT / 5 * 4);

        textPaint = new Paint();
        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLUE);

        isSearching = false;
        isSearchingHost = false;
    }

    public void update(float deltaTime)
    {
        boolean touchTriggered = false;
        boolean isHostSelection = false;
        for(AndroidInput.TouchEvent event : game.getInput().getTouchEvents())
        {
            if(event.type == AndroidInput.TouchEvent.TOUCH_DOWN)
            {
                Rect point = new Rect(event.x, event.y, event.x, event.y);
                if(Rect.intersects(hostButton, point))
                {
                   touchTriggered = true;
                   isHostSelection = true;
                }
                else if(Rect.intersects(simpleButton, point))
                {
                    touchTriggered = true;
                }
            }
        }

        if(touchTriggered)
        {
            isSearching = true;

            if(isHostSelection)
            {
                isSearchingHost = true;
            }

            graphics.clearScreen(Color.BLACK);
        }

        if(gameMultiplayer.getState() == NetworkedAndroidGame.State.CONNECTED)
        {
            Log.d(LOG_TAG, "Still Connected.");
            if(gameMultiplayer.getInputStream() != null && gameMultiplayer.getOutputStream() != null)
            {
                game.setScreen(new MultiGameScreen(game, gameMultiplayer.getInputStream(), gameMultiplayer.getOutputStream(), gameMultiplayer.getHostStatus()));
            }
        }
        else if(isSearching && isSearchingHost)
        {
            gameMultiplayer.setHostStatus(true);
            if(gameMultiplayer.getState() != NetworkedAndroidGame.State.ADVERTISING)
                gameMultiplayer.setState(NetworkedAndroidGame.State.ADVERTISING);
        }
        else if(isSearching)
        {
            gameMultiplayer.setHostStatus(false);
            if(gameMultiplayer.getState() != NetworkedAndroidGame.State.DISCOVERING)
                gameMultiplayer.setState(NetworkedAndroidGame.State.DISCOVERING);
        }
        else if(gameMultiplayer.getState() != NetworkedAndroidGame.State.UNKNOWN)
        {
            gameMultiplayer.setState(NetworkedAndroidGame.State.UNKNOWN);
        }
    }

    public void paint(float deltaTime)
    {
        if(isSearching && isSearchingHost)
        {
            graphics.drawString("BROADCASTING", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 2, textPaint);
        }
        else if(isSearching)
        {
            if(gameMultiplayer.getState() == NetworkedAndroidGame.State.CONNECTED)
            {
                //TODO: Stream Setup
                Toast.makeText(game.getActivity().getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                //game.setScreen(new MultiGameScreen(game, , , isSearchingHost));
            }
            else
            {
                graphics.drawString("SEARCHING", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 2, textPaint);
            }
        }
        else
        {
            graphics.drawRectObject(hostButton, Color.GRAY);
            graphics.drawRectObject(simpleButton, Color.GRAY);
            graphics.drawString("Host", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 10 * 3, textPaint);
            graphics.drawString("Connect", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 10 * 7, textPaint);
            graphics.drawString("Select a Connection Option", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 10, textPaint);
        }
    }

    public void pause()
    {

    }

    public void resume()
    {

    }

    public void dispose()
    {

    }

    public void backButton()
    {
        if(isSearching)
        {
            isSearching = false;
            isSearchingHost = false;
            graphics.clearScreen(Color.BLACK);
        }
        else
        {
            Intent intent = new Intent(game.getActivity(), FieldGame.class);
            game.getActivity().startActivity(intent);
        }
    }

    public void connected()
    {

    }

    public void disconnected()
    {

    }
}
