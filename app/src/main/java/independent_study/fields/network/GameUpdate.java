package independent_study.fields.network;

import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.Game;
import independent_study.fields.sprites.ObjectiveSprite;
import independent_study.fields.sprites.ObstacleSprite;
import independent_study.fields.sprites.PlayerSprite;
import independent_study.fields.sprites.Sprite;

/**
 * Created by Blaine Huey on 12/15/2017.
 */

public class GameUpdate
{
    private static final String HOST_STATUS = "host";
    private static final String PLAYER_CHARGE = "pc";
    private static final String PLAYER_SPEED = "ps";
    private static final String PLAYER_X = "px";
    private static final String PLAYER_Y = "py";
    private static final String OBSTACLES_X = "ox";
    private static final String OBSTACLES_Y = "oy";
    private static final String OBSTACLES_V = "ov";
    private static final String OBSTACLES_P = "op";
    private static final String TIME_STAMP = "time";

    private boolean isHost;
    private byte playerCharge;
    private double playerSpeed;
    private int playerLocationX;
    private int playerLocationY;
    private int[] obstaclesX;
    private int[] obstaclesY;
    private int[] obstaclesV;
    private int[] obstaclesP;
    private long timeStamp;

    private GameUpdate(boolean isHost, int playerCharge, double playerSpeed, int playerLocationX, int playerLocationY, int[] obstaclesX, int[] obstaclesY, int[] obstaclesV, int[] obstaclesP, long time)
    {
        this.isHost = isHost;

        switch (playerCharge)
        {
            case 1:
                this.playerCharge = 1;
                break;
            case -1:
                this.playerCharge = -1;
                break;
            default:
                this.playerCharge = 0;
                break;
        }

        this.playerSpeed = playerSpeed;
        this.playerLocationX = playerLocationX;
        this.playerLocationY = playerLocationY;
        this.timeStamp = time;

        if(!(obstaclesX == null || obstaclesY == null || obstaclesV == null || obstaclesP == null) && isHost)
        {
            this.obstaclesX = obstaclesX;
            this.obstaclesY = obstaclesY;
            this.obstaclesV = obstaclesV;
            this.obstaclesP = obstaclesP;
        }
    }

    private GameUpdate(boolean isHost, PlayerSprite playerSprite, ArrayList<ObstacleSprite> obstacleSprites)
    {
        this.isHost = isHost;

        switch (playerSprite.getChargeState())
        {
            case POSITIVE:
                playerCharge = 1;
                break;
            case NEGATIVE:
                playerCharge = -1;
                break;
            default:
                playerCharge = 0;
                break;
        }

        this.playerSpeed = playerSprite.getSpeed();
        this.playerLocationX = playerSprite.getLocationX();
        this.playerLocationY = playerSprite.getLocationY();
        this.timeStamp = System.currentTimeMillis();

        if(isHost && obstacleSprites != null)
        {
            obstaclesX = new int[obstacleSprites.size()];
            obstaclesY = new int[obstacleSprites.size()];
            obstaclesV = new int[obstacleSprites.size()];
            obstaclesP = new int[obstacleSprites.size()];

            Log.d("GameUpdate", "obstacleSprites.size(): " + obstacleSprites.size());

            int index = 0;
            for(ObstacleSprite obstacle : obstacleSprites)
            {
                obstaclesX[index] = obstacle.getLocationX();
                obstaclesY[index] = obstacle.getLocationY();
                obstaclesV[index] = obstacle.getSpeed();

                if (obstacle instanceof ObjectiveSprite)
                {
                    obstaclesP[index] = ((ObjectiveSprite) obstacle).getPoints();
                }
                else
                {
                    obstaclesP[index] = 0;
                }

                index++;
            }
        }
    }

    public static GameUpdate generateHostGameUpdate(PlayerSprite playerSprite, ArrayList<ObstacleSprite> obstacleSprites)
    {
        return new GameUpdate(true, playerSprite, obstacleSprites);
    }

    public static GameUpdate generateSimpleGameUpdate(PlayerSprite playerSprite)
    {
        return new GameUpdate(false, playerSprite, null);
    }

    public static GameUpdate regenerateGameUpdate(String json)
    {
        if(json == null || json.equals(""))
            return null;

        try
        {
            JSONObject jsonObject = new JSONObject(json);
            boolean host = jsonObject.getBoolean(HOST_STATUS);
            int charge = jsonObject.getInt(PLAYER_CHARGE);
            double playerV = jsonObject.getDouble(PLAYER_SPEED);
            int playerX = jsonObject.getInt(PLAYER_X);
            int playerY = jsonObject.getInt(PLAYER_Y);
            long time = jsonObject.getLong(TIME_STAMP);

            if(host)
            {
                JSONArray jsonArrayX = (JSONArray) jsonObject.get(OBSTACLES_X);
                JSONArray jsonArrayY = (JSONArray) jsonObject.get(OBSTACLES_Y);
                JSONArray jsonArrayV = (JSONArray) jsonObject.get(OBSTACLES_V);
                JSONArray jsonArrayP = (JSONArray) jsonObject.get(OBSTACLES_P);

                int[] obstaclesX = new int[jsonArrayX.length()];
                int[] obstaclesY = new int[jsonArrayY.length()];
                int[] obstaclesV = new int[jsonArrayP.length()];
                int[] obstaclesP = new int[jsonArrayP.length()];

                Log.d("GameUpdate", "ReceivedJSONArray: " + jsonArrayX.toString());

                for(int i = 0; i < Math.min(jsonArrayX.length(), jsonArrayY.length()); i++)
                {
                    obstaclesX[i] = jsonArrayX.getInt(i);
                    obstaclesY[i] = jsonArrayY.getInt(i);
                    obstaclesV[i] = jsonArrayV.getInt(i);
                    obstaclesP[i] = jsonArrayP.getInt(i);
                }
                return new GameUpdate(true, charge, playerV, playerX, playerY, obstaclesX, obstaclesY, obstaclesV, obstaclesP, time);
            }
            else
            {
                return new GameUpdate(false, charge, playerV, playerX, playerY, null, null, null, null, time);
            }
        }
        catch(JSONException jsonE)
        {
            jsonE.printStackTrace();
            return null;
        }
    }

    public ArrayList<Sprite> enact(Game game)
    {
        ArrayList<Sprite> newSprites = new ArrayList<>();
        //TODO: Direction Swapping?
        PlayerSprite playerSprite = new PlayerSprite(game, playerLocationX, true);
        if(playerCharge == 1)
        {
            playerSprite.setChargeState(PlayerSprite.CHARGE_STATE.POSITIVE);
        }
        else if(playerCharge == -1)
        {
            playerSprite.setChargeState(PlayerSprite.CHARGE_STATE.NEGATIVE);
        }
        else
        {
            playerSprite.setChargeState(PlayerSprite.CHARGE_STATE.NEUTRAL);
        }

        newSprites.add(playerSprite);

        if(isHost && obstaclesX != null && obstaclesY != null)
        {
            for(int i = 0; i < Math.min(obstaclesX.length, obstaclesY.length); i++)
            {
                if(obstaclesP[i] == 0)
                {
                    newSprites.add(new ObstacleSprite(obstaclesX[i], obstaclesY[i], obstaclesV[i], game.getGraphics()));
                }
                else
                {
                    newSprites.add(new ObjectiveSprite(obstaclesX[i], obstaclesY[i], obstaclesV[i], obstaclesP[i], game.getGraphics()));
                }
            }
        }
        return newSprites;
    }

    public JSONObject toJSON()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put(HOST_STATUS, isHost);
            jsonObject.put(PLAYER_CHARGE, playerCharge);
            jsonObject.put(PLAYER_SPEED, playerSpeed);
            jsonObject.put(PLAYER_X, playerLocationX);
            jsonObject.put(PLAYER_Y, playerLocationY);

            if(isHost)
            {
                JSONArray jsonArrayX = new JSONArray();
                JSONArray jsonArrayY = new JSONArray();
                JSONArray jsonArrayV = new JSONArray();
                JSONArray jsonArrayP = new JSONArray();

                for(int i = 0; i < obstaclesX.length; i++)
                {
                    jsonArrayX.put(obstaclesX[i]);
                    jsonArrayY.put(obstaclesY[i]);
                    jsonArrayV.put(obstaclesV[i]);
                    jsonArrayP.put(obstaclesP[i]);
                }

                jsonObject.put(OBSTACLES_X, jsonArrayX);
                jsonObject.put(OBSTACLES_Y, jsonArrayY);
                jsonObject.put(OBSTACLES_V, jsonArrayV);
                jsonObject.put(OBSTACLES_P, jsonArrayP);
            }

            jsonObject.put(TIME_STAMP, timeStamp);
        }
        catch (JSONException json)
        {
            json.printStackTrace();
            jsonObject = null;
        }

        Log.d("GameUpdate", jsonObject.toString());
        return jsonObject;
    }
}
