/*
 *  Created by Tri Phan on 7/01/19 8:30 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:30 PM
 */

package edu.flinders.crcapp.injection;

import edu.flinders.crcapp.view.impl.CalibrationActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = CalibrationViewModule.class)
public interface CalibrationViewComponent {
    void inject(CalibrationActivity activity);
}