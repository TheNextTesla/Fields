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
    private Paint otherPaint;
    private long playerHighScore;
    private String response;

    private static final String[] negativeSnarkyRemarks =
            {
                    "Better Luck Next Time",
                    "WOW, that was bad.  Is this actually your high score?",
                    "It's just physics. IT'S NOT THAT HARD...",
                    "Did you fall asleep?",
                    "It's very depressing to watch you fail over and over again"
            };
    private static final String[] positiveSnarkyRemarks =
            {
                    "Remember, holding will keep your charge neutral",
                    "Your initial sign is determined based on what side of the middle you click",
                    "Keep trying!",
                    "Your charge accelerates you.  Be careful not too switch to late."
            };

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

        otherPaint = new Paint();
        otherPaint.setTextSize(20);
        otherPaint.setTextAlign(Paint.Align.CENTER);
        otherPaint.setAntiAlias(true);
        otherPaint.setColor(Color.WHITE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(game.getApplicationContext());
        playerHighScore = sharedPreferences.getLong(Configuration.HIGH_SCORE_TAG, -1);

        response = generateSnarkyRemark();
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
        game.getGraphics().drawString("GAME OVER.", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 3, titlePaint);
        game.getGraphics().drawString("High Score: " + playerHighScore, Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 3 + 50, subTitlePaint);
        game.getGraphics().drawString("Score: " + ((FieldGame) game).getGameScore(), Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 3 + 100, subTitlePaint);
        game.getGraphics().drawString("Tap to return", Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 3 + 150, subTitlePaint);
        game.getGraphics().drawString(response, Configuration.GAME_WIDTH / 2, Configuration.GAME_HEIGHT / 3 + 190, otherPaint);
    }

    public void pause()
    {

    }

    public void resume()
    {

    }

    public void dispose()
    {
        ((FieldGame) game).clearGameScore();
    }

    public void backButton()
    {
        game.setScreen(new TitleScreen(game));
    }

    private String generateSnarkyRemark()
    {
        if(((FieldGame) game).getGameScore() < playerHighScore - 100 && (Math.random() < 0.2))
        {
            int index = (int) (Math.random() * negativeSnarkyRemarks.length);
            return negativeSnarkyRemarks[index];
        }
        else
        {
            int index = (int) (Math.random() * positiveSnarkyRemarks.length);
            return positiveSnarkyRemarks[index];
        }
    }
}
