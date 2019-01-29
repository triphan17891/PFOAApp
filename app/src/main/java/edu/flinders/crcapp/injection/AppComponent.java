package edu.flinders.crcapp.injection;

import android.content.Context;

import edu.flinders.crcapp.CRCApplication;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    Context getAppContext();

    CRCApplication getApp();
}