package independent_study.fields.network;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Stream;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.AndroidInput;
import independent_study.fields.framework.Game;
import independent_study.fields.framework.Screen;
import independent_study.fields.game.Configuration;
import independent_study.fields.game.FieldGame;
import independent_study.fields.game.GameOverScreen;
import independent_study.fields.game.GameScreen;
import independent_study.fields.game.TitleScreen;
import independent_study.fields.settings.SettingsActivity;
import independent_study.fields.sprites.PlayerSprite;
import independent_study.fields.sprites.Sprite;

/**
 * Created by Blaine Huey on 12/11/2017.
 */

public class MultiGameScreen extends GameScreen implements Networked
{
    private static final String GAME_OVER_STRING = "Game Over";
    private static final String LOG_TAG = "MultiGameScreen";

    private FieldGameMultiplayer networkedAndroidGame;
    private String receivedString;
    private ArrayList<Sprite> otherSprites;
    private boolean isHost;

    public MultiGameScreen(Game game, boolean host)
    {
        super(game);
        networkedAndroidGame = (FieldGameMultiplayer) game;
        isHost = host;
        receivedString = "";

        if(!host)
        {
            obstacleSpriteManager = null;
        }
    }

    public void update(float deltaTime)
    {
        boolean isTouchedDown = false;
        boolean isTouchedUp = false;
        for(AndroidInput.TouchEvent touchEvent : input.getTouchEvents())
        {
            if(input.inRectBounds(touchEvent, gameRegion))
            {
                if(touchEvent.type == AndroidInput.TouchEvent.TOUCH_DOWN)
                {
                    if(!hasBeenTouchedYet)
                    {
                        hasBeenTouchedYet = true;
                        if(touchEvent.x > Configuration.GAME_WIDTH / 2)
                        {
                            playerSprite.setChargeState(PlayerSprite.CHARGE_STATE.POSITIVE);
                        }
                        else
                        {
                            playerSprite.setChargeState(PlayerSprite.CHARGE_STATE.NEGATIVE);
                            wasPositiveLast = true;
                        }
                    }
                    else
                    {
                        isTouchedDown = true;
                    }
                }
                else if(touchEvent.type == AndroidInput.TouchEvent.TOUCH_UP)
                {
                    isTouchedUp = true;
                }
                else
                {
                    Log.d(LOG_TAG, "Touch At X: " + touchEvent.x + " Y: " + touchEvent.y);
                }
            }
        }

        if(isTouchedDown || !hasBeenTouchedYet)
        {
            playerSprite.setChargeState(PlayerSprite.CHARGE_STATE.NEUTRAL);
        }
        else if(isTouchedUp)
        {
            if(wasPositiveLast)
            {
                playerSprite.setChargeState(PlayerSprite.CHARGE_STATE.NEGATIVE);
                wasPositiveLast = false;
            }
            else
            {
                playerSprite.setChargeState(PlayerSprite.CHARGE_STATE.POSITIVE);
                wasPositiveLast = true;
            }
        }

        graphics.clearScreen(Color.GRAY);

        playerSprite.update();
        wallSpriteR.update();
        wallSpriteL.update();

        if(isHost)
        {
            obstacleSpriteManager.updateGenerateObstacleAndObjective();
            obstacleSpriteManager.updateAllObstacles();
        }
        Sprite.touchCheckAll();

        System.gc();

        receivedString = retrieveNetworkInformation();
        GameUpdate gameUpdate = GameUpdate.regenerateGameUpdate(receivedString);

        if(gameUpdate != null)
        {
            otherSprites = gameUpdate.enact(game);
            for (Sprite tempSprite : otherSprites)
            {
                tempSprite.paint();
            }
        }
        updateNetworkInformation();
    }

    public void paint(float deltaTime)
    {
        wallSpriteR.paint();
        wallSpriteL.paint();
        playerSprite.paint();

        if(isHost)
            obstacleSpriteManager.paintAllObstacles();

        game.setTimeScore((int) Math.floor((System.currentTimeMillis() - startTime) / (1000.0)));
        graphics.drawString(String.format(Locale.US, "C-Score: %d", game.getGameScore()), (Configuration.FIELD_WIDTH - 15), 40, scorePaint);

        if(game.getGameScore() > sharedPreferences.getLong(Configuration.HIGH_SCORE_TAG, 0L))
        {
            settingsEditor.putLong(Configuration.HIGH_SCORE_TAG, game.getGameScore());
            settingsEditor.apply();
        }

        graphics.drawString(String.format(Locale.US, "H-Score: %d", sharedPreferences.getLong(Configuration.HIGH_SCORE_TAG, 0L)), (Configuration.FIELD_WIDTH  - 15), 65, scorePaint);

        if(otherSprites != null)
        {
            for (Sprite tempSprite : otherSprites)
            {
                tempSprite.destroy();
            }
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

    public void gameOver()
    {
        super.gameOver();

    }

    public void dispose()
    {
        super.dispose();
        endMultiplayerGame();
        networkedAndroidGame.setState(NetworkedAndroidGame.State.UNKNOWN);
    }

    public void backButton()
    {
        dispose();
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

        networkedAndroidGame.sendString(gameUpdate.toJSON().toString());
    }

    private void endMultiplayerGame()
    {
        if(networkedAndroidGame.getState() == NetworkedAndroidGame.State.CONNECTED)
            networkedAndroidGame.sendString(GAME_OVER_STRING);
    }

    private String retrieveNetworkInformation()
    {
        String retrieved = networkedAndroidGame.getLastReceivedString();
        if(retrieved != null && retrieved.equals(GAME_OVER_STRING))
        {
            gameOver();
            networkedAndroidGame.setScreen(new GameOverScreen(game));
            return "";
        }
        else
        {
            return retrieved;
        }
    }
}
