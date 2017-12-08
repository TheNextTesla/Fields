package independent_study.fields.game;

import android.os.Bundle;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.Screen;

/**
 * Created by Blaine Huey on 11/6/2017.
 */

public class FieldGame extends AndroidGame
{
    private int objectiveScore;
    private int timeScore;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        objectiveScore = 0;
        timeScore = 0;
    }

    @Override
    public Screen getInitScreen()
    {
        return new TitleScreen(this);
    }

    @Override
    public void onBackPressed()
    {
        getCurrentScreen().backButton();
    }

    public int getGameScore()
    {
        return objectiveScore + timeScore;
    }

    public void setTimeScore(int newScore)
    {
        timeScore = newScore;
    }

    public void incrementObjectiveScore(int increase)
    {
        objectiveScore += increase;
    }

    public void clearGameScore()
    {
        objectiveScore = 0;
        timeScore = 0;
    }
}
