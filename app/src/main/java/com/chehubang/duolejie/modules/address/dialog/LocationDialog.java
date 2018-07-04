package com.chehubang.duolejie.modules.address.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.AreaBean;

import java.util.ArrayList;
import java.util.List;

import common.Utils.ToastUtils;
import common.view.widget.NumberPickerView;


public class LocationDialog extends Dialog implements View.OnClickListener {

    private List<String> area1 = new ArrayList<String>();
    private List<String> area2 = new ArrayList<String>();
    private List<String> areaid1 = new ArrayList<String>();
    private List<String> areaid2 = new ArrayList<String>();

    private DataSelectedListener mSListener = null;
    private List<AreaBean> mlist = new ArrayList<>();
    private Context mcontext;
    private NumberPickerView npLoad1Edit, npLoad2Edit;

    public LocationDialog(@NonNull Context context, List<AreaBean> list) {//
        super(context, R.style.DialogStyleBottom);
        this.mcontext = context;
        this.mlist = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loction_select);
        npLoad1Edit = (NumberPickerView) findViewById(R.id.np_load1_edit);
        npLoad2Edit = (NumberPickerView) findViewById(R.id.np_load2_edit);
        for (int i = 0; i < mlist.size(); i++) {
            area1.add(mlist.get(i).getAddr());
            areaid1.add(mlist.get(i).getId());
        }
        for (int i = 0; i < mlist.get(0).getCity().size(); i++) {
            area2.add(mlist.get(0).getCity().get(i).getAddr());
            areaid2.add(mlist.get(0).getCity().get(i).getId());
        }
        //添加数据
        npLoad1Edit.refreshByNewDisplayedValues(area1.toArray(new String[area1.size()]));
        npLoad1Edit.setValue(0);

        npLoad2Edit.refreshByNewDisplayedValues(area2.toArray(new String[area2.size()]));
        npLoad2Edit.setValue(0);

        findViewById(R.id.tv_dial_logcation_weight_cancel).setOnClickListener(this);
        findViewById(R.id.tv_dial_logcation_weight_save).setOnClickListener(this);
        npLoad1Edit.setOnScrollListener(new NumberPickerView.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPickerView view, int scrollState) {
                switch (scrollState) {
                    case NumberPickerView.OnScrollListener.SCROLL_STATE_IDLE:
                        int value = npLoad1Edit.getValue();
                        //1级菜单滑动的时候的处理   TODO
                        area2.clear();
                        areaid2.clear();
                        for (int i = 0; i < mlist.get(value).getCity().size(); i++) {
                            area2.add(mlist.get(value).getCity().get(i).getAddr());
                            areaid2.add(mlist.get(value).getCity().get(i).getId());
                        }
                        npLoad2Edit.refreshByNewDisplayedValues(area2.toArray(new String[area2.size()]));
                        npLoad2Edit.setValue(0);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void setOnDataSelectedListener(DataSelectedListener listener) {
        mSListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_dial_logcation_weight_cancel:
                dismiss();
                break;
            case R.id.tv_dial_logcation_weight_save:
                int value1 = npLoad1Edit.getValue();
                int value2 = npLoad2Edit.getValue();
                if (String.valueOf(value2) == null||String.valueOf(value1) == null) {
                    ToastUtils.showToast(mcontext, "请选择正确的社区");
                    return;
                }
                String contentByCurrValue = npLoad1Edit.getContentByCurrValue();
                String contentByCurrValue1 = npLoad2Edit.getContentByCurrValue();
                String id1 = areaid1.get(value1);
                String id2 = areaid2.get(value2);
                if (mSListener != null) {
                    mSListener.dataSelected(contentByCurrValue, contentByCurrValue1, id1, id2);
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface DataSelectedListener {
        void dataSelected(String location1, String location2, String id1, String id2);
    }
}
