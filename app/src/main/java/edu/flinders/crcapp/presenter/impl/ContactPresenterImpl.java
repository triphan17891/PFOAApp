/*
 *  Created by Tri Phan on 7/01/19 8:36 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:36 PM
 */

package edu.flinders.crcapp.presenter.impl;

import android.support.annotation.NonNull;

import edu.flinders.crcapp.presenter.ContactPresenter;
import edu.flinders.crcapp.view.ContactView;
import edu.flinders.crcapp.interactor.ContactInteractor;

import javax.inject.Inject;

public final class ContactPresenterImpl extends BasePresenterImpl<ContactView> implements ContactPresenter {
    /**
     * The interactor
     */
    @NonNull
    private final ContactInteractor mInteractor;

    // The view is available using the mView variable

    @Inject
    public ContactPresenterImpl(@NonNull ContactInteractor interactor) {
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