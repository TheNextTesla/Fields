package independent_study.fields.sprites;

import android.graphics.Color;
import android.graphics.Rect;

import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.game.Configuration;

/**
 * Created by Blaine Huey on 12/5/2017.
 */

public class ObjectiveSprite extends ObstacleSprite
{
    private static final int DEFAULT_POINTS = 10;

    private int points;
    private boolean wasPlayerTouched;
    private boolean didPlayerTouchedAnimation;

    /**
     * Main Constructor for an ObjectiveSprite
     * @param left - Left-most coordinate of Sprite
     * @param top - Top-most coordinate of Sprite
     * @param right - Right-most coordinate of Sprite
     * @param bottom - Bottom-most coordinate of Sprite
     * @param pointValue - Value Given to the Player for Hitting It
     * @param obstacleSpeed - Speed Multiplier for the Obstacle
     * @param graphics - Android Graphics to Draw Sprite On
     */
    public ObjectiveSprite(int left, int top, int right, int bottom, int pointValue, OBSTACLE_SPEED obstacleSpeed, AndroidGraphics graphics)
    {
        super(left, top, right, bottom, obstacleSpeed, graphics);
        points = pointValue;
        wasPlayerTouched = false;
        didPlayerTouchedAnimation = false;
    }

    /**
     * Alternative (1) Constructor For The ObjectiveSprite
     * @param topPixelStart - The Starting Pixel X At the Top of the Screen
     * @param pointValue - Value Given to the Player for Hitting It
     * @param obstacleSpeed - Speed Multiplier for the Obstacle
     * @param graphics - Android Graphics to Draw Sprite On
     */
    public ObjectiveSprite(int topPixelStart, int pointValue, ObstacleSprite.OBSTACLE_SPEED obstacleSpeed, AndroidGraphics graphics)
    {
        super(topPixelStart, obstacleSpeed, graphics);
        points = pointValue;
    }

    /**
     * Alternative (2) Constructor For the ObjectiveSprite
     * @param topPixelStart - The Starting Pixel X At the Top of the Screen
     * @param obstacleSpeed - Speed Multiplier for the Obstacle
     * @param graphics - Android Graphics to Draw Sprite On
     */
    public ObjectiveSprite(int topPixelStart, ObstacleSprite.OBSTACLE_SPEED obstacleSpeed, AndroidGraphics graphics)
    {
        this(topPixelStart, DEFAULT_POINTS, obstacleSpeed, graphics);
    }

    /**
     * Sprite Update Method
     * Updates Unless It Gave a Death Animate
     */
    @Override
    public void update()
    {
        if(!didPlayerTouchedAnimation)
        {
            super.update();
        }
        else
        {
            destroy();
        }
    }

    /**
     * Sprite Paint Method
     * Flashes On the Screen When the
     */
    @Override
    public void paint()
    {
        if(!wasPlayerTouched)
        {
            androidGraphics.drawRectObject(spriteBounds, Color.GREEN);
        }
        else
        {
            androidGraphics.drawRect(0, 0, Configuration.GAME_WIDTH, Configuration.GAME_HEIGHT, Color.GREEN);
            didPlayerTouchedAnimation = true;
        }
    }

    /**
     * How To React When Touched
     * @param other - Other Sprite in Contact
     */
    @Override
    public void touched(Sprite other)
    {
        super.touched(other);

        if(other instanceof PlayerSprite)
        {
            wasPlayerTouched = true;
        }
    }

    /**
     * Return Whether of Not an Unspecified Object is Currently Touching This
     * @param other - Other Sprite in Contact
     * @return Whether of Not an Unspecified Object is Currently Touching This
     */
    @Override
    public boolean isTouching(Sprite other)
    {
        return super.isTouching(other);
    }

    /**
     * Destroys the Sprite
     */
    @Override
    public void destroy()
    {
        super.destroy();
    }

    /**
     * Gets the Point Value Assigned for Its Death
     * @return - The Point Value Assigned for Its Death
     */
    public int getPoints()
    {
        return points;
    }
}
