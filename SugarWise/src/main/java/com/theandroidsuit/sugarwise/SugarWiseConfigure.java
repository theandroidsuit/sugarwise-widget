package com.theandroidsuit.sugarwise;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

/**
 *
 * THE ANDROID SUIT 2014
 * @author Virginia Hernandez
 * @version 1.0
 *
 */

public class SugarWiseConfigure extends Activity implements OnItemSelectedListener{
	
    static final String TAG = "SugarWiseConfigure";

    private static final String PREFS_NAME = "com..SugarWiseWidgetProvider";
    private static final String PREF_PREFIX_KEY = "prefix_";

	private int widgetID;
	
	private String chosenLanguage;
	private String languageToUse;
	private int colorToUse;
	private String themeToUse;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(TAG, "onCreate");
		
		// Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if they press the back button.
		setResult(RESULT_CANCELED);

		// Set the view layout resource to use.
		setContentView(R.layout.sugarwise_configure);
		
	
		// Register Spinners
		registerSpinnerListener();
		
		// Find the widget id from the intent. 
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, 
		            AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		
        // If they gave us an intent without the widget id, just bail.
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        // Bind the action for the save button.
		findViewById(R.id.buttonConfigure).setOnClickListener(mOnClickListener);
		
	}
	
	

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = SugarWiseConfigure.this;

            // When the button is clicked, save the string in our prefs and return that they
            // clicked OK.
            
            Log.d(TAG, "OnClickListener");

            Log.d(TAG + "theme", themeToUse);
            Log.d(TAG + "color", String.valueOf(colorToUse));
            Log.d(TAG + "language", languageToUse);
            
            saveTitlePref(context, widgetID, "theme", themeToUse);
            saveTitlePref(context, widgetID, "color", colorToUse);
            saveTitlePref(context, widgetID, "language", languageToUse);
            
    		AppWidgetManager manager = AppWidgetManager.getInstance(context);
    		SugarWiseWidgetProvider.getNewWisdomCrystal(context, manager, widgetID);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };


	
    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String key, String value) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + "sugarwise" + appWidgetId + key, value);
        prefs.commit();
    }
    
    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String key, int value) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putInt(PREF_PREFIX_KEY + "sugarwise" +  appWidgetId + key, value);
        prefs.commit();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String value = prefs.getString(PREF_PREFIX_KEY + "sugarwise" + appWidgetId + key, null);
        if (value != null) {
            return value;
        } else {
            return "";
        }
    }
    
    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static int loadTitlePrefInt(Context context, int appWidgetId, String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        Integer value = prefs.getInt(PREF_PREFIX_KEY + "sugarwise" + appWidgetId + key, 0);
        if (value != 0) {
            return value;
        } else {
            return Color.parseColor("#000000");
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
    	
    }

	
	private void registerSpinnerListener() {
		Spinner language = (Spinner) findViewById(R.id.languageSpinner);
		Spinner color = (Spinner) findViewById(R.id.colorSpinner);
		Spinner theme = (Spinner) findViewById(R.id.themeSpinner);
		
		language.setOnItemSelectedListener(this);
		color.setOnItemSelectedListener(this);
		theme.setOnItemSelectedListener(this);
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

		int spinnerId = parent.getId();
		
		switch(spinnerId){
			case R.id.languageSpinner:
				setupLanguage((String) parent.getItemAtPosition(position));
				break;
			case R.id.themeSpinner:
				setupTheme((String) parent.getItemAtPosition(position));
				break;
			case R.id.colorSpinner:
				setupColor((String) parent.getItemAtPosition(position));
				break;
		}
				
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}

	

	private void setupColor(String item) {
		switch(item){
			case "Red":
				colorToUse = Color.parseColor("#FF0000");
				break;
			case "Pink":
				colorToUse = Color.parseColor("#FF5CD6");
				break;
			case "White":
				colorToUse = Color.parseColor("#FFFFFF");
				break;
			case "Yellow":
				colorToUse = Color.parseColor("#FFFF4D");
				break;
			case "Grey":
				colorToUse = Color.parseColor("#696969");
				break;
			case "Blue":
				colorToUse = Color.parseColor("#3333FF");
				break;
			case "Green":
				colorToUse = Color.parseColor("#2EB82E");
				break;
			case "Purple":
				colorToUse = Color.parseColor("#7A007A");
				break;
			default:
				colorToUse = Color.parseColor("#000000");
				break;
		};
	}

	private void setupLanguage(String item) {
		chosenLanguage = item;		
		languageToUse = chosenLanguage;
	}
	
	private void setupTheme(String item) {
		
		themeToUse = item.toLowerCase().replace(" ", "").trim();
		Log.d(TAG + ".theme!", themeToUse);
	}
}
