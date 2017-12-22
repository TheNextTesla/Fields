package independent_study.fields.game;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Locale;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidInput;
import independent_study.fields.framework.Game;
import independent_study.fields.framework.Screen;
import independent_study.fields.sprites.ObstacleSprite;
import independent_study.fields.sprites.ObstacleSpriteManager;
import independent_study.fields.sprites.PlayerSprite;
import independent_study.fields.sprites.Sprite;
import independent_study.fields.sprites.WallSprite;

/**
 * Created by Blaine Huey on 11/6/2017.
 */

public class GameScreen extends Screen
{
    private static final String LOG_TAG = "GameScreen";

    protected AndroidGraphics graphics;
    protected AndroidInput input;
    protected boolean wasPositiveLast;
    protected boolean hasBeenTouchedYet;
    protected long startTime;
    protected WallSprite wallSpriteL;
    protected WallSprite wallSpriteR;
    protected PlayerSprite playerSprite;
    protected ObstacleSpriteManager obstacleSpriteManager;
    protected Rect gameRegion;
    protected SharedPreferences sharedPreferences;
    protected SharedPreferences.Editor settingsEditor;
    protected Paint scorePaint;

    public GameScreen(Game game)
    {
        super(game);

        //https://stackoverflow.com/questions/5051739/android-setting-preferences-programmatically
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(game.getActivity().getApplicationContext());
        settingsEditor = sharedPreferences.edit();

        input = game.getInput();
        graphics = game.getGraphics();
        wallSpriteL = WallSprite.generateDefault(WallSprite.DEFAULT_WALL_TYPE.LEFT, sharedPreferences.getBoolean(Configuration.POSITIVE_PLATE_LEFT_TAG, true), graphics);
        wallSpriteR = WallSprite.generateDefault(WallSprite.DEFAULT_WALL_TYPE.RIGHT, !sharedPreferences.getBoolean(Configuration.POSITIVE_PLATE_LEFT_TAG, true), graphics);
        playerSprite = new PlayerSprite(game, sharedPreferences.getBoolean(Configuration.POSITIVE_PLATE_LEFT_TAG, true));

        int speedPreferenceMultiplier = Integer.valueOf(sharedPreferences.getString(Configuration.OBSTACLE_VELOCITY_TAG, "2"));
        ObstacleSprite.OBSTACLE_SPEED speedMultiplier;
        if(speedPreferenceMultiplier == 1)
        {
            speedMultiplier = ObstacleSprite.OBSTACLE_SPEED.SLOW;
        }
        else if(speedPreferenceMultiplier == 3)
        {
            speedMultiplier = ObstacleSprite.OBSTACLE_SPEED.FAST;
        }
        else
        {
            speedMultiplier = ObstacleSprite.OBSTACLE_SPEED.NORMAL;
        }
        obstacleSpriteManager = new ObstacleSpriteManager(speedMultiplier, game);

        gameRegion = new Rect((Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2, 0,
                (Configuration.FIELD_WIDTH + (Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2),
                Configuration.GAME_HEIGHT);

        if(sharedPreferences.getLong(Configuration.HIGH_SCORE_TAG, -1) == -1L)
        {
            settingsEditor.putLong(Configuration.HIGH_SCORE_TAG, 0L);
            settingsEditor.apply();
        }

        scorePaint = new Paint();
        scorePaint.setTextSize(20);
        scorePaint.setTextAlign(Paint.Align.CENTER);
        scorePaint.setAntiAlias(true);
        scorePaint.setColor(Color.WHITE);

        graphics.clearScreen(Color.GRAY);

        startTime = System.currentTimeMillis();
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
        obstacleSpriteManager.updateGenerateObstacleAndObjective();
        obstacleSpriteManager.updateAllObstacles();
        Sprite.touchCheckAll();

        System.gc();
    }

    public void paint(float deltaTime)
    {
        wallSpriteR.paint();
        wallSpriteL.paint();
        playerSprite.paint();
        obstacleSpriteManager.paintAllObstacles();

        game.setTimeScore((int) Math.floor((System.currentTimeMillis() - startTime) / (1000.0)));
        graphics.drawString(String.format(Locale.US, "C-Score: %d", game.getGameScore()), (Configuration.FIELD_WIDTH - 15), 40, scorePaint);

        if(game.getGameScore() > sharedPreferences.getLong(Configuration.HIGH_SCORE_TAG, 0L))
        {
            settingsEditor.putLong(Configuration.HIGH_SCORE_TAG, game.getGameScore());
            settingsEditor.apply();
        }

        graphics.drawString(String.format(Locale.US, "H-Score: %d", sharedPreferences.getLong(Configuration.HIGH_SCORE_TAG, 0L)), (Configuration.FIELD_WIDTH  - 15), 65, scorePaint);
    }

    public void pause()
    {

    }

    public void resume()
    {

    }

    public void dispose()
    {
        //playerSprite.destroy(); - Player Sprite Destruction Triggers This
        wallSpriteR.destroy();
        wallSpriteL.destroy();
        obstacleSpriteManager.deleteAllObstacles();
        //fieldGame.clearGameScore();
    }

    public void backButton()
    {

    }
}
