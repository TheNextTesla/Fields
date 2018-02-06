package independent_study.fields.ui;

import android.graphics.Rect;

import independent_study.fields.framework.AndroidImage;
import independent_study.fields.framework.Game;

/**
 * Created by Blaine Huey on 2/2/2018.
 */

public class IconButton extends ScreenButton
{
    private AndroidImage buttonImage;

    public IconButton(Rect bounds, AndroidImage image, Game game)
    {
        super(bounds, game);
        buttonImage = image;
    }

    public IconButton(int left, int top, int right, int bottom, AndroidImage image, Game game)
    {
        super(left, top, right, bottom, game);
        buttonImage = image;
    }

    public void display()
    {
        buttonGame.getGraphics().drawScaledImage(buttonImage, buttonBounds.left, buttonBounds.top,
                buttonBounds.width(), buttonBounds.height(), 0, 0, buttonImage.getWidth(), buttonImage.getHeight());
    }
}
