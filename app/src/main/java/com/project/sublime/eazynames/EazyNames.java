package com.project.sublime.eazynames;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.project.sublime.eazynames.utils.Timber;

/**
 * Created by goonerDroid on 17-04-2016.
 */
public class EazyNames extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            //Initializing Stetho
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(
                                    Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(
                                    Stetho.defaultInspectorModulesProvider(this))
                            .build());
            //Initializing Timber
            Timber.plant(new Timber.DebugTree());
        }
    }
}
