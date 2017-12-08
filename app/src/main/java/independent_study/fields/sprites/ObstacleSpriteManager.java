package independent_study.fields.sprites;

import android.util.Log;

import java.util.ArrayList;

import independent_study.fields.framework.AndroidGame;
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

    private double frequency;
    private double clustering;
    private ObstacleSprite.OBSTACLE_SPEED obstacleSpeed;
    private ArrayList<ObstacleSprite> obstacles;
    private AndroidGame androidGame;

    /**
     * A Manager for All the Obstacles 
     * @param startingFrequency - The Probability of an Obstacle Appearing
     * @param startingClustering - The Probability Relationship to the Number of Obstacles that Appear
     * @param speedMultiplier - A Setting Multiplier for Obstacle Speed
     * @param game - Android Game
     */
    public ObstacleSpriteManager(double startingFrequency, double startingClustering, ObstacleSprite.OBSTACLE_SPEED speedMultiplier, AndroidGame game)
    {
        frequency = startingFrequency;
        clustering = startingClustering;
        obstacles = updateObstacleList();
        obstacleSpeed = speedMultiplier;

        androidGame = game;
    }

    /**
     * Simplified Alternative Constructor With Default Parameters
     * @param speedMultiplier - A Setting Multiplier for Obstacle Speed
     * @param game -  Android Game
     */
    public ObstacleSpriteManager(ObstacleSprite.OBSTACLE_SPEED speedMultiplier, AndroidGame game)
    {
        this(0.31, 0.1, speedMultiplier, game);
    }

    /**
     * Probabilistically Generates the Obstacles In the Game
     */
    public void updateGenerateObstacle()
    {
        obstacles = updateObstacleList();

        int randomStateClustering = (int) (Math.random() * clustering * 10);
        int randomStateFrequency = (int) (Math.random() * frequency * 10);

        //Guarantees that their is Always At Least One Obstacle
        if(obstacles.size() == 0)
        {
            int randomStartCoordinate = (int) (Math.random() * (Configuration.FIELD_WIDTH + 1));
            while(createNewCheckedObstacleSprite(randomStartCoordinate));
            return;
        }

        if(randomStateFrequency + 2 > STATE_FREQUENCY_CUTOFF)
        {
            int randomStartCoordinate = (int) (Math.random() * (Configuration.FIELD_WIDTH + 1));

            for(int i = 0; i < randomStateClustering / 3; i++)
            {
                while(createNewCheckedObstacleSprite(randomStartCoordinate));
            }
        }
        else
        {
            incrementDifficulty();
        }
    }

    /**
     * Probabilistically Generates the Obstacles and Objectives in the Game
     */
    public void updateGenerateObstacleAndObjective()
    {
        obstacles = updateObstacleList();

        int randomStateClustering = (int) (Math.random() * clustering * 10);
        int randomStateFrequency = (int) (Math.random() * frequency * 10);

        //Guarantees that their is Always At Least One Obstacle
        if(obstacles.size() == 0)
        {
            int randomStartCoordinate = (int) (Math.random() * (Configuration.FIELD_WIDTH + 1));
            while(createNewCheckedObjectiveSprite(randomStartCoordinate));

            randomStartCoordinate = (int) (Math.random() * (Configuration.FIELD_WIDTH + 1));
            while(createNewCheckedObstacleSprite(randomStartCoordinate));
            return;
        }

        if(randomStateFrequency + 2 > STATE_FREQUENCY_CUTOFF)
        {
            int randomStartCoordinate = (int) (Math.random() * (Configuration.FIELD_WIDTH + 1));

            for(int i = 0; i < randomStateClustering / 3; i++)
            {
                while(createNewCheckedObjectiveSprite(randomStartCoordinate));
                randomStartCoordinate = (int) (Math.random() * (Configuration.FIELD_WIDTH + 1));
                while(createNewCheckedObstacleSprite(randomStartCoordinate));
            }
        }
        else
        {
            incrementDifficulty();
        }
    }

    /**
     * Conditionally Creates and Obstacle at a Location
     * @param randomStartCoordinate - X Coordinate for the Center of the Obstacle
     * @return If The Creation Was Successful Without Overlapping Another
     */
    private boolean createNewCheckedObstacleSprite(int randomStartCoordinate)
    {
        ObstacleSprite tempObstacleSprite;

        if (randomStartCoordinate - (Configuration.FIELD_WIDTH / 2) >= 0)
        {
            tempObstacleSprite = new ObstacleSprite((int) Math.round(randomStartCoordinate - STATE_CLUSTERING_SPACING), obstacleSpeed, androidGame.getGraphics());
        }
        else
        {
            tempObstacleSprite = new ObstacleSprite((int) Math.round(randomStartCoordinate + STATE_CLUSTERING_SPACING), obstacleSpeed, androidGame.getGraphics());
        }

        boolean wouldInterfere = false;
        for (ObstacleSprite otherObstacleSprite : obstacles)
        {
            if (otherObstacleSprite.isTouching(tempObstacleSprite) && otherObstacleSprite != tempObstacleSprite)
            {
                wouldInterfere = true;
            }
        }

        if (!wouldInterfere)
        {
            obstacles.add(tempObstacleSprite);
        }
        else
        {
            tempObstacleSprite.destroy();
        }
        return !wouldInterfere;
    }

    /**
     * Conditionally Creates and Objective at a Location
     * @param randomStartCoordinate - X Coordinate for the Center of the Objective
     * @return If The Creation Was Successful Without Overlapping Another
     */
    private boolean createNewCheckedObjectiveSprite(int randomStartCoordinate)
    {
        ObjectiveSprite tempObstacleSprite;

        if (randomStartCoordinate - (Configuration.FIELD_WIDTH / 2) >= 0)
        {
            tempObstacleSprite = new ObjectiveSprite((int) Math.round(randomStartCoordinate - STATE_CLUSTERING_SPACING), obstacleSpeed, androidGame.getGraphics());
        }
        else
        {
            tempObstacleSprite = new ObjectiveSprite((int) Math.round(randomStartCoordinate + STATE_CLUSTERING_SPACING), obstacleSpeed, androidGame.getGraphics());
        }

        boolean wouldInterfere = false;
        for (ObstacleSprite otherObstacleSprite : obstacles)
        {
            if (otherObstacleSprite.isTouching(tempObstacleSprite) && otherObstacleSprite != tempObstacleSprite)
            {
                wouldInterfere = true;
            }
        }

        if (!wouldInterfere)
        {
            obstacles.add(tempObstacleSprite);
        }
        else
        {
            tempObstacleSprite.destroy();
        }
        return !wouldInterfere;
    }

    /**
     * Returns the Current List of Obstacles
     * @return the Current List of Obstacles
     */
    public ArrayList<ObstacleSprite> getObstacles()
    {
        return obstacles;
    }

    /**
     * Runs the Update Method on All Known Obstacles
     */
    public void updateAllObstacles()
    {
        for(ObstacleSprite obstacleSprite : getObstacles())
        {
            obstacleSprite.update();
        }
    }

    /**
     * Runs the Paint Method on All Known Obstacles
     */
    public void paintAllObstacles()
    {
        for(ObstacleSprite obstacleSprite : getObstacles())
        {
            obstacleSprite.paint();
        }
    }

    /**
     * Runs the Delete Method on All Known Obstacles
     */
    public void deleteAllObstacles()
    {
        obstacles = updateObstacleList();
        for(ObstacleSprite obstacleSprite : obstacles)
        {
            obstacleSprite.destroy();
        }
    }

    /**
     * Conditionally Increases the Difficulty (Probability) of Obstacles
     */
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

    /**
     * Generates a New Obstacle List of All Identified Ones from Sprite.sprites
     * @return a New Obstacle List of All Identified Ones from Sprite.sprites
     */
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

    /**
     * Returns the Current 'Difficulty' of the Manager
     * @return the Current 'Difficulty' of the Manager
     */
    public double getDifficulty()
    {
        return frequency * clustering;
    }
}
