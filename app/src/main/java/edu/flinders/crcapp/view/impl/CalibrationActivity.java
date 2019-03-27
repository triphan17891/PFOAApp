/*
 *  Created by Tri Phan on 7/01/19 8:30 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:30 PM
 */

package edu.flinders.crcapp.view.impl;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import edu.flinders.crcapp.R;
import edu.flinders.crcapp.model.StringUtils;
import edu.flinders.crcapp.view.CalibrationView;
import edu.flinders.crcapp.presenter.loader.PresenterFactory;
import edu.flinders.crcapp.presenter.CalibrationPresenter;
import edu.flinders.crcapp.injection.AppComponent;
import edu.flinders.crcapp.injection.CalibrationViewModule;
import edu.flinders.crcapp.injection.DaggerCalibrationViewComponent;

import javax.inject.Inject;

public final class CalibrationActivity extends BaseActivity<CalibrationPresenter, CalibrationView> implements CalibrationView {
    @Inject
    PresenterFactory<CalibrationPresenter> mPresenterFactory;

    @BindView(R.id.btn_one_point)
    Button mBtnOneCalib;
    @BindView(R.id.btn_two_point)
    Button mBtnTwoCalib;
    @BindView(R.id.btn_three_point)
    Button mBtnThreeCalib;
    @BindView(R.id.btn_advance)
    Button mBtnAdv;

    Context mContext;
    Activity mActivity;
    View.OnClickListener mOnClickListener;

    // Your presenter is available using the mPresenter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        ButterKnife.bind(this);

        mContext = this;
        mActivity = this;

        // modify action bar values
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.intro_first_page);
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerListeners();

        mBtnOneCalib.setOnClickListener(mOnClickListener);
        mBtnTwoCalib.setOnClickListener(mOnClickListener);
        mBtnThreeCalib.setOnClickListener(mOnClickListener);
        mBtnAdv.setOnClickListener(mOnClickListener);
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerCalibrationViewComponent.builder()
                .appComponent(parentComponent)
                .calibrationViewModule(new CalibrationViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void registerListeners() {
        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btn_advance:
                        mPresenter.moveToOtherActivity(mActivity, AdvanceSettingActivity.class, null);
                        break;
                    default:
                        Bundle b = new Bundle();
                        b.putInt(StringUtils.KEY_VIEW_ID, view.getId());
                        mPresenter.moveToOtherActivity(mActivity, CameraActivity.class, b);
                }
            }
        };
    }

    @NonNull
    @Override
    protected PresenterFactory<CalibrationPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
