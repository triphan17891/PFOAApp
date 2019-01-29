/*
 *  Created by Tri Phan on 29/01/19 6:26 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 29/01/19 6:26 PM
 */

package edu.flinders.crcapp.view;

import android.support.annotation.UiThread;

@UiThread
public interface AdvanceSettingView {
    void clearAllOverrides();
    void applyAllOverrides();
    void enableButtons();
}