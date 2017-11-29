package independent_study.fields.sprites;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.game.Configuration;

/**
 * Created by Blaine Huey on 11/2/2017.
 */

public class WallSprite extends Sprite
{
    private static final int DEFAULT_WALL_WIDTH = (Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2;
    public enum DEFAULT_WALL_TYPE {LEFT, RIGHT}

    private int wallWidth;
    private int wallHeight;
    private boolean positive;

    public WallSprite(int left, int top, int right, int bottom, boolean isPositive, AndroidGraphics graphics)
    {
        super(left, top, right, bottom, graphics);

        wallWidth = Math.abs(left - right);
        wallHeight = Math.abs(top - bottom);
        positive = isPositive;
    }

    public static WallSprite generateDefault(DEFAULT_WALL_TYPE defaultWallType, AndroidGraphics graphics)
    {
        return defaultWallType == DEFAULT_WALL_TYPE.LEFT ?
            new WallSprite(0, 0, DEFAULT_WALL_WIDTH, Configuration.GAME_HEIGHT, true, graphics) :
            new WallSprite(Configuration.GAME_WIDTH - DEFAULT_WALL_WIDTH, 0, Configuration.GAME_WIDTH, Configuration.GAME_HEIGHT, false, graphics);
    }

    @Override
    public void update()
    {

    }

    @Override
    public void paint()
    {
        if(positive)
        {
            androidGraphics.drawRectObject(spriteBounds, Color.RED);
        }
        else
        {
            androidGraphics.drawRectObject(spriteBounds, Color.BLUE);
        }
    }

    @Override
    public boolean isTouching(Sprite other)
    {
        return Rect.intersects(spriteBounds, other.spriteBounds) || spriteBounds.contains(other.spriteBounds);
    }

    @Override
    public void touched(Sprite other)
    {
        //Other Sprites Run the Check Operations When They Touch This
        //if(other instanceof PlayerSprite)
        //{
            //other.touched(this);
        //}
    }

    @Override
    public void destroy()
    {
        super.destroy();
    }
}
