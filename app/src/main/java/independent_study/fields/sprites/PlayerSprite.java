package independent_study.fields.sprites;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Rect;
import android.preference.PreferenceManager;
import android.util.Log;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.game.Configuration;
import independent_study.fields.game.GameOverScreen;

/**
 * Created by Blaine Huey on 11/2/2017.
 */

public class PlayerSprite extends Sprite
{
    private static final String LOG_TAG = "PlayerSprite";
    public enum CHARGE_STATE {POSITIVE, NUETRAL, NEGATIVE}

    public static final int DEFAULT_PLAYER_WIDTH = 30;
    public static final int DEFAULT_PLAYER_HEIGHT = 30;

    public static final double EPSILON_0 = 8.854187e-12;
    public static final double PLAYER_CHARGE = 1.1e-1;
    public static final double PLAYER_MASS = 0.8; //Old 7.8
    public static final double PLATE_CHARGE_DENSITY = 5.8e-8;

    private boolean wasTouched;
    private boolean isPositiveLeft;
    private double playerVelocity;
    private long lastPlayerUpdateTime;
    private CHARGE_STATE chargeState;
    private AndroidGame androidGame;

    public PlayerSprite(int left, int top, int right, int bottom, boolean direction, AndroidGame game)
    {
        super(left, top, right, bottom, game.getGraphics());

        wasTouched = false;
        isPositiveLeft = direction;
        playerVelocity = 0;
        lastPlayerUpdateTime = System.nanoTime();
        chargeState = CHARGE_STATE.POSITIVE;
        androidGame = game;
    }

    public PlayerSprite(AndroidGame game, boolean direction)
    {
        this((Configuration.GAME_WIDTH / 2) + DEFAULT_PLAYER_WIDTH / 2,
                (Configuration.GAME_HEIGHT) - (2 * DEFAULT_PLAYER_HEIGHT),
                (Configuration.GAME_WIDTH / 2) - DEFAULT_PLAYER_WIDTH / 2,
                (Configuration.GAME_HEIGHT - DEFAULT_PLAYER_HEIGHT), direction, game);
    }

    public void setChargeState(CHARGE_STATE newChargeState)
    {
        chargeState = newChargeState;
    }

    @Override
    public void update()
    {
        if(!wasTouched)
        {
            if (System.nanoTime() - lastPlayerUpdateTime > 1_000_000_000L)
            {
                lastPlayerUpdateTime = System.nanoTime();
            }

            if (chargeState == CHARGE_STATE.POSITIVE)
            {
                double acceleration = ((PLATE_CHARGE_DENSITY / EPSILON_0) * PLAYER_CHARGE) / PLAYER_MASS;
                playerVelocity += (isPositiveLeft ? 1 : -1) * acceleration * (System.nanoTime() - lastPlayerUpdateTime) / 1_000_000_000.0;
            }
            else if (chargeState == CHARGE_STATE.NEGATIVE)
            {
                double acceleration = ((PLATE_CHARGE_DENSITY / EPSILON_0) * PLAYER_CHARGE) / PLAYER_MASS;
                playerVelocity -= (isPositiveLeft ? 1 : -1) * acceleration * ((System.nanoTime() - lastPlayerUpdateTime) / 1_000_000_000.0);
            }
            else
            {
                //Acceleration is 0, No Velocity Changes
            }

            spriteBounds.offset((int) Math.round(playerVelocity * (System.nanoTime() - lastPlayerUpdateTime) / 1_000_000_000.0), 0);
            lastPlayerUpdateTime = System.nanoTime();
            //Log.d(LOG_TAG, "Velocity: " + Double.toString(playerVelocity));
        }
        else
        {
            destroy();
        }
    }

    @Override
    public void paint()
    {
        if(chargeState == CHARGE_STATE.POSITIVE)
        {
            androidGraphics.drawRectObject(spriteBounds, Color.RED);
        }
        else if(chargeState == CHARGE_STATE.NEGATIVE)
        {
            androidGraphics.drawRectObject(spriteBounds, Color.BLUE);
        }
        else
        {
            androidGraphics.drawRectObject(spriteBounds, Color.WHITE);
        }
    }

    @Override
    public boolean isTouching(Sprite other)
    {
        return Rect.intersects(other.spriteBounds, this.spriteBounds);
    }

    @Override
    public void touched(Sprite other)
    {
        if(other instanceof WallSprite)
        {
            if(Math.abs(playerVelocity) < 100)
            {
                if (spriteBounds.centerX() > Configuration.GAME_WIDTH / 2)
                {
                    spriteBounds.offsetTo((Configuration.GAME_WIDTH - (Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2) - spriteBounds.width(), spriteBounds.top);
                }
                else
                {
                    spriteBounds.offsetTo((Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2, spriteBounds.top);
                }
                playerVelocity = 0;
            }
            else
            {
                spriteBounds.offset((int) -Math.ceil(playerVelocity / 20), 0);
                playerVelocity = -(playerVelocity / 5);
            }
        }
        else if(other instanceof ObstacleSprite)
        {
            wasTouched = true;
        }
    }

    @Override
    public void destroy()
    {
        super.destroy();
        androidGame.setScreen(new GameOverScreen(androidGame));
    }
}
