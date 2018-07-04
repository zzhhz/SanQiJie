package com.chehubang.duolejie.modules.carlife.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.model.CarRecord;
import com.chehubang.duolejie.modules.carlife.presenter.CarRecordPresenter;
import com.chehubang.duolejie.utils.log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import common.Utils.JSONUtils;
import common.mvp.activity.MainView;

/**
 * Created by ZZH on 2018/2/6.
 *
 * @Date: 2018/2/6
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description: 车辆投保记录
 */
public class CarRecordActivity extends BaseActivity<CarRecordPresenter> implements MainView {
    @BindView(R.id.lv_list)
    ListView lv_list;
    CarAdapter mCarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_record);
        ButterKnife.bind(this);
        mCarAdapter = new CarAdapter();
        lv_list.setAdapter(mCarAdapter);
        mvpPresenter.getCarRecord();
    }

    @OnClick({R.id.iv_back})
    public void onClickView(View v) {
        switch (v.getId())
        {
            case R.id.iv_back:
                finish();
                break;
        }

    }

    @Override
    public void getDataSuccess(Object model, int action) {
        if (!TextUtils.isEmpty(model.toString())) {
            Wrapper wrapper = JSONUtils.GsonToBean(model.toString(), Wrapper.class);
            mCarAdapter.addAll(wrapper.getCarinsuranceList());
            mCarAdapter.notifyDataSetChanged();
            log.d(mCarAdapter.getCount() + "");
        }

    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected CarRecordPresenter createPresenter() {
        return new CarRecordPresenter(this);
    }

    class Wrapper {
        private List<CarRecord> carinsuranceList;

        public List<CarRecord> getCarinsuranceList() {
            return carinsuranceList;
        }

        public void setCarinsuranceList(List<CarRecord> carinsuranceList) {
            this.carinsuranceList = carinsuranceList;
        }
    }

    class CarAdapter extends BaseAdapter {

        private List<CarRecord> dataList;
        SimpleDateFormat mDateFormat;

        public CarAdapter() {
            dataList = new ArrayList<>();
            mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

        public void addAll(List<CarRecord> list) {
            dataList.addAll(list);
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(CarRecordActivity.this).inflate(R.layout.item_car_record, null);

                viewHolder = new ViewHolder();
                viewHolder.carName = (TextView) convertView.findViewById(R.id.tv_car_num);
                viewHolder.carStatus = (TextView) convertView.findViewById(R.id.tv_status);
                viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.iv_car);
                viewHolder.carTime = (TextView) convertView.findViewById(R.id.tv_car_time);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.carStatus.setText("已提交");
            CarRecord record = dataList.get(position);
            viewHolder.carName.setText("车牌：" + record.getLicense_plate());
            viewHolder.carTime.setText(mDateFormat.format(new Date(Long.parseLong(record.getCreate_time()))));
            return convertView;
        }

        class ViewHolder {
            ImageView mImageView;
            TextView carName;
            TextView carStatus;
            TextView carTime;
        }
    }
}
