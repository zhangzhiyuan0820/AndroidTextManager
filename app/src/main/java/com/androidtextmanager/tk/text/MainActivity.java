package com.androidtextmanager.tk.text;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.androidtextmanager.tk.text.config.Config;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Config.getInstanceSp(this , "text").getEditor();


    }



}
