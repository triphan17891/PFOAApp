/*
 *  Created by Tri Phan on 20/03/19 12:06 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 20/03/19 11:55 AM
 */

package edu.flinders.crcapp.view.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import edu.flinders.crcapp.R;
import edu.flinders.crcapp.interactor.impl.CalibrationInteractorImpl;
import edu.flinders.crcapp.model.*;
import edu.flinders.crcapp.presenter.IntroPresenter;

public class SimpleListAdapter extends PagerAdapter implements View.OnClickListener {
    private Context mContext;

    private static class ViewHolder {
        TextView tvtitle;
        TextView tvDesc;
    }

    public SimpleListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        CalibrationInteractorImpl calibImplObj = new CalibrationInteractorImpl();
        Bundle b = new Bundle();
        b.putInt(StringUtils.KEY_VIEW_ID, (Integer) v.getTag());
        switch (position) {
            case 3:
            case 2:
            case 1:
                calibImplObj.moveToOtherActivity((Activity) mContext, CameraActivity.class, b);
                break;
            case 0:
                calibImplObj.moveToOtherActivity((Activity) mContext, AdvanceSettingActivity.class, b);
                break;
        }
    }

    @Override
    public int getCount() {
        return CalibrationEnum.values().length;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        CalibrationEnum modelObject = CalibrationEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.item_simple, collection, false);
        collection.addView(layout);

        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tvtitle = layout.findViewById(R.id.tv_item_title);
        viewHolder.tvDesc = layout.findViewById(R.id.tv_item_desciption);

        viewHolder.tvtitle.setText(modelObject.getTitleResId());
        viewHolder.tvDesc.setText(modelObject.getDescResId());

        ((LinearLayout) viewHolder.tvtitle.getParent()).setOnClickListener(this);
        ((LinearLayout) viewHolder.tvtitle.getParent()).setTag(position);
        // Return the completed view to render on screen
        return layout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
