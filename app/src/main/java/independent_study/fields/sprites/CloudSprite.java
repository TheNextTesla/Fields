package independent_study.fields.sprites;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;

import independent_study.fields.R;
import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidImage;
import independent_study.fields.framework.Game;
import independent_study.fields.game.Configuration;

/**
 * Created by Blaine Huey on 2/9/2018.
 */

public class CloudSprite extends Sprite
{
    public static final int CLOUD_SPEED = 3;

    private static AndroidImage cloudImage;

    private double movement;

    public CloudSprite(int left, int top, int right, int bottom, Game game)
    {
        super(left, top, right, bottom, game);

        if(cloudImage == null)
        {
            Bitmap settingsBitmap = BitmapFactory.decodeResource(game.getResources(), R.drawable.cloud);
            cloudImage = new AndroidImage(settingsBitmap, AndroidGraphics.ImageFormat.ARGB4444);
        }

        movement = 0;

        Log.d("Cloud", "Created");
    }

    public CloudSprite(Point center, int radius, Game game)
    {
        this(center.x - radius, center.y - radius, center.x + radius, center.y + radius, game);
    }

    /**
     * Sprite Update Method
     */
    @Override
    public void update(float deltaTime)
    {
        Log.d("CloudSprite", " " + deltaTime);
        movement += CLOUD_SPEED * deltaTime / 100 * Configuration.TICKS_PER_SECOND;
        if(movement > 1)
        {
            spriteBounds.offset(0, (int) movement);
            movement = movement % 1;
        }
        //spriteBounds.offset(0, CLOUD_SPEED * (int) Math.round(deltaTime / 100 * Configuration.TICKS_PER_SECOND));
    }

    /**
     * Sprite Paint Method
     * Colors Based off of Sign, Paints to the Android Graphics
     */
    @Override
    public void paint()
    {
        androidGraphics.drawScaledImage(cloudImage, spriteBounds);
    }

    /**
     * Return Whether of Not an Unspecified Object is Currently Touching This
     * @param other - Other Sprite in Contact
     * @return Whether of Not an Unspecified Object is Currently Touching This
     */
    @Override
    public boolean isTouching(Sprite other)
    {
        //This Uses the Sprite Classes Requirement for a 'Rect' of its Collision Box (spriteBounds)
        return Rect.intersects(spriteBounds, other.spriteBounds) || spriteBounds.contains(other.spriteBounds);
    }

    /**
     * How To React When Touched
     * Clouds Don't Die
     * @param other - Other Sprite in Contact
     */
    @Override
    public void touched(Sprite other)
    {

    }

    /**
     * Sprite Destroy Method
     * Clouds Die Just Like Anything Else, When the Game Ends
     */
    @Override
    public void destroy()
    {
        super.destroy();
    }
}
