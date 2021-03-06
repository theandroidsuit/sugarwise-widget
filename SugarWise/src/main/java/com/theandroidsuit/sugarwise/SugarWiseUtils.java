package com.theandroidsuit.sugarwise;

import java.util.List;
import java.util.Random;

import android.content.Context;

import com.theandroidsuit.sugarwise.bean.Wisdom;
import com.theandroidsuit.sugarwise.util.SugarWiseJSONParser;

/**
 *
 * THE ANDROID SUIT 2014
 * @author Virginia Hernandez
 * @version 1.0
 *
 */

public class SugarWiseUtils {
    private static final String TAG = "SugarWiseUtils";

	
	static final String WIDGET_UPDATE_ACTION = "com.theandroidsuit.intent.action.UPDATE_WIDGET";
	
	public static final String MODE_ASSET = "asset";
	public static final String MODE_SERVICE = "service";
	private  List<Wisdom> listWisdom = null;
	private static Wisdom wisdom = null;
	
	private Context context;
	private String theme;
	private int color;
	
	public SugarWiseUtils( Context context, String theme, int color) {
		this.context = context;
		this.theme = theme;
		this.color = color;
		getNewWisdomCrystal();
	}

	private  void getNewWisdomCrystal() {
		if ((null != theme && !"".equals(theme) && null != context) ||  null != listWisdom){

			if (null == listWisdom){
				SugarWiseJSONParser jParser = new SugarWiseJSONParser();
				listWisdom = jParser.getDatafromAsset(context, theme);
			}
			
			Random rnd = new Random(System.currentTimeMillis());
			int randomWisdomLocation = rnd.nextInt(listWisdom.size());
			
			wisdom = listWisdom.get(randomWisdomLocation);
			
			
		}else{
			wisdom = new Wisdom();
		}
	}
	
	public static Wisdom getCurrentWisdomCrystal() {
		return wisdom;
	}
}
