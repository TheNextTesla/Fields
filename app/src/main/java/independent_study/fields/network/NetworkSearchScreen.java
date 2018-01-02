package independent_study.fields.network;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.lang.reflect.Field;

import independent_study.fields.framework.AndroidGame;
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
    private Rect hostButton;
    private Rect simpleButton;
    private Paint textPaint;
    private boolean isSearching;
    private boolean isSearchingHost;

    public NetworkSearchScreen(Game game)
    {
        super(game);

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

            game.getGraphics().clearScreen(Color.BLACK);
        }

        if(isSearching && isSearchingHost)
        {
            ((FieldGameMultiplayer) game).setShouldBeHost(true);
            ((FieldGameMultiplayer) game).setState(NetworkedAndroidGame.State.ADVERTISING);
        }
        else if(isSearching)
        {
            ((FieldGameMultiplayer) game).setShouldBeHost(false);
            ((FieldGameMultiplayer) game).setState(NetworkedAndroidGame.State.DISCOVERING);
        }
        else if(((FieldGameMultiplayer) game).getState() != NetworkedAndroidGame.State.UNKNOWN)
        {
            ((FieldGameMultiplayer) game).setState(NetworkedAndroidGame.State.UNKNOWN);
        }
    }

    public void paint(float deltaTime)
    {
        if(isSearching)
        {
            game.getGraphics().drawString("SEARCHING", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 2, textPaint);
        }
        else
        {
            game.getGraphics().drawRectObject(hostButton, Color.GRAY);
            game.getGraphics().drawRectObject(simpleButton, Color.GRAY);
            game.getGraphics().drawString("Host", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 10 * 3, textPaint);
            game.getGraphics().drawString("Connect", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 10 * 7, textPaint);
            game.getGraphics().drawString("Select a Connection Option", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 10, textPaint);
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
            game.getGraphics().clearScreen(Color.BLACK);
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
