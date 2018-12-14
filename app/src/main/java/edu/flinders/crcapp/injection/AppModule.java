package edu.flinders.crcapp.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import edu.flinders.crcapp.CRC;

import dagger.Module;
import dagger.Provides;

@Module
public final class AppModule {
    @NonNull
    private final CRC mApp;

    public AppModule(@NonNull CRC app) {
        mApp = app;
    }

    @Provides
    public Context provideAppContext() {
        return mApp;
    }

    @Provides
    public CRC provideApp() {
        return mApp;
    }
}
