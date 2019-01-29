/*
 *  Created by Tri Phan on 7/01/19 8:35 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:35 PM
 */

package edu.flinders.crcapp.presenter.impl;

import android.support.annotation.NonNull;

import edu.flinders.crcapp.presenter.ReviewPresenter;
import edu.flinders.crcapp.view.ReviewView;
import edu.flinders.crcapp.interactor.ReviewInteractor;

import javax.inject.Inject;

public final class ReviewPresenterImpl extends BasePresenterImpl<ReviewView> implements ReviewPresenter {
    /**
     * The interactor
     */
    @NonNull
    private final ReviewInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public ReviewPresenterImpl(@NonNull ReviewInteractor interactor) {
        mInteractor = interactor;
    }

    @Override
    public void onStart(boolean viewCreated) {
        super.onStart(viewCreated);

        // Your code here. Your view is available using mView and will not be null until next onStop()
    }

    @Override
    public void onStop() {
        // Your code here, mView will be null after this method until next onStart()

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
}