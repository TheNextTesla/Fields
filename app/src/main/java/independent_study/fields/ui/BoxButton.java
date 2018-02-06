package independent_study.fields.ui;

import android.graphics.Rect;

import independent_study.fields.framework.Game;

/**
 * Created by Blaine Huey on 2/2/2018.
 */

public class BoxButton extends ScreenButton
{
    protected int buttonBackgroundColor;

    public BoxButton(Rect bounds, int backgroundColor, Game game)
    {
        super(bounds, game);
        buttonBackgroundColor = backgroundColor;
    }

    public BoxButton(int left, int top, int right, int bottom, int backgroundColor, Game game)
    {
        super(left, top, right, bottom, game);
        buttonBackgroundColor = backgroundColor;
    }

    public void display()
    {
        buttonGame.getGraphics().drawRectObject(buttonBounds, buttonBackgroundColor);
    }
}
