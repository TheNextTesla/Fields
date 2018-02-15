package independent_study.fields.game;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.preference.PreferenceManager;

import independent_study.fields.R;
import independent_study.fields.framework.AndroidGraphics;
import independent_study.fields.framework.AndroidImage;
import independent_study.fields.framework.AndroidInput;
import independent_study.fields.framework.Game;
import independent_study.fields.framework.Screen;
import independent_study.fields.sprites.ObstacleSprite;
import independent_study.fields.sprites.PlayerSprite;
import independent_study.fields.sprites.Sprite;
import independent_study.fields.sprites.WallSprite;
import independent_study.fields.ui.TextBoxButton;

/**
 * Created by Blaine Huey on 2/14/2018.
 */

public class TutorialScreen extends Screen
{
    private AndroidGraphics graphics;
    private AndroidInput input;

    private PlayerSprite playerSprite;
    private WallSprite wallSpriteL;
    private WallSprite wallSpriteR;
    private AndroidImage skyImage;

    private SharedPreferences sharedPreferences;
    protected Rect gameRegion;
    private int currentInstruction;
    private boolean shouldProceedToNext;

    private Rect textBoxRect;
    private TextBoxButton textBox;
    private boolean hasBeenReleased;

    public TutorialScreen(Game game)
    {
        super(game);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(game.getActivity().getApplicationContext());
        currentInstruction = 0;
        shouldProceedToNext = false;

        input = game.getInput();
        graphics = game.getGraphics();
        wallSpriteL = WallSprite.generateDefault(WallSprite.DEFAULT_WALL_TYPE.LEFT, sharedPreferences.getBoolean(Configuration.POSITIVE_PLATE_LEFT_TAG, true), game);
        wallSpriteR = WallSprite.generateDefault(WallSprite.DEFAULT_WALL_TYPE.RIGHT, !sharedPreferences.getBoolean(Configuration.POSITIVE_PLATE_LEFT_TAG, true), game);
        playerSprite = new PlayerSprite(game, sharedPreferences.getBoolean(Configuration.POSITIVE_PLATE_LEFT_TAG, true));
        textBoxRect = new Rect(graphics.getWidth() / 3, graphics.getHeight() / 3, graphics.getWidth() * 2 / 3, graphics.getHeight() * 2 / 3);
        gameRegion = new Rect((Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2, 0,
                (Configuration.FIELD_WIDTH + (Configuration.GAME_WIDTH - Configuration.FIELD_WIDTH) / 2),
                Configuration.GAME_HEIGHT);

        textBox = new TextBoxButton(textBoxRect, Color.argb(0,1,1 ,1), "Press Anywhere for Tutorial", 30, Color.DKGRAY, game);
        Bitmap settingsBitmap = BitmapFactory.decodeResource(game.getResources(), R.drawable.sky);
        skyImage = new AndroidImage(settingsBitmap, AndroidGraphics.ImageFormat.ARGB4444);
    }

    public void update(float deltaTime)
    {
        shouldProceedToNext = false;

        for(AndroidInput.TouchEvent touchEvent : input.getTouchEvents())
        {
            if(touchEvent.type == AndroidInput.TouchEvent.TOUCH_DOWN)
            {
                shouldProceedToNext = true;
                hasBeenReleased = false;
            }
            else if(touchEvent.type == AndroidInput.TouchEvent.TOUCH_UP)
            {
                hasBeenReleased = true;
            }
        }

        if(shouldProceedToNext)
        {
            currentInstruction++;
        }

        playerSprite.setSpeed(0);
    }

    public void paint(float deltaTime)
    {
        graphics.drawScaledImage(skyImage, gameRegion);

        switch (currentInstruction)
        {
            case 0:
                playerSprite.setLocationX(graphics.getWidth() / 2);
                break;
            case 1:
                playerSprite.setLocationX(graphics.getWidth() / 3);
                textBox.setText("Pressing the Screen Alternates your Acceleration\n" +
                        "You do not control your player's speed, just their direction of acceleration");
                break;
            case 2:
                playerSprite.setLocationX(graphics.getWidth() / 2);
                textBox.setText("Holding the Screen Stops Your Acceleration\n" +
                        "It Stops Your Change in Speed, Not Your Player!");
                break;
            case 3:
                playerSprite.setLocationX(graphics.getWidth() / 3 * 2);
                textBox.setText("Avoid the Electric Walls On Either Side\n" +
                        "You Will DIE.  Instantly.");
            case 4:
                playerSprite.setLocationX(graphics.getWidth() / 5 * 2);
                new ObstacleSprite(graphics.getWidth() / 2, graphics.getHeight() / 2, 0, game).paint();
                textBox.setText("Dodge the Incoming Missiles\n" +
                        "Occasionally, there will be a green 'Good' Missile");
                break;
            default:
                game.setScreen(new TitleScreen(game));
                break;
        }

        playerSprite.paint();
        wallSpriteL.paint();
        wallSpriteR.paint();
        textBox.display();
    }

    public void pause()
    {

    }

    public void resume()
    {

    }

    public void dispose()
    {
        Sprite.destroyAll();
    }

    public void backButton()
    {
        game.setScreen(new TitleScreen(game));
    }
}
