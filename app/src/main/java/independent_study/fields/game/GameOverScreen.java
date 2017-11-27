package independent_study.fields.game;

import android.graphics.Color;
import android.graphics.Paint;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.AndroidInput;
import independent_study.fields.framework.Screen;

/**
 * Created by Blaine Huey on 11/14/2017.
 */

public class GameOverScreen extends Screen
{
    Paint titlePaint;
    Paint subTitlePaint;

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
        game.getGraphics().drawRect(0, 0, 1281, 801, Color.BLACK);
        game.getGraphics().drawString("GAME OVER.", 400, 240, titlePaint);
        game.getGraphics().drawString("Tap to return.", 400, 290, subTitlePaint);
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
