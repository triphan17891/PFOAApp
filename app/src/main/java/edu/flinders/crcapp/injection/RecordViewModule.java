/*
 *  Created by Tri Phan on 7/01/19 8:33 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:33 PM
 */

package edu.flinders.crcapp.injection;

import android.support.annotation.NonNull;

import edu.flinders.crcapp.interactor.RecordInteractor;
import edu.flinders.crcapp.interactor.impl.RecordInteractorImpl;
import edu.flinders.crcapp.presenter.loader.PresenterFactory;
import edu.flinders.crcapp.presenter.RecordPresenter;
import edu.flinders.crcapp.presenter.impl.RecordPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class RecordViewModule {
    @Provides
    public RecordInteractor provideInteractor() {
        return new RecordInteractorImpl();
    }

    @Provides
    public PresenterFactory<RecordPresenter> providePresenterFactory(@NonNull final RecordInteractor interactor) {
        return new PresenterFactory<RecordPresenter>() {
            @NonNull
            @Override
            public RecordPresenter create() {
                return new RecordPresenterImpl(interactor);
            }
        };
    }
}
