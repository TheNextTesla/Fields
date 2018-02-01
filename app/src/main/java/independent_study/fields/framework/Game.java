package independent_study.fields.framework;

import android.app.Activity;
import android.content.res.Resources;

/**
 * Created by Blaine Huey on 12/12/2017.
 */

public interface Game
{
    AndroidInput getInput();
    AndroidGraphics getGraphics();
    AndroidAudio getAudio();
    Activity getActivity();
    Resources getResources();

    void setScreen(Screen screen);
    Screen getCurrentScreen();
    Screen getInitScreen();

    int getGameScore();
    void setTimeScore(int newScore);
    void incrementObjectiveScore(int increase);
    void clearGameScore();
}
