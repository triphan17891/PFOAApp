/*
 *  Created by Tri Phan on 7/01/19 8:33 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:33 PM
 */

package edu.flinders.crcapp.injection;

import edu.flinders.crcapp.view.impl.RecordActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = RecordViewModule.class)
public interface RecordViewComponent {
    void inject(RecordActivity activity);
}