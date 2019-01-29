/*
 *  Created by Tri Phan on 7/01/19 8:36 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:36 PM
 */

package edu.flinders.crcapp.injection;

import android.support.annotation.NonNull;

import edu.flinders.crcapp.interactor.HelpInteractor;
import edu.flinders.crcapp.interactor.impl.HelpInteractorImpl;
import edu.flinders.crcapp.presenter.loader.PresenterFactory;
import edu.flinders.crcapp.presenter.HelpPresenter;
import edu.flinders.crcapp.presenter.impl.HelpPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class HelpViewModule {
    @Provides
    public HelpInteractor provideInteractor() {
        return new HelpInteractorImpl();
    }

    @Provides
    public PresenterFactory<HelpPresenter> providePresenterFactory(@NonNull final HelpInteractor interactor) {
        return new PresenterFactory<HelpPresenter>() {
            @NonNull
            @Override
            public HelpPresenter create() {
                return new HelpPresenterImpl(interactor);
            }
        };
    }
}
