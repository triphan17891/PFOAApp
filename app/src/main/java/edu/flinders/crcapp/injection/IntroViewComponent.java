/*
 *  Created by Tri Phan on 7/01/19 8:42 AM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:42 AM
 */

package edu.flinders.crcapp.injection;

import edu.flinders.crcapp.view.impl.IntroActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = IntroViewModule.class)
public interface IntroViewComponent {
    void inject(IntroActivity activity);
}