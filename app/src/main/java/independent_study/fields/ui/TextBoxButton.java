package independent_study.fields.ui;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import independent_study.fields.framework.Game;

/**
 * Created by Blaine Huey on 2/2/2018.
 */

public class TextBoxButton extends BoxButton
{
    private Paint textPaint;
    private String textContent;
    private int numLines;
    private int textHeight;

    public TextBoxButton(Rect bounds, int backgroundColor, String text, int textSize, int textColor, Game game)
    {
        super(bounds, backgroundColor, game);
        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textContent = text;
        textHeight = textSize / 3;
        numLines = text.split("\n").length;
    }

    public TextBoxButton(int left, int top, int right, int bottom, int backgroundColor, String text, int textSize, int textColor, Game game)
    {
        super(left, top, right, bottom, backgroundColor, game);
        textPaint = new Paint();
        textPaint.setTextSize(textSize);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setAntiAlias(true);
        textPaint.setColor(textColor);
        textContent = text;
        textHeight = textSize / 3;
        numLines = text.split("\n").length;
    }

    @Override
    public void display()
    {
        super.display();
        if(numLines > 1)
        {
            buttonGame.getGraphics().drawString(textContent, buttonBounds.centerX(),
                    buttonBounds.centerY() + textHeight, textPaint);
        }
        else
        {
            for(int i = 0; i < numLines; i++)
            {
                buttonGame.getGraphics().drawString(textContent.split("\n")[i],
                        buttonBounds.centerX(), buttonBounds.top + buttonBounds.height() * (i + 1) / (numLines + 1), textPaint);
            }
        }
    }

    public void setText(String text)
    {
        textContent = text;
    }
}
