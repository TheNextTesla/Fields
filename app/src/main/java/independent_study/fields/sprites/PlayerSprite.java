package independent_study.fields.sprites;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidImage;
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
    public static final double PLAYER_MASS = 7.8;
    public static final double PLATE_CHARGE_DENSITY = 5.8e-8;

    private int playerWidth;
    private int playerHeight;
    private double playerVelocity;
    private long lastPlayerUpdateTime;
    private CHARGE_STATE chargeState;
    private AndroidGame androidGame;

    public PlayerSprite(int left, int top, int right, int bottom, AndroidGame game)
    {
        super(left, top, right, bottom, game.getGraphics());

        playerWidth = Math.abs(left - right);
        playerHeight = Math.abs(top - bottom);
        playerVelocity = 0;
        lastPlayerUpdateTime = System.nanoTime();
        chargeState = CHARGE_STATE.POSITIVE;
        androidGame = game;
    }

    public PlayerSprite(AndroidGame game)
    {
        this((Configuration.GAME_WIDTH / 2) + DEFAULT_PLAYER_WIDTH / 2,
                (Configuration.GAME_HEIGHT) - (2 * DEFAULT_PLAYER_HEIGHT),
                (Configuration.GAME_WIDTH / 2) - DEFAULT_PLAYER_WIDTH / 2,
                (Configuration.GAME_HEIGHT - DEFAULT_PLAYER_HEIGHT), game);
    }

    public void setChargeState(CHARGE_STATE newChargeState)
    {
        chargeState = newChargeState;
    }

    @Override
    public void update()
    {
        if(System.nanoTime() - lastPlayerUpdateTime > 1_000_000_000L)
        {
            lastPlayerUpdateTime = System.nanoTime();
        }

        if(chargeState == CHARGE_STATE.POSITIVE)
        {
            double acceleration = ((PLATE_CHARGE_DENSITY / EPSILON_0) * PLAYER_CHARGE) / PLAYER_MASS;
            playerVelocity += acceleration * (System.nanoTime() - lastPlayerUpdateTime) / 1_000_000_000.0;
            //Log.d(LOG_TAG, "Acceleration: " + Double.toString(acceleration));
        }
        else if(chargeState == CHARGE_STATE.NEGATIVE)
        {
            double acceleration = ((PLATE_CHARGE_DENSITY / EPSILON_0) * PLAYER_CHARGE) / PLAYER_MASS;
            playerVelocity -= acceleration * ((System.nanoTime() - lastPlayerUpdateTime) / 1_000_000_000.0);
            //Log.d(LOG_TAG, "Acceleration: " + Double.toString(acceleration));
        }
        else
        {
            //Acceleration is 0, No Velocity Changes
        }

        spriteBounds.offset((int) Math.round(playerVelocity * (System.nanoTime() - lastPlayerUpdateTime) / 1_000_000_000.0), 0);
        lastPlayerUpdateTime = System.nanoTime();
        //Log.d(LOG_TAG, "Velocity: " + Double.toString(playerVelocity));
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
        /*
        //http://www.geeksforgeeks.org/find-two-rectangles-overlap/
        Point otherPointLT = new Point(other.spriteBounds.left, other.spriteBounds.top);
        Point otherPointRB = new Point(other.spriteBounds.right, other.spriteBounds.bottom);
        Point thisPointLT  = new Point(this.spriteBounds.left, this.spriteBounds.top);
        Point thisPointRB = new Point(this.spriteBounds.right, this.spriteBounds.bottom);

        if (otherPointLT.x >= thisPointRB.x || thisPointLT.x >= otherPointRB.x)
        {
            Log.d(LOG_TAG, "Point Not Touching X");
            return false;
        }

        if (otherPointLT.y < thisPointRB.y || thisPointLT.y < otherPointRB.y)
        {
            Log.d(LOG_TAG, "Point Not Touching Y :" + (otherPointLT.y < thisPointRB.y) + " :" + (thisPointLT.y < otherPointRB.y));
            return false;
        }
        */
        //return Rect.intersects(other.spriteBounds, this.spriteBounds) || this.spriteBounds.contains(other.spriteBounds);
        return Rect.intersects(other.spriteBounds, this.spriteBounds);
    }

    @Override
    public void touched(Sprite other)
    {
        Log.d(LOG_TAG, "Player Touched");
        if(other instanceof WallSprite)
        {
            //TODO: Revise Bounce Algorithm
            spriteBounds.offset((int) -Math.ceil(Math.signum(playerVelocity) * 5), 0);
            playerVelocity = -(playerVelocity / 5);
        }
        else if(other instanceof ObstacleSprite)
        {
            destroy();
        }
    }

    @Override
    public void destroy()
    {
        androidGame.setScreen(new GameOverScreen(androidGame));
    }
}
