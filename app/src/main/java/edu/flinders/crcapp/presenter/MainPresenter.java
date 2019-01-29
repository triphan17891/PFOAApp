package edu.flinders.crcapp.presenter;

import android.app.Activity;
import android.os.Bundle;
import edu.flinders.crcapp.view.MainView;

public interface MainPresenter extends BasePresenter<MainView> {
    void moveToOtherActivity(Activity activity, Class<?> actName, Bundle bundle);
}