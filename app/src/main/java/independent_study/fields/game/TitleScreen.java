package independent_study.fields.game;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import independent_study.fields.R;
import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidImage;
import independent_study.fields.framework.AndroidInput;
import independent_study.fields.framework.Game;
import independent_study.fields.framework.Screen;
import independent_study.fields.network.FieldGameMultiplayer;
import independent_study.fields.settings.SettingsActivity;
import independent_study.fields.sprites.CloudSpriteManager;
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
    private AndroidImage backgroundImage;
    private CloudSpriteManager cloudSpriteManager;

    private Rect singlePlayerButtonRect;
    private Rect multiPlayerButtonRect;
    private Rect settingsButtonRect;
    private Rect tutorialButtonRect;
    private Rect screenRect;
    private TextBoxButton singlePlayerButton;
    private TextBoxButton multiPlayerButton;
    private IconButton settingsButton;
    private TextBoxButton tutorialButton;
    private int gameWidth;
    private int gameHeight;
    private Paint titlePaint;


    public TitleScreen(Game game)
    {
        super(game);
        androidGraphics = game.getGraphics();

        gameWidth = androidGraphics.getWidth();
        gameHeight = androidGraphics.getHeight();

        screenRect = new Rect(0, 0, gameWidth, gameHeight);

        cloudSpriteManager = new CloudSpriteManager(5.0, game);

        Resources resources = game.getResources();
        Bitmap settingsBitmap = BitmapFactory.decodeResource(resources, R.drawable.settings);
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
        tutorialButtonRect = new Rect(50, 50, 0,0);

        singlePlayerButton = new TextBoxButton(singlePlayerButtonRect, Color.LTGRAY,
                "Single Player", 20, Color.WHITE, game);
        multiPlayerButton = new TextBoxButton(multiPlayerButtonRect, Color.LTGRAY,
                "Multi Player", 20, Color.WHITE, game);
        settingsButton = new IconButton(settingsButtonRect, settingsImage, game);
        tutorialButton = new TextBoxButton(tutorialButtonRect, Color.LTGRAY,
                    "T", 20, Color.WHITE, game);

        titlePaint = new Paint();
        titlePaint.setTextSize(50);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setAntiAlias(true);
        titlePaint.setColor(Color.WHITE);

        Bitmap backgroundBitmap = BitmapFactory.decodeResource(game.getResources(), R.drawable.sky);
        backgroundImage = new AndroidImage(backgroundBitmap, AndroidGraphics.ImageFormat.ARGB4444);
    }

    public void update(float deltaTime)
    {
        boolean isSelected = false;
        boolean isSettings = false;
        boolean isSinglePlayer = false;
        boolean isTutorial = false;

        for(AndroidInput.TouchEvent event : game.getInput().getTouchEvents())
        {
            if(event.type == AndroidInput.TouchEvent.TOUCH_DOWN)
            {
                Rect point = new Rect(event.x, event.y, event.x, event.y);

                if(settingsButton.isPressed(point))
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
                else if(tutorialButton.isPressed(point))
                {
                    isSelected = true;
                    isTutorial = true;
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
            else if(isTutorial)
            {
                game.setScreen(new TutorialScreen(game));
            }
            else
            {
                Intent intent = new Intent(game.getActivity(), FieldGameMultiplayer.class);
                game.getActivity().startActivity(intent);
            }
        }

        cloudSpriteManager.update(deltaTime);
    }

    public void paint(float deltaTime)
    {
        androidGraphics.drawScaledImage(backgroundImage, screenRect);
        cloudSpriteManager.paint();
        androidGraphics.drawString("Fields", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 8, titlePaint);
        multiPlayerButton.display();
        singlePlayerButton.display();
        settingsButton.display();
        tutorialButton.display();
    }

    public void pause()
    {

    }

    public void resume()
    {

    }

    public void dispose()
    {
        cloudSpriteManager.deleteAllClouds();
    }

    public void backButton()
    {
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
