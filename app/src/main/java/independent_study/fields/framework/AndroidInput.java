package independent_study.fields.framework;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.view.View;

import java.util.List;

/**
 * Derived from KiloBolt Apache-Licensed Code
 */
public class AndroidInput
{
    TouchHandler touchHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        if(Integer.parseInt(VERSION.SDK) < 5) 
            touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
        else
            touchHandler = new MultiTouchHandler(view, scaleX, scaleY);        
    }

    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    public boolean inRectBounds(TouchEvent event, Rect rect) {
        return inBounds(event, rect.left, rect.top, rect.width(), rect.height());
    }

    public boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
        if (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1)
            return true;
        else
            return false;
    }

    public List<AndroidInput.TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }

    public static class TouchEvent {
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_UP = 1;
        public static final int TOUCH_DRAGGED = 2;
        public static final int TOUCH_HOLD = 3;

        public int type;
        public int x, y;
        public int pointer;


    }
}
