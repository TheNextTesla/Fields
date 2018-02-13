package independent_study.fields.sprites;

import android.graphics.Color;
import android.graphics.Point;

import java.util.ArrayList;

import independent_study.fields.framework.Game;

/**
 * Created by Blaine Huey on 2/9/2018.
 */

public class CloudSpriteManager
{
    private ArrayList<CloudSprite> cloudSprites;
    private double secondsPerCloud;
    private Game game;
    private double milliTime;

    public CloudSpriteManager(double secondsPerCloud, Game game)
    {
        cloudSprites = new ArrayList<>();
        this.secondsPerCloud = secondsPerCloud;
        this.game = game;
        milliTime = System.currentTimeMillis();
        createNewCloud();
    }

    public void update()
    {
        if((System.currentTimeMillis() - milliTime) > secondsPerCloud * 1000)
        {
            milliTime = System.currentTimeMillis();
            createNewCloud();
        }

        for(int i = cloudSprites.size() - 1; i >= 0; i--)
        {
            if(cloudSprites.get(i).spriteBounds.top > game.getGraphics().getHeight())
            {
                cloudSprites.remove(i).destroy();
            }
            else
            {
                cloudSprites.get(i).update();
            }
        }
    }

    public void paint()
    {
        //game.getGraphics().clearScreen(Color.LTGRAY);

        for(CloudSprite cloudSprite : cloudSprites)
        {
            cloudSprite.paint();
        }
    }

    public void createNewCloud()
    {
        int randomSize = (int) (Math.random() * 100 + 50);
        int randomX = (int) (Math.random() * game.getGraphics().getWidth());
        Point point = new Point(randomX, -randomSize);
        cloudSprites.add(new CloudSprite(point, randomSize, game));
    }

    public void deleteAllClouds()
    {
        for(CloudSprite cloudSprite : cloudSprites)
        {
            cloudSprite.destroy();
        }
    }
}
