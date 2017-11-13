package independent_study.fields.sprites;

import android.graphics.Bitmap;
import android.graphics.Color;

import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidImage;
import independent_study.fields.game.Configuration;

/**
 * Created by Blaine Huey on 11/2/2017.
 */

public class PlayerSprite extends Sprite
{
    public static final int DEFAULT_PLAYER_WIDTH = 30;
    public static final int DEFAULT_PLAYER_HEIGHT = 30;

    private int playerWidth;
    private int playerHeight;

    public PlayerSprite(int left, int top, int right, int bottom, AndroidGraphics graphics)
    {
        super(left, top, right, bottom, graphics);

        playerWidth = Math.abs(left - right);
        playerHeight = Math.abs(top - bottom);
    }

    public PlayerSprite(AndroidGraphics graphics)
    {
        this((Configuration.GAME_WIDTH / 2) + DEFAULT_PLAYER_WIDTH / 2,
                (Configuration.GAME_HEIGHT) - (2 * DEFAULT_PLAYER_HEIGHT),
                (Configuration.GAME_WIDTH / 2) - DEFAULT_PLAYER_WIDTH / 2,
                (Configuration.GAME_HEIGHT - DEFAULT_PLAYER_HEIGHT), graphics);

        /*
        this((Configuration.GAME_HEIGHT) - (2 * DEFAULT_PLAYER_HEIGHT),
                (Configuration.GAME_WIDTH / 2) + DEFAULT_PLAYER_WIDTH / 2,
                (Configuration.GAME_HEIGHT - DEFAULT_PLAYER_HEIGHT),
                (Configuration.GAME_WIDTH / 2) - DEFAULT_PLAYER_WIDTH / 2 , graphics);
                */
        /*
        this((Configuration.GAME_WIDTH) - (2 * DEFAULT_PLAYER_WIDTH),
                (Configuration.GAME_HEIGHT / 2) + DEFAULT_PLAYER_HEIGHT / 2,
                (Configuration.GAME_WIDTH - DEFAULT_PLAYER_WIDTH),
                (Configuration.GAME_HEIGHT / 2) - DEFAULT_PLAYER_HEIGHT / 2 , graphics);
                */

        //this(0, 0, 20, 20, graphics);
        /*
        this((Configuration.FIELD_WIDTH / 2 - DEFAULT_PLAYER_WIDTH),
                Configuration.GAME_HEIGHT - DEFAULT_PLAYER_HEIGHT,
                (Configuration.FIELD_WIDTH / 2 + DEFAULT_PLAYER_WIDTH),
                Configuration.GAME_HEIGHT, graphics);
                */
    }

    @Override
    public void update()
    {

    }

    @Override
    public void paint()
    {
        androidGraphics.drawRectObject(spriteBounds, Color.WHITE);
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
