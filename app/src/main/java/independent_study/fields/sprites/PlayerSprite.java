package independent_study.fields.sprites;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import independent_study.fields.R;
import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidImage;
import independent_study.fields.framework.Game;
import independent_study.fields.framework.Screen;
import independent_study.fields.game.Configuration;
import independent_study.fields.game.GameOverScreen;
import independent_study.fields.game.GameScreen;

/**
 * Created by Blaine Huey on 11/2/2017.
 */

public class PlayerSprite extends Sprite
{
    //Constants of The PlayerSprite Class Itself
    private static final String LOG_TAG = "PlayerSprite";
    public enum CHARGE_STATE {POSITIVE, NEUTRAL, NEGATIVE}

    //Constants of the PlayerSprite's Size
    public static final int DEFAULT_PLAYER_WIDTH = 50;
    public static final int DEFAULT_PLAYER_HEIGHT = 50;

    //Constants of the PlayerSprite's Motion
    public static final double EPSILON_0 = 8.854187e-12;
    public static final double PLAYER_CHARGE = 1.1e-1;
    public static final double PLAYER_MASS = 1.0; //Old 7.8
    public static final double PLATE_CHARGE_DENSITY = 5.8e-8;

    //Instance Variable of Motion and State
    private boolean wasTouched;
    private boolean isPositiveLeft;
    private double playerVelocity;
    private long lastPlayerUpdateTime;
    private CHARGE_STATE chargeState;
    private AndroidImage spriteImage;
    private Game androidGame;

    /**
     * Main Constructor for the PlayerSprite (Should Only Be Called Once Per Game)
     * @param left - Left-most coordinate of a Player
     * @param top - Top-most coordinate of a Player
     * @param right - Right-most coordinate of a Player
     * @param bottom - Bottom-most coordinate of a Player
     * @param direction - Direction of the Electric Field (Matches Wall Charge Sign Changes)
     * @param game - Android Game Object
     */
    public PlayerSprite(int left, int top, int right, int bottom, boolean direction, Game game)
    {
        super(left, top, right, bottom, game);

        wasTouched = false;
        isPositiveLeft = direction;
        playerVelocity = 0;
        lastPlayerUpdateTime = System.nanoTime();
        chargeState = CHARGE_STATE.POSITIVE;
        androidGame = game;

        Bitmap settingsBitmap = BitmapFactory.decodeResource(resources, R.drawable.fighter);
        spriteImage = new AndroidImage(settingsBitmap, AndroidGraphics.ImageFormat.ARGB4444);
    }

    /**
     * Alternative Constructor Assuming Defaults from the Configuration Class
     * @param game - Android Game Object
     * @param direction - Direction of the Electric Field (Matches Wall Charge Sign Changes)
     */
    public PlayerSprite(Game game, boolean direction)
    {
        this((Configuration.GAME_WIDTH / 2) + DEFAULT_PLAYER_WIDTH / 2,
                (Configuration.GAME_HEIGHT) - (2 * DEFAULT_PLAYER_HEIGHT),
                (Configuration.GAME_WIDTH / 2) - DEFAULT_PLAYER_WIDTH / 2,
                (Configuration.GAME_HEIGHT - DEFAULT_PLAYER_HEIGHT), direction, game);
    }

    public PlayerSprite(Game game, int x, boolean direction)
    {
        this(x - DEFAULT_PLAYER_WIDTH / 2,
                (Configuration.GAME_HEIGHT) - (2 * DEFAULT_PLAYER_HEIGHT),
                x + DEFAULT_PLAYER_WIDTH / 2,
                Configuration.GAME_HEIGHT - DEFAULT_PLAYER_HEIGHT, direction, game);
    }

    /**
     * Changes the Charge State to a New Charge State
     * @param newChargeState - New Charge State Enum
     */
    public void setChargeState(CHARGE_STATE newChargeState)
    {
        chargeState = newChargeState;
    }

    /**
     * Returns the Current Charge State
     * @return chargeState
     */
    public CHARGE_STATE getChargeState()
    {
        return chargeState;
    }

    public void setSpeed(double newSpeed)
    {
        playerVelocity = newSpeed;
    }

    public double getSpeed()
    {
        return playerVelocity;
    }

    /**
     * Updates the Positioning of the Player
     */
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
            //If the Object Was Previously Touched, Self-Destruct
            destroy();
        }
    }

    /**
     * Paints the Player Sprite at its new Color and Position
     */
    @Override
    public void paint()
    {
        androidGraphics.drawScaledImage(spriteImage, spriteBounds);
        /*
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
        */
    }

    /**
     * Return Whether of Not an Unspecified Object is Currently Touching This
     * @param other - Other Sprite in Contact
     * @return Whether of Not an Unspecified Object is Currently Touching This
     */
    @Override
    public boolean isTouching(Sprite other)
    {
        return Rect.intersects(other.spriteBounds, this.spriteBounds);
    }

    /**
     * How to React to Touch Operation
     * @param other - Other Sprite in Contact
     */
    @Override
    public void touched(Sprite other)
    {
        //If it is a Wall, Do Die
        if(other instanceof WallSprite)
        {
            Log.d(LOG_TAG, "Player Touched!");
            wasTouched = true;
        }
        else if(other instanceof ObstacleSprite)
        {
            if(other instanceof ObjectiveSprite)
            {
                    androidGame.incrementObjectiveScore(((ObjectiveSprite) other).getPoints());
                    other.touched(this);
                    Log.d(LOG_TAG, "Points Gained From Objective - Current Score : " + androidGame.getGameScore());
            }
            else
            {
                wasTouched = true;
            }
            //If it is an Obstacle, Do Die
        }
    }

    /**
     * When the Player Dies, the Game Ends
     * Takes from the Sprite Destroy Method
     */
    @Override
    public void destroy()
    {
        super.destroy();
        if(wasTouched)
        {
            Screen screen = androidGame.getCurrentScreen();
            if(screen instanceof GameScreen)
            {
                Log.d(LOG_TAG, "Player Game Over");
                ((GameScreen) screen).gameOver();
            }

            androidGame.setScreen(new GameOverScreen(androidGame));
        }
    }
}
