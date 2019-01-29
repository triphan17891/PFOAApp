package edu.flinders.crcapp.view;

import android.support.annotation.UiThread;
import android.view.View;

@UiThread
public interface MainView {
    void moveToOtherActivity(View view);
}