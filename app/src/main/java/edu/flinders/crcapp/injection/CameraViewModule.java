/*
 *  Created by Tri Phan on 7/01/19 10:22 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 10:22 PM
 */

package edu.flinders.crcapp.injection;

import android.content.Context;
import android.support.annotation.NonNull;

import android.view.TextureView;
import edu.flinders.crcapp.interactor.CameraInteractor;
import edu.flinders.crcapp.interactor.impl.CameraInteractorImpl;
import edu.flinders.crcapp.presenter.loader.PresenterFactory;
import edu.flinders.crcapp.presenter.CameraPresenter;
import edu.flinders.crcapp.presenter.impl.CameraPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class CameraViewModule {
    @Provides
    public CameraInteractor provideInteractor() {
        return new CameraInteractorImpl();
    }

    @Provides
    public PresenterFactory<CameraPresenter> providePresenterFactory(@NonNull final CameraInteractor interactor) {
        return new PresenterFactory<CameraPresenter>() {
            @NonNull
            @Override
            public CameraPresenter create() {
                return new CameraPresenterImpl(interactor);
            }
        };
    }
}
