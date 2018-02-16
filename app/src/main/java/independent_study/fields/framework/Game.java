package independent_study.fields.framework;

import android.app.Activity;
import android.content.res.Resources;
/**
 * Derived from KiloBolt Apache-Licensed Code
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
