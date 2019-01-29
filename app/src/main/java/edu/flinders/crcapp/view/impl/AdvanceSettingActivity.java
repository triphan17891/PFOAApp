/*
 *  Created by Tri Phan on 29/01/19 6:26 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 29/01/19 6:26 PM
 */

package edu.flinders.crcapp.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import edu.flinders.crcapp.R;
import edu.flinders.crcapp.view.AdvanceSettingView;
import edu.flinders.crcapp.presenter.loader.PresenterFactory;
import edu.flinders.crcapp.presenter.AdvanceSettingPresenter;
import edu.flinders.crcapp.injection.AppComponent;
import edu.flinders.crcapp.injection.AdvanceSettingViewModule;
import edu.flinders.crcapp.injection.DaggerAdvanceSettingViewComponent;

import javax.inject.Inject;

public final class AdvanceSettingActivity extends BaseActivity<AdvanceSettingPresenter, AdvanceSettingView> implements AdvanceSettingView {
    @Inject
    PresenterFactory<AdvanceSettingPresenter> mPresenterFactory;

    @BindView(R.id.edt_a1_override)
    EditText mEdtA1;
    @BindView(R.id.edt_a2_override)
    EditText mEdtA2;
    @BindView(R.id.edt_a3_override)
    EditText mEdtA3;
    @BindView(R.id.edt_a4_override)
    EditText mEdtA4;
    @BindView(R.id.btn_apply_overrides)
    Button mBtnApply;
    @BindView(R.id.btn_clear_overrides)
    Button mBtnClear;

    // Your presenter is available using the mPresenter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_setting);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.calib_setting_title);
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerAdvanceSettingViewComponent.builder()
                .appComponent(parentComponent)
                .advanceSettingViewModule(new AdvanceSettingViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerListeners();
    }

    @Override
    protected void registerListeners() {

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

    @Override @OnClick(R.id.btn_clear_overrides)
    public void clearAllOverrides(){
        mEdtA1.setText("");
        mEdtA2.setText("");
        mEdtA3.setText("");
        mEdtA4.setText("");
    }

    @Override @OnTextChanged({R.id.edt_a1_override, R.id.edt_a2_override, R.id.edt_a3_override, R.id.edt_a4_override})
    public void enableButtons() {
        String t = mEdtA1.getText().toString() + mEdtA2.getText().toString()
                + mEdtA3.getText().toString() + mEdtA4.getText().toString();
        if(t != "") {
            mBtnApply.setEnabled(true);
            mBtnClear.setEnabled(true);
        } else {
            mBtnApply.setEnabled(false);
            mBtnClear.setEnabled(false);
        }
    }

    @Override @OnClick(R.id.btn_apply_overrides)
    public void applyAllOverrides() {

    }

    @NonNull
    @Override
    protected PresenterFactory<AdvanceSettingPresenter> getPresenterFactory() {
        return mPresenterFactory;
    }
}
