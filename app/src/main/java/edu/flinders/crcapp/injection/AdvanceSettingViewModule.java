/*
 *  Created by Tri Phan on 29/01/19 6:26 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 29/01/19 6:26 PM
 */

package edu.flinders.crcapp.injection;

import android.support.annotation.NonNull;

import edu.flinders.crcapp.interactor.AdvanceSettingInteractor;
import edu.flinders.crcapp.interactor.impl.AdvanceSettingInteractorImpl;
import edu.flinders.crcapp.presenter.loader.PresenterFactory;
import edu.flinders.crcapp.presenter.AdvanceSettingPresenter;
import edu.flinders.crcapp.presenter.impl.AdvanceSettingPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class AdvanceSettingViewModule {
    @Provides
    public AdvanceSettingInteractor provideInteractor() {
        return new AdvanceSettingInteractorImpl();
    }

    @Provides
    public PresenterFactory<AdvanceSettingPresenter> providePresenterFactory(@NonNull final AdvanceSettingInteractor interactor) {
        return new PresenterFactory<AdvanceSettingPresenter>() {
            @NonNull
            @Override
            public AdvanceSettingPresenter create() {
                return new AdvanceSettingPresenterImpl(interactor);
            }
        };
    }
}
