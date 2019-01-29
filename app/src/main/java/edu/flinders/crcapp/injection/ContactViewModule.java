/*
 *  Created by Tri Phan on 7/01/19 8:36 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:36 PM
 */

package edu.flinders.crcapp.injection;

import android.support.annotation.NonNull;

import edu.flinders.crcapp.interactor.ContactInteractor;
import edu.flinders.crcapp.interactor.impl.ContactInteractorImpl;
import edu.flinders.crcapp.presenter.loader.PresenterFactory;
import edu.flinders.crcapp.presenter.ContactPresenter;
import edu.flinders.crcapp.presenter.impl.ContactPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class ContactViewModule {
    @Provides
    public ContactInteractor provideInteractor() {
        return new ContactInteractorImpl();
    }

    @Provides
    public PresenterFactory<ContactPresenter> providePresenterFactory(@NonNull final ContactInteractor interactor) {
        return new PresenterFactory<ContactPresenter>() {
            @NonNull
            @Override
            public ContactPresenter create() {
                return new ContactPresenterImpl(interactor);
            }
        };
    }
}
