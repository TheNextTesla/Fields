package independent_study.fields.framework;

import android.view.View.OnTouchListener;

import java.util.List;

/**
 * Derived from KiloBolt Apache-Licensed Code
 */
public interface TouchHandler extends OnTouchListener {
    public boolean isTouchDown(int pointer);
    
    public int getTouchX(int pointer);
    
    public int getTouchY(int pointer);
    
    public List<AndroidInput.TouchEvent> getTouchEvents();
}
