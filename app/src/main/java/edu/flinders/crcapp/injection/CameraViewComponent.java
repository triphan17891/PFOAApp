/*
 *  Created by Tri Phan on 7/01/19 10:22 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 10:22 PM
 */

package edu.flinders.crcapp.injection;

import edu.flinders.crcapp.view.impl.CameraActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = CameraViewModule.class)
public interface CameraViewComponent {
    void inject(CameraActivity activity);
}