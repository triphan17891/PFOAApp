package edu.flinders.crcapp.presenter.impl;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import edu.flinders.crcapp.presenter.MainPresenter;
import edu.flinders.crcapp.view.MainView;
import edu.flinders.crcapp.interactor.MainInteractor;

import javax.inject.Inject;

public final class MainPresenterImpl extends BasePresenterImpl<MainView> implements MainPresenter {
    /**
     * The interactor
     */
    @NonNull
    private final MainInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public MainPresenterImpl(@NonNull MainInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean viewCreated) {
        super.onStart(viewCreated);

        this.onViewAttached(mView);
        // Your code here. Your view is available using mView and will not be null until next onStop()
    }

    @Override
    public void onStop() {
        // Your code here, mView will be null after this method until next onStart()
        this.onViewDetached();
        super.onStop();
    }

    @Override
    public void onPresenterDestroyed() {
        /*
         * Your code here. After this method, your presenter (and view) will be completely destroyed
         * so make sure to cancel any HTTP call or database connection
         */

        super.onPresenterDestroyed();
    }

    @Override
    public void moveToOtherActivity(Activity activity, Class<?> actName, Bundle bundle) {
        mInteractor.moveToOtherActivity(activity, actName, bundle);
    }
}