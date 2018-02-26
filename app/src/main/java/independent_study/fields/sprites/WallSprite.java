package independent_study.fields.sprites;

import android.graphics.Color;
import android.graphics.Rect;

import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.Game;
import independent_study.fields.game.Configuration;

/**
 * Created by Blaine Huey on 11/2/2017.
 */

public class WallSprite extends Sprite
{
    //Wall Width Generated from Configuration Class
    private static final int DEFAULT_WALL_WIDTH = (Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2;
    //Enum for the predefined options for the Wall Type
    public enum DEFAULT_WALL_TYPE {LEFT, RIGHT}

    //Is this wall a 'positive' or 'negative' wall
    private boolean positive;

    /**
     * Creates the 'Wall' Sprite Object, Which Represents A Colored Wall On Edges of the Screen
     * @param left - Left-most coordinate of Wall
     * @param top - Top-most coordinate of Wall
     * @param right - Right-most coordinate of Wall
     * @param bottom - Bottom-most coordinate of Wall
     * @param isPositive - Charge of Sign of Wall (Determines Color)
     * @param game - Android Game Object to Draw Upon
     */
    public WallSprite(int left, int top, int right, int bottom, boolean isPositive, Game game)
    {
        //Call to the Sprite Superclass, and sets the boolean isPositive
        super(left, top, right, bottom, game);
        positive = isPositive;
    }

    /**
     * WallSprite creating method for calling the constructor using default parameters (of Size and Location)
     * @param defaultWallType - Enum Representing the 'Type' of Wall, Currently Left and Right
     * @param positive - Charge of Sign of Wall
     * @param game - Android Graphics to Draw Upon
     * @return A New WallSprite in Default Location
     */
    public static WallSprite generateDefault(DEFAULT_WALL_TYPE defaultWallType, boolean positive, Game game)
    {
        return defaultWallType == DEFAULT_WALL_TYPE.LEFT ?
            new WallSprite(0, 0, DEFAULT_WALL_WIDTH, Configuration.GAME_HEIGHT, positive, game) :
            new WallSprite(Configuration.GAME_WIDTH - DEFAULT_WALL_WIDTH, 0, Configuration.GAME_WIDTH, Configuration.GAME_HEIGHT, positive, game);
    }

    /**
     * Sprite Update Method
     * Walls Don't Move...
     */
    @Override
    public void update(float deltaTime)
    {

    }

    /**
     * Sprite Paint Method
     * Colors Based off of Sign, Paints to the Android Graphics
     */
    @Override
    public void paint()
    {
        if(!positive)
        {
            androidGraphics.drawRectObject(spriteBounds, Color.BLUE);
        }
        else
        {
            androidGraphics.drawRectObject(spriteBounds, Color.DKGRAY);
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
        //This Uses the Sprite Classes Requirement for a 'Rect' of its Collision Box (spriteBounds)
        return Rect.intersects(spriteBounds, other.spriteBounds) || spriteBounds.contains(other.spriteBounds);
    }

    /**
     * How To React When Touched
     * Walls Don't Die
     * @param other - Other Sprite in Contact
     */
    @Override
    public void touched(Sprite other)
    {

    }

    /**
     * Sprite Destroy Method
     * Walls Die Just Like Anything Else, When the Game Ends
     */
    @Override
    public void destroy()
    {
        super.destroy();
    }

    public void swapCharge()
    {
        positive = !positive;
    }

    public boolean getCharge()
    {
        return positive;
    }

}
