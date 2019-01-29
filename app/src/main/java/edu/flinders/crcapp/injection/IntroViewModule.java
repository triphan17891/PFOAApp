/*
 *  Created by Tri Phan on 7/01/19 8:42 AM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:42 AM
 */

package edu.flinders.crcapp.injection;

import android.support.annotation.NonNull;

import edu.flinders.crcapp.interactor.IntroInteractor;
import edu.flinders.crcapp.interactor.impl.IntroInteractorImpl;
import edu.flinders.crcapp.presenter.loader.PresenterFactory;
import edu.flinders.crcapp.presenter.IntroPresenter;
import edu.flinders.crcapp.presenter.impl.IntroPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class IntroViewModule {
    @Provides
    public IntroInteractor provideInteractor() {
        return new IntroInteractorImpl();
    }

    @Provides
    public PresenterFactory<IntroPresenter> providePresenterFactory(@NonNull final IntroInteractor interactor) {
        return new PresenterFactory<IntroPresenter>() {
            @NonNull
            @Override
            public IntroPresenter create() {
                return new IntroPresenterImpl(interactor);
            }
        };
    }
}
