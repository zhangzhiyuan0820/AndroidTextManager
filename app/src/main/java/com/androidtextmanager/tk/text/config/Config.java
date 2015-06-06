package com.androidtextmanager.tk.text.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by TK on 2015/6/5.
 */
public class Config {

    private SharedPreferences sp = null;

    private SharedPreferences.Editor editor = null;

    private static volatile Config   mInstance ;

    public static Config getInstanceSp(Context context, String configFileName) {
        if( mInstance == null ) {
            synchronized( Config.class ) {
                if( mInstance == null ) {
                    mInstance = new Config( context, configFileName );
                }
            }
        }
        return mInstance;
    }

    protected Config( Context context, String configFileName ) {
        sp = context.getApplicationContext().getSharedPreferences( configFileName, Context.MODE_PRIVATE );
    }

    public SharedPreferences.Editor getEditor() {
        return sp.edit();
    }

    public void destroy() {
        sp = null;
        mInstance = null;
    }

}
