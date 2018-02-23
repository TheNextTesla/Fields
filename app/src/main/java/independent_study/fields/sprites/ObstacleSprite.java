package independent_study.fields.sprites;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import independent_study.fields.R;
import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidImage;
import independent_study.fields.framework.Game;
import independent_study.fields.game.Configuration;

/**
 * Created by Blaine Huey on 11/2/2017.
 */

public class ObstacleSprite extends Sprite
{
    public static final String LOG_TAG = "ObstacleSprite";

    //Constants of the Size and Game's Region
    public static final int DEFAULT_OBSTACLE_WIDTH = 10;
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

    protected static AndroidImage obstacleSpriteImage;
    protected static AndroidImage obstacleExplosionImage;

    //Instance Variables
    private int speed;
    protected boolean wasTouched;

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

        if(obstacleSpriteImage == null)
        {
            Bitmap settingsBitmap = BitmapFactory.decodeResource(resources, R.drawable.missile);
            obstacleSpriteImage = new AndroidImage(settingsBitmap, AndroidGraphics.ImageFormat.ARGB4444);
        }

        if(obstacleExplosionImage == null)
        {
            Bitmap settingsBitmap = BitmapFactory.decodeResource(resources, R.drawable.explosion);
            obstacleExplosionImage = new AndroidImage(settingsBitmap, AndroidGraphics.ImageFormat.ARGB4444);
        }
    }

    public ObstacleSprite(int centerX, int centerY, int obstacleSpeed, Game game)
    {
        super(centerX - DEFAULT_OBSTACLE_WIDTH / 2, centerY + DEFAULT_OBSTACLE_HEIGHT / 2, centerX + DEFAULT_OBSTACLE_WIDTH / 2, centerY - DEFAULT_OBSTACLE_HEIGHT / 2, game);
        speed = obstacleSpeed;
        wasTouched = false;

        if(obstacleSpriteImage == null)
        {
            Bitmap settingsBitmap = BitmapFactory.decodeResource(resources, R.drawable.missile);
            obstacleSpriteImage = new AndroidImage(settingsBitmap, AndroidGraphics.ImageFormat.ARGB4444);
        }

        if(obstacleExplosionImage == null)
        {
            Bitmap settingsBitmap = BitmapFactory.decodeResource(resources, R.drawable.explosion);
            obstacleExplosionImage = new AndroidImage(settingsBitmap, AndroidGraphics.ImageFormat.ARGB4444);
        }
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
            if(!Rect.intersects(spriteBounds, gameRegion))
            {
                Log.d(LOG_TAG, "Is Outside of Game Region");
            }
            else
            {
                Log.d(LOG_TAG, "Was Touched By Other Sprite");
            }

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
        if(!wasTouched)
        {
            androidGraphics.drawScaledImage(obstacleSpriteImage, spriteBounds);
        }
        else
        {
            Rect largeRect = new Rect(spriteBounds.left - spriteBounds.width(),
                    spriteBounds.top - spriteBounds.height(),
                    spriteBounds.right + spriteBounds.width(),
                    spriteBounds.bottom + spriteBounds.height());
            androidGraphics.drawScaledImage(obstacleExplosionImage, largeRect);
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
        else if(other instanceof PlayerSprite)
        {
            Rect largeRect = new Rect(spriteBounds.centerX() - spriteBounds.height() * 3,
                    spriteBounds.centerY() - spriteBounds.height() * 3,
                    spriteBounds.centerX() + spriteBounds.height() * 3,
                    spriteBounds.centerY() + spriteBounds.height() * 3);
            androidGraphics.drawScaledImage(obstacleExplosionImage, largeRect);
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
