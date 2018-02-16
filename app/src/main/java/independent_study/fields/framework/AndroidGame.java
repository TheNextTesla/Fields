package independent_study.fields.framework;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.Window;
import android.view.WindowManager;

/**
 * Derived from KiloBolt Apache-Licensed Code
 */
public abstract class AndroidGame extends Activity implements Game
{
    protected AndroidFastRenderView renderView;
    protected AndroidGraphics graphics;
    protected AndroidAudio audio;
    protected AndroidInput input;
    protected Screen screen;
    protected WakeLock wakeLock;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Bitmap frameBuffer = Bitmap.createBitmap(independent_study.fields.game.Configuration.GAME_WIDTH,
                independent_study.fields.game.Configuration.GAME_HEIGHT, Config.RGB_565);
        
        float scaleX = (float) independent_study.fields.game.Configuration.GAME_WIDTH
                / getWindowManager().getDefaultDisplay().getWidth();
        float scaleY = (float) independent_study.fields.game.Configuration.GAME_HEIGHT
                / getWindowManager().getDefaultDisplay().getHeight();

        renderView = new AndroidFastRenderView(this, frameBuffer);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        screen = getInitScreen();
        setContentView(renderView);
        
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "MyGame");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        wakeLock.acquire();
        screen.resume();
        renderView.resume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        wakeLock.release();
        renderView.pause();
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    public AndroidInput getInput()
    {
        return input;
    }

    /*
    public FileIO getFileIO()
    {
        return fileIO;
    }
    */

    public AndroidGraphics getGraphics()
    {
        return graphics;
    }

    public AndroidAudio getAudio()
    {
        return audio;
    }

    public void setScreen(Screen screen)
    {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }
    
    public Screen getCurrentScreen()
    {
    	return screen;
    }

    public abstract Screen getInitScreen();

    public Activity getActivity()
    {
        return this;
    }
}