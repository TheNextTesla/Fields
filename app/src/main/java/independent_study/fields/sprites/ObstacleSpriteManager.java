package independent_study.fields.sprites;

import java.util.ArrayList;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.game.Configuration;

/**
 * Created by Blaine Huey on 11/6/2017.
 */

public class ObstacleSpriteManager
{
    private static final double DIFFICULTY_INCREMENT = 0.001;
    private static final double STATE_FREQUENCY_CUTOFF = 4;
    private static final double STATE_CLUSTERING_SPACING = 20;

    private double frequency;
    private double clustering;
    private ArrayList<ObstacleSprite> obstacles;
    private AndroidGame androidGame;

    public ObstacleSpriteManager(double startingFrequency, double startingClustering, AndroidGame game)
    {
        frequency = startingFrequency;
        clustering = startingClustering;
        obstacles = updateObstacleList();

        androidGame = game;
    }

    public void updateGenerateObstacle()
    {
        obstacles = updateObstacleList();

        int randomStateClustering = (int) (Math.random() * clustering * 10);
        int randomStateFrequency = (int) (Math.random() * frequency * 10);

        if(randomStateFrequency > STATE_FREQUENCY_CUTOFF)
        {
            int randomStartCoordinate = (int) (Math.random() * (Configuration.FIELD_WIDTH + 1));

            for(int i = 0; i < randomStateClustering / 3; i++)
            {
                if(randomStartCoordinate - (Configuration.FIELD_WIDTH / 2) >= 0)
                {
                    obstacles.add(new ObstacleSprite((int) Math.round(randomStartCoordinate - STATE_CLUSTERING_SPACING), androidGame.getGraphics()));
                }
                else
                {
                    obstacles.add(new ObstacleSprite((int) Math.round(randomStartCoordinate + STATE_CLUSTERING_SPACING), androidGame.getGraphics()));
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

    private void incrementDifficulty()
    {
        int randomState = (int) (Math.random() * 3);

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
