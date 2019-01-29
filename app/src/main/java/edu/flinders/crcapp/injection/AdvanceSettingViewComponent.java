/*
 *  Created by Tri Phan on 29/01/19 6:26 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 29/01/19 6:26 PM
 */

package edu.flinders.crcapp.injection;

import edu.flinders.crcapp.view.impl.AdvanceSettingActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = AdvanceSettingViewModule.class)
public interface AdvanceSettingViewComponent {
    void inject(AdvanceSettingActivity activity);
}