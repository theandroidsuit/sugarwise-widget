package com.theandroidsuit.sugarwise;

import com.theandroidsuit.sugarwise.bean.Wisdom;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

/**
 *
 * THE ANDROID SUIT 2014
 * @author Virginia Hernandez
 * @version 1.0
 *
 */

public class SugarWiseWidgetProvider extends AppWidgetProvider{
	
    private static final String TAG = "SugarWiseWidgetProvider";
    
	public static String theme = "";
	public static int color = 0;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		Log.d(TAG, "onUpdate");

		// For each widget that needs an update, get the text that we should display:
        //   - Create a RemoteViews object for it
        //   - Set the text in the RemoteViews object
        //   - Tell the AppWidgetManager to show that views object for the widget.
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            getNewWisdomCrystal(context, appWidgetManager, appWidgetId);
        }

	}

	public void onDeleted(Context context, int[] appWidgetIds) {
        Log.d(TAG, "onDeleted");
        // When the user deletes the widget, delete the preference associated with it.
        
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            SugarWiseConfigure.deleteTitlePref(context, appWidgetIds[i]);
        }
    }

	
    public static void getNewWisdomCrystal(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
    	Log.d(TAG, "getNewWisdomCrystal");
    	Log.d(TAG + "." + appWidgetId, String.valueOf(appWidgetId));
    	
		Intent intent = new Intent();
		intent.setAction(SugarWiseUtils.WIDGET_UPDATE_ACTION);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,  appWidgetId);
				
    	PendingIntent pending = PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		// re-registering for click listener
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_sugarwise);
			
		getConfiguration(context, appWidgetId);
		setNewWisdomSugar(context, appWidgetId, theme, color, remoteViews);
		
		remoteViews.setOnClickPendingIntent(R.id.contentContainer, pending);
		
		
		/* button to share */
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        
		Wisdom wisdom = SugarWiseUtils.getCurrentWisdomCrystal();
		
	    sharingIntent.setType("text/plain");
	    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, wisdom.getWisdomTitleToShare());
	    sharingIntent.putExtra(Intent.EXTRA_TEXT, wisdom.getWisdomSentence() + " -- " + wisdom.getWisdomAuthor());
	    
	    
	    PendingIntent sharePending = PendingIntent.getActivity(context, 
	    		appWidgetId, 
	    		Intent.createChooser(sharingIntent, context.getString(R.string.shareSugarCrystal)), 
	    		PendingIntent.FLAG_UPDATE_CURRENT);
	    
	    remoteViews.setOnClickPendingIntent(R.id.buttonShare, sharePending);
	   		
		
    	
		appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
	}
    
    
	public static void getConfiguration(Context context, int widgetID) {
		Log.d(TAG, "getConfiguration");
		
		theme = SugarWiseConfigure.loadTitlePref(context, widgetID, "theme");
		color = SugarWiseConfigure.loadTitlePrefInt(context, widgetID, "color");
		
        Log.d(TAG + "theme", theme);
        Log.d(TAG + "color", String.valueOf(color));
	}
	
	public static void setNewWisdomSugar(Context context, int widgetID, String theme, int color, RemoteViews remoteViews) {
		Log.d(TAG, "setNewWisdomSugar");
		
		SugarWiseUtils utils = new SugarWiseUtils(context, theme, color);
		
		Wisdom wisdom = utils.getCurrentWisdomCrystal();
		
		String title =  wisdom.getWisdomTitle();
		String desc = wisdom.getWisdomSentence();
		String author = wisdom.getWisdomAuthor();
		
		Log.d(TAG + ".title", title);
		Log.d(TAG + ".desc", desc);
		Log.d(TAG + ".author", author);
		
		remoteViews.setTextViewText(R.id.title,title);
		remoteViews.setTextViewText(R.id.desc, desc);
		remoteViews.setTextViewText(R.id.author, author);
		
		remoteViews.setTextColor(R.id.title, color);
		remoteViews.setTextColor(R.id.desc, color);
		remoteViews.setTextColor(R.id.author, color);

	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		
		Log.d(TAG, "onReceive");
		int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;

		Bundle extras = intent.getExtras();
		if (extras != null) {
		    widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 
		            AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		
		if (intent.getAction().equals(SugarWiseUtils.WIDGET_UPDATE_ACTION) && 
			AppWidgetManager.INVALID_APPWIDGET_ID != widgetID) {
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
    		getNewWisdomCrystal(context, manager, widgetID);
		}
	}

}
