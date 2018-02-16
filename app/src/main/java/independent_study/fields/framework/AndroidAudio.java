package independent_study.fields.framework;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;

/**
 * Derived from KiloBolt Apache-Licensed Code
 */
public class AndroidAudio
{
    AssetManager assets;
    SoundPool soundPool;

    public AndroidAudio(Activity activity)
    {
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        this.assets = activity.getAssets();
        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
    }

    public AndroidMusic createMusic(String filename)
    {
        try
        {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            return new AndroidMusic(assetDescriptor);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Couldn't load music '" + filename + "'");
        }
    }

    public AndroidSound createSound(String filename)
    {
        try
        {
            AssetFileDescriptor assetDescriptor = assets.openFd(filename);
            int soundId = soundPool.load(assetDescriptor, 0);
            return new AndroidSound(soundPool, soundId);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Couldn't load sound '" + filename + "'");
        }
    }
}
