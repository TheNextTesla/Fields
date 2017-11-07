package independent_study.fields.game;

import android.graphics.Bitmap;

import java.util.List;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidImage;
import independent_study.fields.framework.AndroidInput;
import independent_study.fields.framework.Screen;

/**
 * Created by Blaine Huey on 11/6/2017.
 */

public class TitleScreen extends Screen
{
    private AndroidImage androidImage;
    private AndroidGraphics androidGraphics;

    private int gameWidth;
    private int gameHeight;

    public TitleScreen(AndroidGame game)
    {
        super(game);
        androidImage = new AndroidImage(createTitleBitmap(), AndroidGraphics.ImageFormat.ARGB8888);
        androidGraphics = game.getGraphics();

        gameWidth = androidGraphics.getWidth();
        gameHeight = androidGraphics.getHeight();
    }

    private Bitmap createTitleBitmap()
    {
        int[] imagePixels = new int[Configuration.GAME_HEIGHT * Configuration.GAME_WIDTH];
        for(int i = 0; i < imagePixels.length; i++)
        {
            imagePixels[i] = 0xff00ff;
        }

        return Bitmap.createBitmap(imagePixels, Configuration.GAME_WIDTH, Configuration.GAME_HEIGHT, Bitmap.Config.ARGB_8888);
    }

    public void update(float deltaTime)
    {
        List<AndroidInput.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        if(touchEvents.size() > 0)
        {
            game.setScreen(new GameScreen(game));
        }
    }

    public void paint(float deltaTime)
    {
        androidGraphics.drawImage(androidImage, 0, 0, gameWidth, gameHeight, gameWidth, gameHeight);
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
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
