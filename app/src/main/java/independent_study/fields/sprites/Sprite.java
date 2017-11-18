package independent_study.fields.sprites;

import android.graphics.Rect;

import java.util.ArrayList;

import independent_study.fields.framework.AndroidGraphics;

/**
 * Created by Blaine Huey on 11/1/2017.
 */

public abstract class Sprite
{
    protected static ArrayList<Sprite> sprites = new ArrayList<>();

    public static void touchCheckAll()
    {
        for(int i = sprites.size() - 1; i >= 0; i--)
        {
            for(int j = 0; j < i; j++)
            {
                if(sprites.get(i).isTouching(sprites.get(j)))
                {
                    sprites.get(i).touched(sprites.get(j));
                }
            }
        }
    }

    public static void updateAll()
    {
        for(int i = sprites.size() - 1; i >= 0; i--)
        {
            for(int j = 0; j < i; j++)
            {
                if(sprites.get(i).isTouching(sprites.get(j)))
                {
                    sprites.get(i).touched(sprites.get(j));
                }
            }

            sprites.get(i).update();
        }
    }

    public static void paintAll()
    {
        for(int i = sprites.size() - 1; i >= 0; i--)
        {
            sprites.get(i).paint();
        }
    }

    public static void destroyAll()
    {
        for(Sprite tempSprite : sprites)
        {
            tempSprite.destroy();
        }
    }

    protected Rect spriteBounds;
    protected AndroidGraphics androidGraphics;

    protected Sprite(int left, int top, int right, int bottom, AndroidGraphics graphics)
    {
        sprites.add(this);
        spriteBounds = new Rect(left, top, right, bottom);
        spriteBounds.sort();
        androidGraphics = graphics;
    }

    public abstract void update();
    public abstract void paint();
    public abstract boolean isTouching(Sprite other);
    public abstract void touched(Sprite other);
    public void destroy()
    {
        sprites.remove(this);
    }
}
