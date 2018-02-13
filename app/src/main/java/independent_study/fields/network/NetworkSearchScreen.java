package independent_study.fields.network;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidInput;
import independent_study.fields.framework.Game;
import independent_study.fields.framework.Screen;
import independent_study.fields.game.Configuration;
import independent_study.fields.ui.TextBoxButton;

/**
 * Created by Blaine Huey on 12/11/2017.
 */

public class NetworkSearchScreen extends Screen
{
    private static final String LOG_TAG = "NetworkSearchScreen";

    private FieldGameMultiplayer gameMultiplayer;
    private AndroidGraphics graphics;
    private Rect hostButtonRect;
    private Rect simpleButtonRect;
    private TextBoxButton hostButton;
    private TextBoxButton simpleButton;
    private Paint textPaint;
    private boolean isSearching;
    private boolean isSearchingHost;
    private boolean didTryStart;

    public NetworkSearchScreen(Game game)
    {
        super(game);

        gameMultiplayer = (FieldGameMultiplayer) game;
        graphics = game.getGraphics();

        hostButtonRect = new Rect(Configuration.GAME_WIDTH / 3,
                Configuration.GAME_HEIGHT / 5,
                Configuration.GAME_WIDTH /3 * 2,
                Configuration.GAME_HEIGHT / 5 * 2);
        simpleButtonRect = new Rect(Configuration.GAME_WIDTH / 3,
                Configuration.GAME_HEIGHT / 5 * 3,
                Configuration.GAME_WIDTH / 3 * 2,
                Configuration.GAME_HEIGHT / 5 * 4);

        hostButton = new TextBoxButton(hostButtonRect, Color.GRAY, "Host", 30, Color.BLUE, game);
        simpleButton = new TextBoxButton(simpleButtonRect, Color.GRAY, "Connect", 30, Color.BLUE, game);

        textPaint = new Paint();
        textPaint.setTextSize(30);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLUE);

        isSearching = false;
        isSearchingHost = false;
        didTryStart = false;
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
                if(hostButton.isPressed(point))
                {
                   touchTriggered = true;
                   isHostSelection = true;
                }
                else if(simpleButton.isPressed(point))
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

        if(gameMultiplayer.getState() == NetworkedAndroidGame.State.CONNECTED && isSearching)
        {
            if(gameMultiplayer.isConnectionConfirmed())
            {
                game.setScreen(new MultiGameScreen(game, gameMultiplayer.getHostStatus()));
            }
            else if(!didTryStart && isSearchingHost)
            {
                didTryStart = true;
                gameMultiplayer.startGameHosting();
            }
            else
            {
                graphics.drawString("" + System.currentTimeMillis(), 200, 100, textPaint);
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
        else if(didTryStart)
        {
            didTryStart = false;
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
            if(gameMultiplayer.getState() != NetworkedAndroidGame.State.CONNECTED)
            {
                graphics.drawString("SEARCHING", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 2, textPaint);
            }
        }
        else
        {
            hostButton.display();
            simpleButton.display();
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
            game.getActivity().finish();
        }
    }

    public void connected()
    {

    }

    public void disconnected()
    {

    }
}
