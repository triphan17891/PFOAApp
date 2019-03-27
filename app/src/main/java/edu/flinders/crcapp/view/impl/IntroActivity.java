/*
 *  Created by Tri Phan on 7/01/19 8:42 AM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:42 AM
 */

package edu.flinders.crcapp.view.impl;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.LoaderManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.shuhart.stepview.StepView;
import edu.flinders.crcapp.R;
import edu.flinders.crcapp.model.SimpleCallback;
import edu.flinders.crcapp.model.StringUtils;
import edu.flinders.crcapp.view.IntroView;
import edu.flinders.crcapp.presenter.loader.PresenterFactory;
import edu.flinders.crcapp.presenter.IntroPresenter;
import edu.flinders.crcapp.injection.AppComponent;
import edu.flinders.crcapp.injection.IntroViewModule;
import edu.flinders.crcapp.injection.DaggerIntroViewComponent;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

public final class IntroActivity extends BaseActivity<IntroPresenter, IntroView> implements IntroView {
    @BindView(R.id.vp_intro)
    ViewPager mVpIntro;

    @Inject
    PresenterFactory<IntroPresenter> mPresenterFactory;

    private StepView mStepIndicator;
    private CustomPagerAdapter mCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.bind(this);

        mStepIndicator = findViewById(R.id.step_view);

        mStepIndicator.getState().commit();
        mStepIndicator.done(false);

        mCustomAdapter = new CustomPagerAdapter(this);

        mVpIntro.setAdapter(mCustomAdapter);
        mVpIntro.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mStepIndicator.go(i, true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mStepIndicator.setOnStepClickListener(new StepView.OnStepClickListener() {
            @Override
            public void onStepClick(int step) {
                mVpIntro.setCurrentItem(step, true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if any change of Calibration data from SharedPreference and load it into page
        mCustomAdapter.notifyDataSetChanged();
        // Do a dirty trick here to refresh new data for calibration page  :))
        if(mVpIntro.getCurrentItem() == 0) {
            mVpIntro.setCurrentItem(2, false);
            mVpIntro.setCurrentItem(0, false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final Class actName;
        switch (item.getItemId()) {
            case R.id.menu_contact:
                actName = ContactActivity.class;
                mPresenter.moveToOtherActivity(this, actName, null);
                return (true);
            case R.id.menu_help:
                actName = HelpActivity.class;
                mPresenter.moveToOtherActivity(this, actName, null);
                return (true);
        }

        return (super.onOptionsItemSelected(item));
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
