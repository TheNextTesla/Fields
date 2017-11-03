package independent_study.fields.sprites;

/**
 * Created by Blaine Huey on 11/2/2017.
 */

public class WallSprite extends Sprite
{
    public WallSprite(int left, int top, int right, int bottom)
    {
        super(left, top, right, bottom);
    }

    @Override
    public void update()
    {

    }

    @Override
    public boolean isTouching(Sprite other)
    {
        return false;
    }

    @Override
    public void touched(Sprite other)
    {
        if(other instanceof WallSprite)
        {
            destroy();
        }
        else if(other instanceof ObstacleSprite)
        {
            destroy();
        }
    }

    @Override
    public void destroy()
    {

    }
}
