package independent_study.fields.game;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

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
    private static final String LOG_TAG = "TitleScreen";

    private AndroidImage androidImage;
    private AndroidGraphics androidGraphics;

    private int gameWidth;
    private int gameHeight;

    public TitleScreen(AndroidGame game)
    {
        super(game);
        //androidImage = new AndroidImage(createTitleBitmap(), AndroidGraphics.ImageFormat.RGB565);
        androidGraphics = game.getGraphics();

        gameWidth = androidGraphics.getWidth();
        gameHeight = androidGraphics.getHeight();
        Log.d(LOG_TAG, "Constructed");
    }

    public void update(float deltaTime)
    {
        List<AndroidInput.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        if(touchEvents.size() > 0)
        {
            game.setScreen(new GameScreen(game));
        }
        Log.d(LOG_TAG, "update");
    }

    public void paint(float deltaTime)
    {
        androidGraphics.clearScreen(Color.BLUE);
        Log.d(LOG_TAG, "paint");
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
