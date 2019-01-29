/*
 *  Created by Tri Phan on 14/12/18 12:14 PM
 *  Copyright (c) 2018 . All rights reserved.
 *  Last modified 14/12/18 12:14 PM
 */

package edu.flinders.crcapp.view.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.flinders.crcapp.R;
import edu.flinders.crcapp.model.StringUtils;
import edu.flinders.crcapp.view.MainView;
import edu.flinders.crcapp.presenter.loader.PresenterFactory;
import edu.flinders.crcapp.presenter.MainPresenter;
import edu.flinders.crcapp.injection.AppComponent;
import edu.flinders.crcapp.injection.MainViewModule;
import edu.flinders.crcapp.injection.DaggerMainViewComponent;

import javax.inject.Inject;

public final class MainActivity extends BaseActivity<MainPresenter, MainView> implements MainView {
    Context mContext;
    Activity mActivity;

    @Inject
    PresenterFactory<MainPresenter> mPresenterFactory;

    @BindView(R.id.btn_record_new_sample)
    Button mBtnRecord;
    @BindView(R.id.btn_calib)
    Button mBtnCalib;
    @BindView(R.id.btn_review_prev_sample)
    Button mBtnReview;
    @BindView(R.id.btn_help)
    Button mBtnHelp;
    @BindView(R.id.btn_contact_us)
    Button mBtnContact;

    View.OnClickListener mOnClickListener;

    // Your presenter is available using the mPresenter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
        ButterKnife.bind(this);
        mContext = this;
        mActivity = this;
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerListeners();
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerMainViewComponent.builder()
                .appComponent(parentComponent)
                .mainViewModule(new MainViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void registerListeners() {
    }

    @Override @OnClick({R.id.btn_record_new_sample, R.id.btn_calib, R.id.btn_review_prev_sample, R.id.btn_help, R.id.btn_contact_us})
    public void moveToOtherActivity(View view) {
        Class<?> actName = null;
        Bundle b = null;
        switch (view.getId()) {
            case R.id.btn_record_new_sample:
                actName = CameraActivity.class;
                b = new Bundle();
                b.putInt(StringUtils.KEY_VIEW_ID, R.id.btn_record_new_sample);
                break;
            case R.id.btn_calib:
                actName = CalibrationActivity.class;
                break;
            case R.id.btn_review_prev_sample:
                actName = ReviewActivity.class;
                break;
            case R.id.btn_help:
                actName = HelpActivity.class;
                break;
            case R.id.btn_contact_us:
                actName = ContactActivity.class;
                break;
        }

        mPresenter.moveToOtherActivity(mActivity, actName, b);
    }

    @NonNull
    @Override
    protected PresenterFactory<MainPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }
}
