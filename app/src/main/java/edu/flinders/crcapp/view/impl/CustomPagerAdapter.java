/*
 *  Created by Tri Phan on 7/01/19 4:31 PM
 *  Copyright (c) 2019 . All rights reserved.
 *  Last modified 7/01/19 4:31 PM
 */

package edu.flinders.crcapp.view.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.audiofx.DynamicsProcessing;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bartoszlipinski.flippablestackview.FlippableStackView;
import com.bartoszlipinski.flippablestackview.StackPageTransformer;
import edu.flinders.crcapp.interactor.impl.CalibrationInteractorImpl;
import edu.flinders.crcapp.model.*;
import edu.flinders.crcapp.R;
import edu.flinders.crcapp.presenter.IntroPresenter;

public class CustomPagerAdapter extends PagerAdapter {
    private Context mContext;
    private int[] INTRO_CONTENT_TEXT_ID = {R.string.intro_calib_instruction, R.string.intro_analyze_sample, R.string.intro_review_results};

    public CustomPagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        IntroPageEnum modelObject = IntroPageEnum.values()[position];
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(modelObject.getLayoutResId(), collection, false);
        collection.addView(layout);

        final ViewHolder holder;
        holder = new ViewHolder(layout, modelObject.getLayoutResId());

        holder.tvTitle.setText(modelObject.getTitleResId());
        holder.tvContent.setText(INTRO_CONTENT_TEXT_ID[position]);
        if (position == 0) {
            Calibration calibObj = Equation.getSavedObjectFromPreference(
                    mContext,
                    Equation.SHARED_FILE_KEY,
                    Equation.SHARED_OBJ_KEY,
                    Calibration.class);
            if (calibObj == null) {
                holder.tvInfo.setText(R.string.calib_info_no_perform);
                holder.tvInfo.setTextColor(Color.RED);
            } else {
                String info = mContext.getResources().getString(R.string.calib_having_calibration)
                        + "\nC1: " + calibObj.get_c1()
                        + "\nC2: " + calibObj.get_c2()
                        + "\nC3: " + calibObj.get_c3()
                        + "\nC4: " + calibObj.get_c4();
                holder.tvInfo.setText(info);
                holder.tvInfo.setTextColor(Color.GREEN);
            }
        } else {
            holder.tvInfo.setText("");
        }

        // Change views based on layout id
        caseOrganize(modelObject.getLayoutResId(), position, holder);

        return layout;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private void caseOrganize(int layoutId, final int pos, ViewHolder holder) {
        if (layoutId == R.layout.view_intro_page) {
            holder.btnMove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Class<?> actName = null;
                    Bundle b = new Bundle();
                    IntroPresenter presenter = ((IntroActivity) mContext).mPresenter;
                    switch (pos) {
                        case 1:
                            actName = CameraActivity.class;
                            b.putInt(StringUtils.KEY_VIEW_ID, R.id.btn_record_new_sample);
                            break;
                        case 2:
                            actName = ReviewActivity.class;
                            break;
                    }

                    presenter.moveToOtherActivity((Activity) mContext, actName, b);
                }
            });
        } else if(layoutId == R.layout.view_calib_page) {
//            View.OnClickListener calibOnClickListener = new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    CalibrationInteractorImpl calibImplObj = new CalibrationInteractorImpl();
//                    Bundle b = new Bundle();
//                    b.putInt(StringUtils.KEY_VIEW_ID, view.getId());
//                    // Add the difference for Manual calibration
//                    if (view.getId() == R.id.btn_man_calib) {
//                        calibImplObj.moveToOtherActivity((Activity) mContext, AdvanceSettingActivity.class, b);
//                    } else {
//                        calibImplObj.moveToOtherActivity((Activity) mContext, CameraActivity.class, b);
//                    }
//                }
//            };
//
//            holder.btnOnePoint.setOnClickListener(calibOnClickListener);
//            holder.btnTwoPoint.setOnClickListener(calibOnClickListener);
//            holder.btnThreePoint.setOnClickListener(calibOnClickListener);
//            holder.btnManual.setOnClickListener(calibOnClickListener);
            holder.lvCalib.initStack(CalibrationEnum.values().length, StackPageTransformer.Orientation.VERTICAL);
            holder.lvCalib.setAdapter(new SimpleListAdapter(mContext));
        } else {
            if(holder.lvResult.getCount() == 0) {
                holder.tvMessage.setVisibility(View.VISIBLE);
            } else {
                holder.tvMessage.setVisibility(View.GONE);
            }
        }
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
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_info)
        TextView tvInfo;

        Button btnMove;
        ListView lvResult;
        FlippableStackView lvCalib;
        TextView tvMessage;

        public ViewHolder(View view, int layoutId) {
            ButterKnife.bind(this, view);
            // Get rid of null pointer error when assigning to null object from xml
            if (layoutId == R.layout.view_intro_page) {
                btnMove = view.findViewById(R.id.btn_move);
            } else if (layoutId == R.layout.view_calib_page) {
                lvCalib = view.findViewById(R.id.lv_calib);
            } else {
                lvResult = view.findViewById(R.id.lv_result);
                tvMessage = view.findViewById(R.id.tv_result_message);
            }

        }
    }
}
