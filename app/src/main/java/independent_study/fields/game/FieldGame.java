package independent_study.fields.game;

import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.Screen;

/**
 * Created by Blaine Huey on 11/6/2017.
 */

public class FieldGame extends AndroidGame
{
    private int objectiveScore;
    private int timeScore;
    private Screen heldScreen;
    private InterstitialAd mInterstitialAd;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        objectiveScore = 0;
        timeScore = 0;

        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdLoaded()
            {

            }

            @Override
            public void onAdFailedToLoad(int errorCode)
            {
                FieldGame.super.setScreen(heldScreen);
            }

            @Override
            public void onAdOpened()
            {

            }

            @Override
            public void onAdLeftApplication()
            {

            }

            @Override
            public void onAdClosed()
            {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                FieldGame.super.setScreen(heldScreen);
            }
        });
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

    @Override
    public void setScreen(final Screen screen)
    {
        if(screen instanceof GameScreen && getCurrentScreen() instanceof TitleScreen)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if (heldScreen == null && mInterstitialAd.isLoaded())
                    {
                        heldScreen = screen;
                        mInterstitialAd.show();
                    }
                }
            });
        }
        else
        {
            heldScreen = null;
            super.setScreen(screen);
        }
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
