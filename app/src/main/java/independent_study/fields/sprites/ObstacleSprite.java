package independent_study.fields.sprites;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.Log;

import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidImage;
import independent_study.fields.game.Configuration;

/**
 * Created by Blaine Huey on 11/2/2017.
 */

public class ObstacleSprite extends Sprite
{
    public static final int DEFAULT_OBSTACLE_WIDTH = 20;
    public static final int DEFAULT_OBSTACLE_HEIGHT = 50;
    public static final int DEFAULT_OBSTACLE_SPEED = 1;
    public static final Rect gameRegion = new Rect((Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2, 0,
        (Configuration.FIELD_WIDTH + (Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2), Configuration.GAME_HEIGHT);

    private int obstacleWidth;
    private int obstacleHeight;

    public ObstacleSprite(int left, int top, int right, int bottom, AndroidGraphics graphics)
    {
        super(left, top, right, bottom, graphics);
        obstacleWidth = Math.abs(left - right);
        obstacleHeight = Math.abs(top - bottom);
    }

    public ObstacleSprite(int topPixelStart, AndroidGraphics graphics)
    {
        this(((topPixelStart + (Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2) - DEFAULT_OBSTACLE_WIDTH / 2),
                0, ((topPixelStart + (Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2) + DEFAULT_OBSTACLE_WIDTH),
                DEFAULT_OBSTACLE_HEIGHT, graphics);
    }

    @Override
    public void update()
    {
        if(!spriteBounds.intersect(gameRegion))
        {
            destroy();
        }
        else
        {
            spriteBounds.offset(0, DEFAULT_OBSTACLE_SPEED);
        }
    }

    @Override
    public void paint()
    {
        androidGraphics.drawRectObject(spriteBounds, Color.BLACK);
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
            destroy();
        }
        else if(other instanceof ObstacleSprite)
        {
            destroy();
        }
    }

    @Override
    public void destroy()
    {

    }
}
