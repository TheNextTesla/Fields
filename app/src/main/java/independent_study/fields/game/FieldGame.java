package independent_study.fields.game;

import independent_study.fields.framework.AndroidGame;
import independent_study.fields.framework.Screen;

/**
 * Created by Blaine Huey on 11/6/2017.
 */

public class FieldGame extends AndroidGame
{
    public Screen getInitScreen()
    {
        return new TitleScreen(this);
    }
}
