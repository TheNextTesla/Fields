package independent_study.fields.framework;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SingleTouchHandler implements TouchHandler
{
    boolean isTouched;
    int touchX;
    int touchY;
    Pool<AndroidInput.TouchEvent> touchEventPool;
    List<AndroidInput.TouchEvent> touchEvents = new ArrayList<>();
    List<AndroidInput.TouchEvent> touchEventsBuffer = new ArrayList<>();
    float scaleX;
    float scaleY;
    
    public SingleTouchHandler(View view, float scaleX, float scaleY)
    {
        Pool.PoolObjectFactory<AndroidInput.TouchEvent> factory = new Pool.PoolObjectFactory<AndroidInput.TouchEvent>()
        {
            @Override
            public AndroidInput.TouchEvent createObject()
            {
                return new AndroidInput.TouchEvent();
            }            
        };
        touchEventPool = new Pool<AndroidInput.TouchEvent>(factory, 100);
        view.setOnTouchListener(this);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized(this)
        {
            AndroidInput.TouchEvent touchEvent = touchEventPool.newObject();
            switch (event.getAction())
            {
            case MotionEvent.ACTION_DOWN:
                touchEvent.type = AndroidInput.TouchEvent.TOUCH_DOWN;
                isTouched = true;
                break;
            case MotionEvent.ACTION_MOVE:
                touchEvent.type = AndroidInput.TouchEvent.TOUCH_DRAGGED;
                isTouched = true;
                break;
            case MotionEvent.ACTION_CANCEL:                
            case MotionEvent.ACTION_UP:
                touchEvent.type = AndroidInput.TouchEvent.TOUCH_UP;
                isTouched = false;
                break;
            }
            
            touchEvent.x = touchX = (int)(event.getX() * scaleX);
            touchEvent.y = touchY = (int)(event.getY() * scaleY);
            touchEventsBuffer.add(touchEvent);                        
            
            return true;
        }
    }

    @Override
    public boolean isTouchDown(int pointer)
    {
        synchronized(this)
        {
            if(pointer == 0)
                return isTouched;
            else
                return false;
        }
    }

    @Override
    public int getTouchX(int pointer)
    {
        synchronized(this)
        {
            return touchX;
        }
    }

    @Override
    public int getTouchY(int pointer)
    {
        synchronized(this)
        {
            return touchY;
        }
    }

    @Override
    public List<AndroidInput.TouchEvent> getTouchEvents()
    {
        synchronized(this)
        {
            int len = touchEvents.size();
            for( int i = 0; i < len; i++ )
                touchEventPool.free(touchEvents.get(i));
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }
}
