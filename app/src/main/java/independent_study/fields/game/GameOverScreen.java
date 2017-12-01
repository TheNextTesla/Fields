package independent_study.fields.game;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.AndroidInput;
import independent_study.fields.framework.Screen;

/**
 * Created by Blaine Huey on 11/14/2017.
 */

public class GameOverScreen extends Screen
{
    private Paint titlePaint;
    private Paint subTitlePaint;
    private long playerScore;

    public GameOverScreen(AndroidGame game)
    {
        super(game);

        titlePaint = new Paint();
        titlePaint.setTextSize(100);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setAntiAlias(true);
        titlePaint.setColor(Color.WHITE);

        subTitlePaint = new Paint();
        subTitlePaint.setTextSize(30);
        subTitlePaint.setTextAlign(Paint.Align.CENTER);
        subTitlePaint.setAntiAlias(true);
        subTitlePaint.setColor(Color.WHITE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(game.getApplicationContext());
        playerScore = sharedPreferences.getLong(Configuration.HIGH_SCORE_TAG, -1);
    }

    public void update(float deltaTime)
    {
        boolean touchTriggered = false;
        for(AndroidInput.TouchEvent event : game.getInput().getTouchEvents())
        {
            if(event.type == AndroidInput.TouchEvent.TOUCH_DOWN)
            {
                touchTriggered = true;
            }
        }

        if(touchTriggered)
        {
            game.setScreen(new TitleScreen(game));
        }
    }

    public void paint(float deltaTime)
    {
        game.getGraphics().clearScreen(Color.BLACK);
        game.getGraphics().drawString("GAME OVER.", Configuration.GAME_WIDTH / 2, 240, titlePaint);
        game.getGraphics().drawString("Tap to return.", Configuration.GAME_WIDTH / 2, 290, subTitlePaint);
        game.getGraphics().drawString("Score: " + playerScore, Configuration.GAME_WIDTH / 2, 340, subTitlePaint);
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
        game.setScreen(new TitleScreen(game));
    }
}
