package edu.flinders.crcapp.interactor.impl;

import javax.inject.Inject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import edu.flinders.crcapp.interactor.MainInteractor;

public final class MainInteractorImpl implements MainInteractor {
    @Inject
    public MainInteractorImpl() {

    }

    @Override
    public void moveToOtherActivity(Activity activity, Class<?> actName, Bundle bundle) {
        Intent i = new Intent(activity.getApplicationContext(), actName);
        if(bundle != null) {
            i.putExtras(bundle);
        }
        activity.startActivity(i);
    }
}