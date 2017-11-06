package independent_study.fields.game;

import android.graphics.Bitmap;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidImage;
import independent_study.fields.framework.Screen;

/**
 * Created by Blaine Huey on 11/6/2017.
 */

public class TitleScreen extends Screen
{
    AndroidImage androidImage;
    AndroidGraphics androidGraphics;

    public TitleScreen(AndroidGame game)
    {
        super(game);
        androidImage = new AndroidImage(createTitleBitmap(), AndroidGraphics.ImageFormat.ARGB8888);
        androidGraphics = game.getGraphics();

        int gameWidth = androidGraphics.getWidth();
        int gameHeight = androidGraphics.getHeight();

        androidGraphics.drawImage(androidImage, 0, 0, gameWidth, gameHeight, gameWidth, gameHeight);
    }

    private Bitmap createTitleBitmap()
    {

    }

    public void update(float deltaTime)
    {

    }

    public void paint(float deltaTime)
    {

    }

    public void pause()
    {

    }

    public void resume()
    {

    }

    public void dispose()
    {

    }

    public void backButton()
    {

    }
}
