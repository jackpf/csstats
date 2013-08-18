package com.jackpf.csstats;

import java.io.InputStream;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.jackpf.csstats.Steam.SteamStats;

public class UI
{
	/**
	 * Update UI
	 * TODO: This may need splitting up if/when it gets bigger
	 * 
	 * @param stats
	 */
	public void update(SteamStats profile, SteamStats stats)
	{
		Activity context = MainActivity.getInstance();
		
		if (Integer.parseInt(stats.get("visibilityState")) != SteamStats.VIEWABLE) {
			Lib.error(
				context,
				context.getString(R.string.error_not_viewable)
			);
		}
		
		// Set avatar
		new ImageLoader(
			(ImageView) context.findViewById(R.id.avatar)
		).execute(
			profile.get("avatarFull")
		);
		
		// Set username
		((TextView) context.findViewById(R.id.steamId)).setText(profile.get("steamID"));
		
		TabHost tabHost = (TabHost) context.findViewById(R.id.tabhost);
		tabHost.setup();
		tabHost.setVisibility(LinearLayout.VISIBLE);

        tabHost.addTab(
    		tabHost.newTabSpec("Summary")
	            .setIndicator("Summary")
	            .setContent(R.id.fragment_summary)
        );
        tabHost.addTab(
    		tabHost.newTabSpec("Last Game")
	            .setIndicator("Last Game")
	            .setContent(R.id.fragment_last_game)
        );
        tabHost.addTab(
    		tabHost.newTabSpec("Lifetime")
	            .setIndicator("Overview")
	            .setContent(R.id.fragment_lifetime)
        );
 
        tabHost.setCurrentTab(0);
        
        LinearLayout fragmentSummary = (LinearLayout) context.findViewById(R.id.fragment_summary);
        
        
	}
	
	public void error(Exception e)
	{
		Lib.error(MainActivity.getInstance(), e.getMessage());
	}
	
	/**
	 * Load images from a url
	 * Copied from halo4servicerecord
	 * TODO: Caching?
	 */
	private static class ImageLoader extends AsyncTask<String, Void, Bitmap> {
	    ImageView bmImage;

	    public ImageLoader(ImageView bmImage) {
	        this.bmImage = bmImage;
	    }

	    protected Bitmap doInBackground(String... urls) {
	        String urldisplay = urls[0];
	        Bitmap mIcon11 = null;
	        try {
	            InputStream in = new java.net.URL(urldisplay).openStream();
	            mIcon11 = BitmapFactory.decodeStream(in);
	        } catch (Exception e) {
	            Log.e("Error", e.getMessage());
	            e.printStackTrace();
	        }
	        return mIcon11;
	    }

	    protected void onPostExecute(Bitmap result) {
	        bmImage.setImageBitmap(result);
	    }
	}
}