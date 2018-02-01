package independent_study.fields.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidImage;
import independent_study.fields.framework.AndroidInput;
import independent_study.fields.framework.Game;
import independent_study.fields.framework.Screen;
import independent_study.fields.network.FieldGameMultiplayer;
import independent_study.fields.settings.SettingsActivity;

/**
 * Created by Blaine Huey on 11/6/2017.
 */

public class TitleScreen extends Screen
{
    private static final String LOG_TAG = "TitleScreen";

    private AndroidImage androidImage;
    private AndroidGraphics androidGraphics;

    private Rect singlePlayerButton;
    private Rect multiPlayerButton;
    private int gameWidth;
    private int gameHeight;
    private Paint settingsPaint;
    private Paint titlePaint;

    public TitleScreen(Game game)
    {
        super(game);
        //androidImage = new AndroidImage(createTitleBitmap(), AndroidGraphics.ImageFormat.RGB565);
        androidGraphics = game.getGraphics();

        gameWidth = androidGraphics.getWidth();
        gameHeight = androidGraphics.getHeight();

        singlePlayerButton = new Rect(Configuration.GAME_WIDTH / 3,
                Configuration.GAME_HEIGHT / 5,
                Configuration.GAME_WIDTH /3 * 2,
                Configuration.GAME_HEIGHT / 5 * 2);
        multiPlayerButton = new Rect(Configuration.GAME_WIDTH / 3,
                Configuration.GAME_HEIGHT / 5 * 3,
                Configuration.GAME_WIDTH / 3 * 2,
                Configuration.GAME_HEIGHT / 5 * 4);

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
        boolean isSelected = false;
        boolean isSettings = false;
        boolean isSinglePlayer = false;

        for(AndroidInput.TouchEvent event : game.getInput().getTouchEvents())
        {
            if(event.type == AndroidInput.TouchEvent.TOUCH_DOWN)
            {
                Rect point = new Rect(event.x, event.y, event.x, event.y);

                if(event.x > Configuration.GAME_WIDTH - 100 && event.y < Configuration.GAME_HEIGHT / 4)
                {
                    isSelected = true;
                    isSettings = true;
                }
                else if(Rect.intersects(point, singlePlayerButton))
                {
                    isSelected = true;
                    isSinglePlayer = true;
                }
                else if(Rect.intersects(point, multiPlayerButton))
                {
                    isSelected = true;
                }
            }
        }

        if(isSelected)
        {
            if(isSettings)
            {
                Intent intent = new Intent(game.getActivity(), SettingsActivity.class);
                game.getActivity().startActivity(intent);
            }
            else if(isSinglePlayer)
            {
                game.setScreen(new GameScreen(game));
            }
            else
            {
                Intent intent = new Intent(game.getActivity(), FieldGameMultiplayer.class);
                game.getActivity().startActivity(intent);
            }
        }
    }

    public void paint(float deltaTime)
    {
        androidGraphics.clearScreen(Color.BLUE);
        androidGraphics.drawRect(Configuration.GAME_WIDTH - 100, Configuration.GAME_HEIGHT / 4, 100, 0, Color.LTGRAY);
        androidGraphics.drawString("Settings",Configuration.GAME_WIDTH - 50, Configuration.GAME_HEIGHT / 8, settingsPaint);
        androidGraphics.drawString("Fields", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 8, titlePaint);
        androidGraphics.drawRectObject(singlePlayerButton, Color.LTGRAY);
        androidGraphics.drawRectObject(multiPlayerButton, Color.LTGRAY);
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
