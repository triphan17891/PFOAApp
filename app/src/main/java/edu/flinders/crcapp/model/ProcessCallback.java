/*
 *  Created by Tri Phan on 5/02/19 3:13 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 5/02/19 3:13 PM
 */

package edu.flinders.crcapp.model;

public interface ProcessCallback {
    void onStart();
    void inProcess();
    void onSuccess();
    void onFailure();
}
