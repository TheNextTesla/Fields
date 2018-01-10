package independent_study.fields.network;

import android.content.Intent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Stream;

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

public class MultiGameScreen extends GameScreen implements Networked
{
    private InputStream inputStream;
    private OutputStream outputStream;
    private boolean isHost;

    public MultiGameScreen(Game game, InputStream input, OutputStream output, boolean host)
    {
        super(game);
        inputStream = input;
        outputStream = output;
        isHost = host;
    }

    public void update(float deltaTime)
    {
        super.update(deltaTime);
    }

    public void paint(float deltaTime)
    {
        super.paint(deltaTime);
    }

    public void pause()
    {
        super.pause();
    }

    public void resume()
    {
        super.resume();
    }

    public void dispose()
    {
        super.dispose();
    }

    public void backButton()
    {
        Intent intent = new Intent(game.getActivity(), FieldGame.class);
        game.getActivity().startActivity(intent);
    }

    public void connected()
    {
        //Should Already Be Connected on Start
    }

    public void disconnected()
    {
        //game.setScreen(new TitleScreen(game));
        backButton();
    }

    public void updateNetworkInformation()
    {
        GameUpdate gameUpdate;

        if(isHost)
        {
            gameUpdate = GameUpdate.generateHostGameUpdate(playerSprite, obstacleSpriteManager.getObstacles());
        }
        else
        {
            gameUpdate = GameUpdate.generateSimpleGameUpdate(playerSprite);
        }

        try
        {
            outputStream.write(gameUpdate.toJSON().toString().getBytes());
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }
}
