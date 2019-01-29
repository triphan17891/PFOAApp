/*
 *  Created by Tri Phan on 7/01/19 3:00 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 3:00 PM
 */

package edu.flinders.crcapp.model;

import edu.flinders.crcapp.R;

public enum IntroPageEnum {
    FIRST_PAGE(R.string.intro_first_page, R.layout.view_intro_page),
    SECOND_PAGE(R.string.intro_second_page, R.layout.view_intro_page),
    THIRD_PAGE(R.string.intro_third_page, R.layout.view_intro_page),
    FORTH_PAGE(R.string.intro_forth_page, R.layout.view_intro_page),
    FIFTH_PAGE(R.string.intro_fifth_page, R.layout.view_intro_page);

    private int mTitleResId;
    private int mLayoutResId;

    IntroPageEnum(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
