package com.saaty.util;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.multidex.MultiDex;

import java.util.Locale;



/*
created by mahmoud 10/4/2017
 */

public class AppController extends Application {

        @Override
        protected void attachBaseContext(Context base) {
            super.attachBaseContext(LocaleManager.setLocale(base));
        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            LocaleManager.setLocale(this);
        }
    }