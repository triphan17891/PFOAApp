/*
 *  Created by Tri Phan on 7/01/19 8:42 AM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:42 AM
 */

package edu.flinders.crcapp.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.LoaderManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import butterknife.BindView;
import butterknife.ButterKnife;
import edu.flinders.crcapp.R;
import edu.flinders.crcapp.view.IntroView;
import edu.flinders.crcapp.presenter.loader.PresenterFactory;
import edu.flinders.crcapp.presenter.IntroPresenter;
import edu.flinders.crcapp.injection.AppComponent;
import edu.flinders.crcapp.injection.IntroViewModule;
import edu.flinders.crcapp.injection.DaggerIntroViewComponent;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public final class IntroActivity extends BaseActivity<IntroPresenter, IntroView> implements IntroView {
    @BindView(R.id.vp_intro) ViewPager mVpIntro;

    @Inject
    PresenterFactory<IntroPresenter> mPresenterFactory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        mVpIntro.setAdapter(new CustomPagerAdapter(this));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerIntroViewComponent.builder()
                .appComponent(parentComponent)
                .introViewModule(new IntroViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void registerListeners() {

    }

    @NonNull
    @Override
    protected PresenterFactory<IntroPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }
}
