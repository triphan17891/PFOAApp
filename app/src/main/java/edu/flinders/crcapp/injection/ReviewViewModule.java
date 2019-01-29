/*
 *  Created by Tri Phan on 7/01/19 8:35 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:35 PM
 */

package edu.flinders.crcapp.injection;

import android.support.annotation.NonNull;

import edu.flinders.crcapp.interactor.ReviewInteractor;
import edu.flinders.crcapp.interactor.impl.ReviewInteractorImpl;
import edu.flinders.crcapp.presenter.loader.PresenterFactory;
import edu.flinders.crcapp.presenter.ReviewPresenter;
import edu.flinders.crcapp.presenter.impl.ReviewPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class ReviewViewModule {
    @Provides
    public ReviewInteractor provideInteractor() {
        return new ReviewInteractorImpl();
    }

    @Provides
    public PresenterFactory<ReviewPresenter> providePresenterFactory(@NonNull final ReviewInteractor interactor) {
        return new PresenterFactory<ReviewPresenter>() {
            @NonNull
            @Override
            public ReviewPresenter create() {
                return new ReviewPresenterImpl(interactor);
            }
        };
    }
}
