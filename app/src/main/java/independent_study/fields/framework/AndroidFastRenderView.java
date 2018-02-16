package independent_study.fields.framework;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Derived from KiloBolt Apache-Licensed Code
 */
public class AndroidFastRenderView extends SurfaceView implements Runnable
{
    Activity game;
    Bitmap framebuffer;
    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;
    
    public AndroidFastRenderView(Activity game, Bitmap framebuffer)
    {
        super(game);
        this.game = game;
        this.framebuffer = framebuffer;
        this.holder = getHolder();
    }

    public void resume()
    {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }      
    
    public void run()
    {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();
        while(running)
        {
            if(!holder.getSurface().isValid())
                continue;

            float deltaTime = (System.nanoTime() - startTime) / 10000000.000f;
            startTime = System.nanoTime();
            
            if (deltaTime > 3.15)
            {
                deltaTime = (float) 3.15;
            }

            if(game instanceof Game)
            {
                ((Game) game).getCurrentScreen().update(deltaTime);
                ((Game) game).getCurrentScreen().paint(deltaTime);
            }
            
            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(framebuffer, null, dstRect, null);                           
            holder.unlockCanvasAndPost(canvas);
            System.gc();
        }
    }

    public void pause()
    {
        running = false;                        
        while(true)
        {
            try
            {
                renderThread.join();
                break;
            }
            catch (InterruptedException e)
            {
                //TODO: Retry Operations?
            }
        }
    }
}