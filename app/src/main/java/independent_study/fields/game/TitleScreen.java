package independent_study.fields.game;

import android.content.Intent;
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
import independent_study.fields.settings.SettingsActivity;

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
    private Paint settingsPaint;
    private Paint titlePaint;

    public TitleScreen(AndroidGame game)
    {
        super(game);
        //androidImage = new AndroidImage(createTitleBitmap(), AndroidGraphics.ImageFormat.RGB565);
        androidGraphics = game.getGraphics();

        gameWidth = androidGraphics.getWidth();
        gameHeight = androidGraphics.getHeight();

        settingsPaint = new Paint();
        settingsPaint.setTextSize(20);
        settingsPaint.setTextAlign(Paint.Align.CENTER);
        settingsPaint.setAntiAlias(true);
        settingsPaint.setColor(Color.WHITE);

        titlePaint = new Paint();
        titlePaint.setTextSize(50);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setAntiAlias(true);
        titlePaint.setColor(Color.WHITE);
    }

    public void update(float deltaTime)
    {
        boolean touchTriggered = false;
        boolean isSettingsSelection = false;
        for(AndroidInput.TouchEvent event : game.getInput().getTouchEvents())
        {
            if(event.type == AndroidInput.TouchEvent.TOUCH_DOWN)
            {
                touchTriggered = true;

                if(event.x > Configuration.GAME_WIDTH - 100 && event.y < Configuration.GAME_HEIGHT / 4)
                {
                    isSettingsSelection = true;
                }
            }
        }

        if(touchTriggered)
        {
            if(!isSettingsSelection)
            {
                game.setScreen(new GameScreen(game));
            }
            else
            {
                Intent intent = new Intent(game, SettingsActivity.class);
                game.startActivity(intent);
            }
        }
        //Log.d(LOG_TAG, "update");
    }

    public void paint(float deltaTime)
    {
        androidGraphics.clearScreen(Color.BLUE);
        androidGraphics.drawRect(Configuration.GAME_WIDTH - 100, Configuration.GAME_HEIGHT / 4, 100, 0, Color.LTGRAY);
        androidGraphics.drawString("Settings",Configuration.GAME_WIDTH - 50, Configuration.GAME_HEIGHT / 8, settingsPaint);
        androidGraphics.drawString("Fields", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 2, titlePaint);
        //Log.d(LOG_TAG, "paint");
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
