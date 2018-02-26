package independent_study.fields.sprites;

import android.content.res.Resources;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.Game;

/**
 * Created by Blaine Huey on 11/1/2017.
 */

public abstract class Sprite
{
    //A Common List of Every Sprite
    protected static ArrayList<Sprite> sprites = new ArrayList<>();

    /**
     * Runs Touch Comparisons on All Objects, Calling Its Touched Method
     * WARNING: Having a Sprite Touched Method Directly Change the sprite List
     * WILL LIKELY CAUSE AN ERROR (i.e. Never call a destroy in touched)
     */
    public static void touchCheckAll()
    {
        for(Sprite spriteA : sprites)
        {
            for(Sprite spriteB : sprites)
            {
                if(spriteA == spriteB)
                    continue;

                if(spriteA.isTouching(spriteB))
                {
                    spriteA.touched(spriteB);
                }
            }
        }
    }

    /**
     * Calls the Paint Method of All Sprites
     */
    public static void paintAll()
    {
        for(int i = sprites.size() - 1; i >= 0; i--)
        {
            sprites.get(i).paint();
        }
    }

    /**
     * Calls the Destroy Method of Every Sprite
     * AS THIS IS WRITTEN NOW, THIS IS A TERRIBLE IDEA
     */
    public static void destroyAll()
    {
        for(int i = sprites.size() - 1; i >= 0; i--)
        {
            sprites.get(i).destroy();
        }
    }

    //Instance Variable Required By Sprite
    protected Rect spriteBounds;
    protected AndroidGraphics androidGraphics;
    protected Resources resources;

    /**
     * Constructor for Every Sprite
     * @param left - Left-most coordinate of Sprite
     * @param top - Top-most coordinate of Sprite
     * @param right - Right-most coordinate of Sprite
     * @param bottom - Bottom-most coordinate of Sprite
     * @param game - Android Game to Draw Sprite On
     */
    protected Sprite(int left, int top, int right, int bottom, Game game)
    {
        sprites.add(this);
        spriteBounds = new Rect(left, top, right, bottom);
        spriteBounds.sort();
        androidGraphics = game.getGraphics();
        resources = game.getResources();
    }

    //Abstract Required Methods For Implementation
    public abstract void update(float deltaTime);
    public abstract void paint();
    public abstract boolean isTouching(Sprite other);
    public abstract void touched(Sprite other);

    /**
     * Deletes the Current Sprite's  Entry from the List of All Sprites
     * If there is no other direct reference to it, the garbage collector will eat it up
     */
    public void destroy()
    {
        Log.d("Sprite", "remove? " + sprites.remove(this));
    }

    public int getLocationX()
    {
        return spriteBounds.centerX();
    }

    public void setLocationX(int newX)
    {
        spriteBounds.offsetTo(newX - spriteBounds.width() / 2, spriteBounds.top);
    }

    public void setLocationY(int newY)
    {
        spriteBounds.offsetTo(spriteBounds.left, newY - spriteBounds.height() / 2);
    }

    public int getLocationY()
    {
        return spriteBounds.centerY();
    }


}
