package independent_study.fields.network;

import android.content.Intent;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.AndroidInput;
import independent_study.fields.framework.Screen;
import independent_study.fields.game.Configuration;
import independent_study.fields.game.GameScreen;
import independent_study.fields.game.TitleScreen;
import independent_study.fields.settings.SettingsActivity;

/**
 * Created by Blaine Huey on 12/11/2017.
 */

public class MultiGameScreen extends Screen implements Networked
{
    public MultiGameScreen(AndroidGame game)
    {
        super(game);
    }

    public void update(float deltaTime)
    {
        boolean touchTriggered = false;
        boolean isSettingsSelection = false;
        for(AndroidInput.TouchEvent event : game.getInput().getTouchEvents())
        {
            if(event.type == AndroidInput.TouchEvent.TOUCH_DOWN)
            {
                touchTriggered = true;

                if(event.x > Configuration.GAME_WIDTH - 100 && event.y < Configuration.GAME_HEIGHT / 4)
                {
                    isSettingsSelection = true;
                }
            }
        }

        if(touchTriggered)
        {
            if (!isSettingsSelection)
            {
                game.setScreen(new GameScreen(game));
            }
            else
            {
                Intent intent = new Intent(game.getActivity(), SettingsActivity.class);
                game.getActivity().startActivity(intent);
            }
        }
    }

    public void paint(float deltaTime)
    {

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

    public void connected()
    {

    }

    public void disconnected()
    {

    }
}
