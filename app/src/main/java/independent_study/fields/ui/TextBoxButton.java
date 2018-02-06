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
        textHeight = textSize / 2;
    }

    @Override
    public void display()
    {
        super.display();
        buttonGame.getGraphics().drawString(textContent, buttonBounds.centerX(), buttonBounds.centerY() + textHeight, textPaint);
    }
}
