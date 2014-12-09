package com.theandroidsuit.sugarwise;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
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

public class SugarWiseIntentReceiver extends BroadcastReceiver{
	
    private static final String TAG = "SugarWiseIntentReceiver";


	public static String theme = "";
	public static int color = 0;
	public static int widgetID;
		
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "onReceive");

		Bundle extras = intent.getExtras();
		if (extras != null) {
		    widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 
		            AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		

		if (intent.getAction().equals(SugarWiseUtils.WIDGET_UPDATE_ACTION)) {
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
    		SugarWiseWidgetProvider.getNewWisdomCrystal(context, manager, widgetID);
    		
			//updateWidgetPictureAndButtonListener(context);
			//AppWidgetManager manager = AppWidgetManager.getInstance(context);
            //SugarWiseWidgetProvider.updateAppWidget(context, manager ,widgetID);

		}
	}

	private void updateWidgetPictureAndButtonListener(Context context) {
		Log.d(TAG, "updateWidgetPictureAndButtonListener");
		
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.widget_sugarwise);
		
		// updating view
		
		SugarWiseUtils utils = new SugarWiseUtils(context, theme, color);

		
		SugarWiseWidgetProvider.getConfiguration(context, widgetID);
		SugarWiseWidgetProvider.setNewWisdomSugar(context, widgetID, theme, color, remoteViews);
		
		Intent intent = new Intent();
		intent.setAction(SugarWiseUtils.WIDGET_UPDATE_ACTION);
		
		PendingIntent pending = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		// re-registering for click listener
		remoteViews.setOnClickPendingIntent(R.id.contentContainer, pending);
		
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(widgetID, remoteViews);
	}

	
}
