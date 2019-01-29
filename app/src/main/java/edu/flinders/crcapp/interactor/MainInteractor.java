package edu.flinders.crcapp.interactor;

import android.app.Activity;
import android.os.Bundle;

public interface MainInteractor extends BaseInteractor {
    void moveToOtherActivity(Activity activity, Class<?> actName, Bundle bundle);
}