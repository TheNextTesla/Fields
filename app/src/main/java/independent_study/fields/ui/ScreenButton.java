package independent_study.fields.ui;

import android.graphics.Rect;

import independent_study.fields.framework.AndroidInput;
import independent_study.fields.framework.Game;

/**
 * Created by Blaine Huey on 2/2/2018.
 */

public abstract class ScreenButton
{
    protected Game buttonGame;
    protected Rect buttonBounds;

    public ScreenButton(Rect bounds, Game game)
    {
        buttonBounds = bounds;
        bounds.sort();
        buttonGame = game;
    }

    public ScreenButton(int left, int top, int right, int bottom, Game game)
    {
        this(new Rect(left, top, right, bottom), game);
    }

    public boolean isPressed(Rect point)
    {
        return Rect.intersects(buttonBounds, point);
    }

    public boolean isPressed(int x, int y)
    {
        return isPressed(new Rect(x, y, x, y));
    }

    public abstract void display();
}
