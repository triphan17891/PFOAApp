package edu.flinders.crcapp;

import android.app.Application;
import android.support.annotation.NonNull;

import edu.flinders.crcapp.injection.AppComponent;
import edu.flinders.crcapp.injection.AppModule;
import edu.flinders.crcapp.injection.DaggerAppComponent;

public final class CRCApplication extends Application {
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    @NonNull
    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}