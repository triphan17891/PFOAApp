/*
 *  Created by Tri Phan on 7/01/19 8:30 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:30 PM
 */

package edu.flinders.crcapp.injection;

import android.support.annotation.NonNull;

import edu.flinders.crcapp.interactor.CalibrationInteractor;
import edu.flinders.crcapp.interactor.impl.CalibrationInteractorImpl;
import edu.flinders.crcapp.presenter.loader.PresenterFactory;
import edu.flinders.crcapp.presenter.CalibrationPresenter;
import edu.flinders.crcapp.presenter.impl.CalibrationPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class CalibrationViewModule {
    @Provides
    public CalibrationInteractor provideInteractor() {
        return new CalibrationInteractorImpl();
    }

    @Provides
    public PresenterFactory<CalibrationPresenter> providePresenterFactory(@NonNull final CalibrationInteractor interactor) {
        return new PresenterFactory<CalibrationPresenter>() {
            @NonNull
            @Override
            public CalibrationPresenter create() {
                return new CalibrationPresenterImpl(interactor);
            }
        };
    }
}
