/*
 *  Created by Tri Phan on 7/01/19 8:35 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:35 PM
 */

package edu.flinders.crcapp.injection;

import edu.flinders.crcapp.view.impl.ReviewActivity;

import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = ReviewViewModule.class)
public interface ReviewViewComponent {
    void inject(ReviewActivity activity);
}