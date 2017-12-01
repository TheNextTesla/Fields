package independent_study.fields.sprites;

import android.util.Log;

import java.util.ArrayList;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.AndroidInput;
import independent_study.fields.game.Configuration;

/**
 * Created by Blaine Huey on 11/6/2017.
 */

public class ObstacleSpriteManager
{
    public static final String LOG_TAG = "ObstacleSpriteManager";

    private static final double DIFFICULTY_INCREMENT = 0.001;
    private static final double STATE_FREQUENCY_CUTOFF = 4;
    private static final double STATE_CLUSTERING_SPACING = 20;

    private boolean firstRun;
    private double frequency;
    private double clustering;
    private ObstacleSprite.OBSTACLE_SPEED obstacleSpeed;
    private ArrayList<ObstacleSprite> obstacles;
    private AndroidGame androidGame;

    public ObstacleSpriteManager(double startingFrequency, double startingClustering, ObstacleSprite.OBSTACLE_SPEED speedMultiplier, AndroidGame game)
    {
        firstRun = true;
        frequency = startingFrequency;
        clustering = startingClustering;
        obstacles = updateObstacleList();
        obstacleSpeed = speedMultiplier;

        androidGame = game;
    }

    public ObstacleSpriteManager(ObstacleSprite.OBSTACLE_SPEED speedMultiplier, AndroidGame game)
    {
        this(0.31, 0.1, speedMultiplier, game);
    }

    public void updateGenerateObstacle()
    {
        obstacles = updateObstacleList();

        int randomStateClustering = (int) (Math.random() * clustering * 10);
        int randomStateFrequency = (int) (Math.random() * frequency * 10);

        if(firstRun)
        {
            int randomStartCoordinate = (int) (Math.random() * (Configuration.FIELD_WIDTH + 1));
            ObstacleSprite tempObstacleSprite;
            if(randomStartCoordinate - (Configuration.FIELD_WIDTH / 2) >= 0)
            {
                tempObstacleSprite = new ObstacleSprite((int) Math.round(randomStartCoordinate - STATE_CLUSTERING_SPACING), obstacleSpeed, androidGame.getGraphics());
            }
            else
            {
                tempObstacleSprite = new ObstacleSprite((int) Math.round(randomStartCoordinate + STATE_CLUSTERING_SPACING), obstacleSpeed, androidGame.getGraphics());
            }
            obstacles.add(tempObstacleSprite);
            firstRun = false;
            return;
        }

        //Log.d(LOG_TAG, "Frequency Double: " + Math.random() * frequency * 10);
        //Log.d(LOG_TAG, "Frequency: " + frequency);

        if(randomStateFrequency + 2 > STATE_FREQUENCY_CUTOFF)
        {
            int randomStartCoordinate = (int) (Math.random() * (Configuration.FIELD_WIDTH + 1));

            for(int i = 0; i < randomStateClustering / 3; i++)
            {
                ObstacleSprite tempObstacleSprite = null;
                if(randomStartCoordinate - (Configuration.FIELD_WIDTH / 2) >= 0)
                {
                    tempObstacleSprite = new ObstacleSprite((int) Math.round(randomStartCoordinate - STATE_CLUSTERING_SPACING), obstacleSpeed, androidGame.getGraphics());
                }
                else
                {
                    tempObstacleSprite = new ObstacleSprite((int) Math.round(randomStartCoordinate + STATE_CLUSTERING_SPACING), obstacleSpeed, androidGame.getGraphics());
                }

                boolean wouldInterfere = false;
                for(ObstacleSprite otherObstacleSprite : obstacles)
                {
                    if(otherObstacleSprite.isTouching(tempObstacleSprite) && otherObstacleSprite != tempObstacleSprite)
                    {
                        wouldInterfere = true;
                    }
                }

                if(!wouldInterfere)
                {
                    obstacles.add(tempObstacleSprite);
                    //Log.d(LOG_TAG, "New Obstacle Added");
                }
                else
                {
                    tempObstacleSprite.destroy();
                }
            }
        }
        else
        {
            incrementDifficulty();
        }
    }

    public ArrayList<ObstacleSprite> getObstacles()
    {
        return obstacles;
    }

    public void updateAllObstacles()
    {
        for(ObstacleSprite obstacleSprite : getObstacles())
        {
            obstacleSprite.update();
        }
    }

    public void paintAllObstacles()
    {
        for(ObstacleSprite obstacleSprite : getObstacles())
        {
            obstacleSprite.paint();
        }
    }

    public void deleteAllObstacles()
    {
        obstacles = updateObstacleList();
        for(ObstacleSprite obstacleSprite : obstacles)
        {
            obstacleSprite.destroy();
        }
    }

    private void incrementDifficulty()
    {
        int randomState = (int) (Math.random() * 50 - getDifficulty()); //Originally 3

        if(!(getDifficulty() + DIFFICULTY_INCREMENT >= 1))
        {
            if (randomState < 1 && frequency <= 1)
            {
                frequency += DIFFICULTY_INCREMENT;
            }
            else if (randomState < 2 && clustering  <= 1)
            {
                clustering += DIFFICULTY_INCREMENT;
            }
            else
            {
                //Nothing Should Happen, No Difficulty Increase
                //Just Double Precision Checks (No Going Over 1)
                frequency = frequency > 1 ? 1 : frequency;
                clustering = clustering > 1 ? 1 : frequency;
            }
        }
        else
        {
            //No More Incrementing
            frequency = 1;
            clustering = 1;
        }
    }

    private ArrayList<ObstacleSprite> updateObstacleList()
    {
        ArrayList<ObstacleSprite> obstacleSprites = new ArrayList<>();

        for(Sprite tempSprite : Sprite.sprites)
        {
            if(tempSprite instanceof ObstacleSprite)
            {
                obstacleSprites.add((ObstacleSprite) tempSprite);
            }
        }

        return obstacleSprites;
    }

    public double getDifficulty()
    {
        return frequency * clustering;
    }
}
