package independent_study.fields.game;

import android.app.FragmentManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidInput;
import independent_study.fields.framework.Screen;
import independent_study.fields.sprites.ObstacleSprite;
import independent_study.fields.sprites.ObstacleSpriteManager;
import independent_study.fields.sprites.PlayerSprite;
import independent_study.fields.sprites.WallSprite;

/**
 * Created by Blaine Huey on 11/6/2017.
 */

public class GameScreen extends Screen
{
    private static final String LOG_TAG = "GameScreen";
    private enum CHARGE_STATE {POSITIVE, NUETRAL, NEGATIVE}

    private AndroidGraphics graphics;
    private AndroidInput input;
    private boolean wasTouchedDownLast;
    private boolean wasPositiveLast;
    private CHARGE_STATE chargeState;
    private WallSprite wallSpriteL;
    private WallSprite wallSpriteR;
    private PlayerSprite playerSprite;
    private ObstacleSpriteManager obstacleSpriteManager;
    private Rect gameRegion;

    public GameScreen(AndroidGame game)
    {
        super(game);
        input = game.getInput();
        graphics = game.getGraphics();
        wasTouchedDownLast = false;
        wallSpriteL = WallSprite.generateDefault(WallSprite.DEFAULT_WALL_TYPE.LEFT, graphics);
        wallSpriteR = WallSprite.generateDefault(WallSprite.DEFAULT_WALL_TYPE.RIGHT, graphics);
        playerSprite = new PlayerSprite(graphics);
        obstacleSpriteManager = new ObstacleSpriteManager(0, 0, game);

        gameRegion = new Rect((Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2, 0,
                (Configuration.FIELD_WIDTH + (Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2),
                Configuration.GAME_HEIGHT);

        graphics.clearScreen(Color.GRAY);
        Log.d(LOG_TAG, "Constructed");
}

    public void update(float deltaTime)
    {
        boolean isTouchedDown = false;
        for(AndroidInput.TouchEvent touchEvent : input.getTouchEvents())
        {
            if(input.inRectBounds(touchEvent, gameRegion))
            {
                if(touchEvent.type == AndroidInput.TouchEvent.TOUCH_DOWN)
                {
                    isTouchedDown = true;
                }
                else
                {
                    Log.d(LOG_TAG, "Touch At X: " + touchEvent.x + " Y: " + touchEvent.y);
                }
            }
        }

        if(isTouchedDown && !wasTouchedDownLast)
        {
            chargeState = CHARGE_STATE.NUETRAL;
        }
        else if(!isTouchedDown && wasTouchedDownLast)
        {
            if(wasPositiveLast)
            {
                chargeState = CHARGE_STATE.NEGATIVE;
            }
            else
            {
                chargeState = CHARGE_STATE.POSITIVE;
            }
        }

        playerSprite.update();
        wallSpriteR.update();
        wallSpriteL.update();
        obstacleSpriteManager.updateGenerateObstacle();
        obstacleSpriteManager.updateAllObstacles();
    }

    public void paint(float deltaTime)
    {
        wallSpriteR.paint();
        wallSpriteL.paint();
        playerSprite.paint();
        obstacleSpriteManager.paintAllObstacles();
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
