package util;

import android.content.Context;
import android.content.SharedPreferences;

import constant.Constant;

/**
 * temp configuration
 * Created by lucas on 21/11/2016.
 */

public class Config {

    public static SharedPreferences getSharedPrefereneces() {
        return BaseApp.BaseApp().getSharedPreferences(Constant.PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPrefereneces().edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key) {
        SharedPreferences preference = getSharedPrefereneces();
        return preference.getString(key, "");
    }
}
