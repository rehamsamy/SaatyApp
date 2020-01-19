package com.saaty.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {

    private static final String APP_Language = "APP_Language";


    // properties
//    private static final String SOME_STRING_VALUE = "SOME_STRING_VALUE";
    public static final String select_lang = "Language";
    // other properties...

    private PreferenceHelper() {
    }

    public static final SharedPreferences getSharedPreference(Context context){
        return  context.getSharedPreferences(APP_Language,Context.MODE_PRIVATE);
    }


    public static String getValue(Context context){
        return getSharedPreference(context).getString(select_lang,"ar");
    }

    public static  void setValue(Context context,String newValue){
        SharedPreferences.Editor editor=getSharedPreference(context).edit();
        editor.putString(select_lang,newValue);
        editor.commit();
    }



}
