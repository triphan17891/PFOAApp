/*
 *  Created by Tri Phan on 7/01/19 4:31 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 4:31 PM
 */

package edu.flinders.crcapp.view.impl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.flinders.crcapp.model.IntroPageEnum;
import edu.flinders.crcapp.R;
import edu.flinders.crcapp.model.StringUtils;
import edu.flinders.crcapp.presenter.IntroPresenter;

public class CustomPagerAdapter extends PagerAdapter {
    private Context mContext;

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        IntroPageEnum modelObject = IntroPageEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        collection.addView(layout);

        ViewHolder holder;
        holder = new ViewHolder(layout);

        holder.tvTitle.setText(modelObject.getTitleResId());
        final int pos = position;
        holder.btnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Class<?> actName = null;
                Bundle b = new Bundle();
                IntroPresenter presenter = ((IntroActivity) mContext).mPresenter;
                switch(pos) {
                    case 0:
                        actName = CalibrationActivity.class;
                        break;
                    case 1:
                        actName = CameraActivity.class;
                        b.putInt(StringUtils.KEY_VIEW_ID, R.id.btn_record_new_sample);
                        break;
                    case 2:
                        actName = ReviewActivity.class;
                        break;
                    case 3:
                        actName = HelpActivity.class;
                        break;
                    case 4:
                        actName = ContactActivity.class;
                        break;
                }

                presenter.moveToOtherActivity((Activity) mContext, actName, b);
            }
        });

        return layout;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        return IntroPageEnum.values().length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        IntroPageEnum customPagerEnum = IntroPageEnum.values()[position];
        return mContext.getString(customPagerEnum.getTitleResId());
    }

    static class ViewHolder {
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_content) TextView tvContent;
        @BindView(R.id.btn_move) Button btnMove;
        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
