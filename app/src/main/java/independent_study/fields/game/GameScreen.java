package independent_study.fields.game;

import android.app.FragmentManager;
import android.graphics.Color;
import android.util.Log;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.Screen;
import independent_study.fields.sprites.ObstacleSpriteManager;
import independent_study.fields.sprites.PlayerSprite;

/**
 * Created by Blaine Huey on 11/6/2017.
 */

public class GameScreen extends Screen
{
    private static final String LOG_TAG = "GameScreen";

    private AndroidGraphics graphics;
    private PlayerSprite playerSprite;
    private ObstacleSpriteManager obstacleSpriteManager;

    public GameScreen(AndroidGame game)
    {
        super(game);
        graphics = game.getGraphics();
        playerSprite = new PlayerSprite();
        obstacleSpriteManager = new ObstacleSpriteManager(0, 0);

        Log.d(LOG_TAG, "Constructed");
    }

    public void update(float deltaTime)
    {
        obstacleSpriteManager.updateGenerateObstacle();
    }

    public void paint(float deltaTime)
    {
        graphics.clearScreen(Color.GREEN);
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

    }
}
