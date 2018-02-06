package independent_study.fields.game;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import independent_study.fields.R;
import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidImage;
import independent_study.fields.framework.AndroidInput;
import independent_study.fields.framework.Game;
import independent_study.fields.framework.Screen;
import independent_study.fields.network.FieldGameMultiplayer;
import independent_study.fields.settings.SettingsActivity;
import independent_study.fields.ui.IconButton;
import independent_study.fields.ui.TextBoxButton;

/**
 * Created by Blaine Huey on 11/6/2017.
 */

public class TitleScreen extends Screen
{
    private static final String LOG_TAG = "TitleScreen";

    private AndroidGraphics androidGraphics;
    private AndroidImage settingsImage;

    private Rect singlePlayerButtonRect;
    private Rect multiPlayerButtonRect;
    private Rect settingsButtonRect;
    private TextBoxButton singlePlayerButton;
    private TextBoxButton multiPlayerButton;
    private IconButton settingsButton;
    private int gameWidth;
    private int gameHeight;
    private Paint titlePaint;

    public TitleScreen(Game game)
    {
        super(game);
        androidGraphics = game.getGraphics();

        gameWidth = androidGraphics.getWidth();
        gameHeight = androidGraphics.getHeight();

        Resources resources = game.getResources();
        //int resourceId = resources.getIdentifier("settings.png" , "", game.getActivity().getPackageName());
        Bitmap settingsBitmap = BitmapFactory.decodeResource(resources, R.drawable.settings);

        /*
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < settingsBitmap.getWidth(); i++)
        {
            for(int j = 0; j < settingsBitmap.getHeight(); j++)
            {
                builder.append(settingsBitmap.getPixel(i, j));
            }
        }
        */
        Log.d(LOG_TAG, "Settings Bytes: " + settingsBitmap.getByteCount());
        //Log.d(LOG_TAG, "Example Pixel: " + builder.toString());
        settingsImage = new AndroidImage(settingsBitmap, AndroidGraphics.ImageFormat.ARGB4444);

        singlePlayerButtonRect = new Rect(gameWidth / 3,
                gameHeight / 5,
                gameWidth / 3 * 2,
                gameHeight / 5 * 2);
        multiPlayerButtonRect = new Rect(gameWidth / 3,
                gameHeight / 5 * 3,
                gameWidth / 3 * 2,
                gameHeight / 5 * 4);
        settingsButtonRect = new Rect(gameWidth - 50,
                50, gameWidth - 10, 10);

        singlePlayerButton = new TextBoxButton(singlePlayerButtonRect, Color.LTGRAY,
                "Single Player", 20, Color.WHITE, game);
        multiPlayerButton = new TextBoxButton(multiPlayerButtonRect, Color.LTGRAY,
                "Multi Player", 20, Color.WHITE, game);
        settingsButton = new IconButton(settingsButtonRect, settingsImage, game);

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
                else if(singlePlayerButton.isPressed(point))
                {
                    isSelected = true;
                    isSinglePlayer = true;
                }
                else if(multiPlayerButton.isPressed(point))
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
        //androidGraphics.drawRect(Configuration.GAME_WIDTH - 100, Configuration.GAME_HEIGHT / 4, 100, 0, Color.LTGRAY);
        //androidGraphics.drawString("Settings",Configuration.GAME_WIDTH - 50, Configuration.GAME_HEIGHT / 8, settingsPaint);
        androidGraphics.drawString("Fields", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 8, titlePaint);
        //androidGraphics.drawRectObject(singlePlayerButton, Color.LTGRAY);
        //androidGraphics.drawRectObject(multiPlayerButton, Color.LTGRAY);
        //Log.d(LOG_TAG, "paint");
        multiPlayerButton.display();
        singlePlayerButton.display();
        settingsButton.display();
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
