package com.dev.kaizen.util;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

public class FontUtils {
	
	 // font file name
    public static final String FONT_CUSTOM = "Poppins-Regular.ttf";
    public static final String FONT_CUSTOM_SEMIBOLD = "Poppins-SemiBold.ttf";
    public static final String FONT_CUSTOM_BOLD = "Poppins-Bold.ttf";

    // store the opened typefaces(fonts)
    private static final Hashtable<String, Typeface> mCache = new Hashtable<String, Typeface>();
    
    public static Typeface loadFontFromAssets(Context context) {
   	 
    	String fontName = FONT_CUSTOM;
        // make sure we load each font only once
        synchronized (mCache) {
 
            if (! mCache.containsKey(fontName)) {
 
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
 
                mCache.put(fontName, typeface);
            }
 
            return mCache.get(fontName);
 
        }
 
    }
    
    public static Typeface loadFontFromAssets(Context context, int styleType) {
    	 
    	String fontName = FONT_CUSTOM;
    	if(Constant.FONT_BOLD == styleType){
    		fontName = FONT_CUSTOM_BOLD;
    	}
        if(Constant.FONT_SEMIBOLD == styleType){
            fontName = FONT_CUSTOM_SEMIBOLD;
        }
        // make sure we load each font only once
        synchronized (mCache) {
 
            if (! mCache.containsKey(fontName)) {
 
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontName);
 
                mCache.put(fontName, typeface);
            }
 
            return mCache.get(fontName);
 
        }
 
    }

}
