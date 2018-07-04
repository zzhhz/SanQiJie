package com.chehubang.duolejie.modules.mysetting.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;


import com.chehubang.duolejie.R;

import common.view.widget.NumberPickerView;

/**
 * Created by Bob on 2017/3/20.
 */

public class GenderDialog extends Dialog implements View.OnClickListener{

    private String[] gender = new String[]{"男","女"};
    private NumberPickerView genderSelect;
    private genderSelectedListener mSListener = null;

    public GenderDialog(@NonNull Context context) {
        super(context, R.style.DialogStyleBottom);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_gender_select);
        genderSelect = (NumberPickerView) findViewById(R.id.np_gender_edit);
        genderSelect.refreshByNewDisplayedValues(gender);
        findViewById(R.id.tv_dial_gender_cancel).setOnClickListener(this);
        findViewById(R.id.tv_dial_gender_save).setOnClickListener(this);
    }
    public void setOngenderSelectedListener(genderSelectedListener listener) {
        mSListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_dial_gender_cancel:
                dismiss();
                break;
            case R.id.tv_dial_gender_save:
                String contentByCurrValue = genderSelect.getContentByCurrValue();
                if (mSListener != null) {
                    mSListener.genderSelected(contentByCurrValue);
                }
                dismiss();
                break;
        }
    }

    public interface genderSelectedListener{
        void genderSelected(String gender);
    }

}
