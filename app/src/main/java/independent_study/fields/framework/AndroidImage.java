package independent_study.fields.framework;

import android.graphics.Bitmap;

public class AndroidImage
{
    Bitmap bitmap;
    AndroidGraphics.ImageFormat format;
    
    public AndroidImage(Bitmap bitmap, AndroidGraphics.ImageFormat format)
    {
        this.bitmap = bitmap;
        this.format = format;
    }

    public int getWidth()
    {
        return bitmap.getWidth();
    }

    public int getHeight()
    {
        return bitmap.getHeight();
    }

    public AndroidGraphics.ImageFormat getFormat()
    {
        return format;
    }

    public void dispose()
    {
        bitmap.recycle();
    }      
}
