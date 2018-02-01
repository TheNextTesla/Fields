package independent_study.fields.sprites;

import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.Game;
import independent_study.fields.game.Configuration;

/**
 * Created by Blaine Huey on 11/2/2017.
 */

public class ObstacleSprite extends Sprite
{
    //Constants of the Size and Game's Region
    public static final int DEFAULT_OBSTACLE_WIDTH = 20;
    public static final int DEFAULT_OBSTACLE_HEIGHT = 50;
    public static final int DEFAULT_OBSTACLE_SPEED = 2;
    public static final Rect gameRegion = new Rect((Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2, 0,
        (Configuration.FIELD_WIDTH + (Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2), Configuration.GAME_HEIGHT);

    /**
     * An Enum For The Different Obstacle Speed Settings Options
     */
    public enum OBSTACLE_SPEED
    {
        SLOW(1), NORMAL(2), FAST(3);

        int value;
        OBSTACLE_SPEED(int speedConstant)
        {
            value = speedConstant;
        }
    }

    //Instance Variables
    private int speed;
    private boolean wasTouched;

    /**
     * Main Constructor for ObstacleSprite
     * @param left - Left-most coordinate of Sprite
     * @param top - Top-most coordinate of Sprite
     * @param right - Right-most coordinate of Sprite
     * @param bottom - Bottom-most coordinate of Sprite
     * @param obstacleSpeed - Speed Multiplier for the Obstacle
     * @param game - Android Graphics to Draw Sprite On
     */
    public ObstacleSprite(int left, int top, int right, int bottom, OBSTACLE_SPEED obstacleSpeed, Game game)
    {
        super(left, top, right, bottom, game);
        speed = (int) Math.round(DEFAULT_OBSTACLE_SPEED * (obstacleSpeed.value / 2.0));
        wasTouched = false;
    }

    public ObstacleSprite(int centerX, int centerY, int obstacleSpeed, Game game)
    {
        super(centerX - DEFAULT_OBSTACLE_WIDTH / 2, centerY + DEFAULT_OBSTACLE_HEIGHT / 2, centerX + DEFAULT_OBSTACLE_WIDTH / 2, centerY - DEFAULT_OBSTACLE_HEIGHT / 2, game);
        speed = obstacleSpeed;
        wasTouched = false;
    }

    /**
     * Alternative Constructor for ObstacleSprite
     * @param topPixelStart - The Starting Pixel X At the Top of the Screen
     * @param obstacleSpeed - Speed Multiplier for the Obstacle
     * @param game - Android Graphics to Draw Sprite On
     */
    public ObstacleSprite(int topPixelStart, OBSTACLE_SPEED obstacleSpeed, Game game)
    {
        this(((topPixelStart + (Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2) - DEFAULT_OBSTACLE_WIDTH / 2),
                -DEFAULT_OBSTACLE_HEIGHT + 1, ((topPixelStart + (Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2) + DEFAULT_OBSTACLE_WIDTH),
                1, obstacleSpeed, game);
    }

    /**
     * Sprite Update Method
     * Moves the Object Until It Leaves the Game Area
     */
    @Override
    public void update()
    {
        if(!Rect.intersects(spriteBounds, gameRegion) || wasTouched)
        {
            destroy();
        }
        else
        {
            spriteBounds.offset(0, speed);
        }
    }

    /**
     * Sprite Paint Method
     */
    @Override
    public void paint()
    {
        androidGraphics.drawRectObject(spriteBounds, Color.BLACK);
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
     *
     * @param other - Other Sprite in Contact
     */
    @Override
    public void touched(Sprite other)
    {
        if(other instanceof WallSprite)
        {
            wasTouched = true;
        }
        else if(other instanceof ObstacleSprite)
        {
            wasTouched = true;
        }
    }

    /**
     * Changes the Speed Of the Obstacle
     * @param newSpeed - The New Speed of the Obstacle
     */
    public void setSpeed(int newSpeed)
    {
        speed = newSpeed;
    }

    public int getSpeed()
    {
        return speed;
    }
}
