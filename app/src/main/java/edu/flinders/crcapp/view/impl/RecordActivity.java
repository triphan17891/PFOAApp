/*
 *  Created by Tri Phan on 7/01/19 8:33 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 8:33 PM
 */

package edu.flinders.crcapp.view.impl;

import android.os.Bundle;
import android.support.annotation.NonNull;

import android.view.Menu;
import android.view.MenuItem;
import edu.flinders.crcapp.R;
import edu.flinders.crcapp.view.RecordView;
import edu.flinders.crcapp.presenter.loader.PresenterFactory;
import edu.flinders.crcapp.presenter.RecordPresenter;
import edu.flinders.crcapp.injection.AppComponent;
import edu.flinders.crcapp.injection.RecordViewModule;
import edu.flinders.crcapp.injection.DaggerRecordViewComponent;

import javax.inject.Inject;

public final class RecordActivity extends BaseActivity<RecordPresenter, RecordView> implements RecordView {
    @Inject
    PresenterFactory<RecordPresenter> mPresenterFactory;

    // Your presenter is available using the mPresenter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.intro_second_page);

        // Your code here
        // Do not call mPresenter from here, it will be null! Wait for onStart or onPostCreate.
    }

    @Override
    protected void setupComponent(@NonNull AppComponent parentComponent) {
        DaggerRecordViewComponent.builder()
                .appComponent(parentComponent)
                .recordViewModule(new RecordViewModule())
                .build()
                .inject(this);
    }

    @Override
    protected void registerListeners() {

    }

    @NonNull
    @Override
    protected PresenterFactory<RecordPresenter> getPresenterFactory() {
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
