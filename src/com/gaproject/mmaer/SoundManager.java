package com.gaproject.mmaer;

import java.util.HashMap;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {

	private static  SoundPool mSoundPool;
	private static  HashMap<Integer, Integer> mSoundPoolMap;
	private static  AudioManager  mAudioManager;
	private  Context mContext;
	static boolean isSoundPlaying=false;
	
	public void initSounds(Context context) {
	    mContext = context;
	    mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
	    mSoundPoolMap = new HashMap<Integer, Integer>();
	    mAudioManager = (AudioManager)mContext.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public void addSound(int index, int SoundID)
	{
	    mSoundPoolMap.put(index, mSoundPool.load(mContext, SoundID, 1));
	}
	
	public void playLoopedSound(int index)
	{
		if(!isSoundPlaying)
		{
	      float streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
	      streamVolume = streamVolume / mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
	      mSoundPool.play((Integer) mSoundPoolMap.get(index), streamVolume, streamVolume, 1,-1, 1f);
	      isSoundPlaying = true;
		}
	
	  
	}
	
	public static void stopSound(int index)
	{
		mSoundPool.stop((Integer) mSoundPoolMap.get(index));
	}
 
	/**
	 * Deallocates the resources and Instance of SoundManager
	 */
	public static void cleanup()
	{
		mSoundPool.release();
		mSoundPool = null;
	    mSoundPoolMap.clear();
	    mAudioManager.unloadSoundEffects();
	    isSoundPlaying=false;
	}
	
}
