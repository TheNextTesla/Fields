package independent_study.fields.sprites;

import independent_study.fields.game.Configuration;

/**
 * Created by Blaine Huey on 11/2/2017.
 */

public class PlayerSprite extends Sprite
{
    public static final int DEFAULT_PLAYER_WIDTH = 10;
    public static final int DEFAULT_PLAYER_HEIGHT = 10;

    public PlayerSprite(int left, int top, int right, int bottom)
    {
        super(left, top, right, bottom);
    }

    public PlayerSprite()
    {
        this((Configuration.FIELD_WIDTH / 2 - DEFAULT_PLAYER_WIDTH),
                Configuration.GAME_HEIGHT - DEFAULT_PLAYER_HEIGHT,
                (Configuration.FIELD_WIDTH / 2 + DEFAULT_PLAYER_WIDTH),
                Configuration.GAME_HEIGHT);
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean isTouching(Sprite other)
    {
        return other.spriteBounds.contains(this.spriteBounds);
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
