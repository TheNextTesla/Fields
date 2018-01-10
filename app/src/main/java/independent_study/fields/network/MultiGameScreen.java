package independent_study.fields.network;

import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
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
import independent_study.fields.sprites.Sprite;

/**
 * Created by Blaine Huey on 12/11/2017.
 */

public class MultiGameScreen extends GameScreen implements Networked
{
    private InputStream inputStream;
    private OutputStream outputStream;
    private String receivedString;
    private ArrayList<Sprite> otherSprites;
    private boolean isHost;

    public MultiGameScreen(Game game, InputStream input, OutputStream output, boolean host)
    {
        super(game);
        inputStream = input;
        outputStream = output;
        isHost = host;
        receivedString = "";
    }

    public void update(float deltaTime)
    {
        super.update(deltaTime);

        receivedString = retrieveNetworkInformation();
        GameUpdate gameUpdate = GameUpdate.regenerateGameUpdate(receivedString);
        otherSprites = gameUpdate.enact(game);
        for(Sprite tempSprite : otherSprites)
        {
            tempSprite.paint();
        }
        updateNetworkInformation();
    }

    public void paint(float deltaTime)
    {
        super.paint(deltaTime);
        for(Sprite tempSprite : otherSprites)
        {
            tempSprite.destroy();
        }
        otherSprites = null;
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

    private void updateNetworkInformation()
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

    private String retrieveNetworkInformation()
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
            {
                out.append(line);
            }
            return out.toString();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return "";
    }
}
