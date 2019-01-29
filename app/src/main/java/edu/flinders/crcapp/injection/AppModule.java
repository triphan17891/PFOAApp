package edu.flinders.crcapp.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import edu.flinders.crcapp.CRCApplication;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {
    @NonNull
    private final CRCApplication mApp;

    public AppModule(@NonNull CRCApplication app) {
        mApp = app;
    }

    @Provides
    public Context provideAppContext() {
        return mApp;
    }

    @Provides
    public CRCApplication provideApp() {
        return mApp;
    }
}
