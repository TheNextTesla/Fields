package independent_study.fields.sprites;

import independent_study.fields.game.Configuration;

/**
 * Created by Blaine Huey on 11/2/2017.
 */

public class ObstacleSprite extends Sprite
{
    public static final int DEFAULT_OBSTACLE_WIDTH = 20;
    public static final int DEFAULT_OBSTACLE_HEIGHT = 50;

    public ObstacleSprite(int left, int top, int right, int bottom)
    {
        super(left, top, right, bottom);
    }

    public ObstacleSprite(int topPixelStart)
    {
        this(((topPixelStart + (Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2) - DEFAULT_OBSTACLE_WIDTH / 2),
                0, ((topPixelStart + (Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2) + DEFAULT_OBSTACLE_WIDTH),
                DEFAULT_OBSTACLE_HEIGHT);
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean isTouching(Sprite other)
    {
        return false;
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
