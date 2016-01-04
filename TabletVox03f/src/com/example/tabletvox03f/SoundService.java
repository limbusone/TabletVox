package com.example.tabletvox03f;

import java.io.File;
import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;

public class SoundService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener
{
    private MediaPlayer mMediaPlayer = null;
    private Bundle extras;
	
    public int onStartCommand(Intent intent, int flags, int startId) 
    {
    	try
    	{
    		extras = intent.getExtras();
    		
            mMediaPlayer = new MediaPlayer(); // initialize it here
            //Uri path = Uri.parse("android.resource://com.example.tabletvox01/" + extras.getInt("id_som"));
            //Uri path = Uri.parse("file:///android_asset/sons/" + extras.getString("titulo_som") + ".wav");
            //String path = "file:///android_asset/sons/" + extras.getString("titulo_som") + ".wav";

            // aqui recupera-se o som em si
			// o som está sendo recuperado de internal storage
			// ou da pasta assets/sons
            
			File f_from_internal_storage = new File(this.getDir("sons", Context.MODE_PRIVATE).getPath() + "/" +  extras.getString("titulo_som") + ".wav");
			
			if (f_from_internal_storage.exists())
				mMediaPlayer.setDataSource(f_from_internal_storage.getAbsolutePath());
			else
			{

				AssetFileDescriptor afd = getAssets().openFd("sons/" + extras.getString("titulo_som") + ".wav");
				mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
			}            
            
            
            mMediaPlayer.setOnPreparedListener(SoundService.this);
            mMediaPlayer.prepareAsync(); // prepare async to not block main thread
    	} 
    	catch (IOException e)
    	{
    		
    	}
        
        return super.onStartCommand(intent, flags, startId);
    }

    /** Called when MediaPlayer is ready */
    public void onPrepared(MediaPlayer player) 
    {
        player.start();
        /*
        if (extras.getBoolean("delayed"))
        {
        	try
			{
				Thread.sleep(1500);
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        */
    }
    
    /** Called when MediaPlayer is done */
	@Override
	public void onCompletion(MediaPlayer player)
	{
		stopSelf();
	}    

	@Override
	public IBinder onBind(Intent arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (mMediaPlayer != null)
		{
			mMediaPlayer.release();
			mMediaPlayer = null;
		}
	}

}

