package edu.flinders.crcapp;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import edu.flinders.crcapp.injection.AppComponent;
import edu.flinders.crcapp.injection.AppModule;
import edu.flinders.crcapp.injection.DaggerAppComponent;

public final class CRCApplication extends Application {
    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    @NonNull
    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}