package com.chehubang.duolejie.modules.home.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import com.chehubang.duolejie.R;
import com.chehubang.duolejie.base.BaseActivity;
import com.chehubang.duolejie.config.UserInfo;
import com.chehubang.duolejie.listener.OnButtonClick;
import com.chehubang.duolejie.modules.home.fragment.GiftFragment;
import com.chehubang.duolejie.modules.home.fragment.HomeFragment;
import com.chehubang.duolejie.modules.home.fragment.MyFragment;
import com.chehubang.duolejie.modules.home.fragment.ShoppingFragment;
import com.chehubang.duolejie.modules.home.presenter.MainPresenter;
import com.chehubang.duolejie.modules.login.activity.LoginActivity;

import common.mvp.activity.MainView;

public class MainActivity extends BaseActivity<MainPresenter> implements MainView, View.OnClickListener, OnButtonClick {

    private FrameLayout home_content;
    private RadioButton rbuttonHome, rbuttonGift, rbuttonShopping, rbuttonMy;
    private Fragment mFragment;//当前显示的Fragment
    private HomeFragment fragment1;
    private GiftFragment fragment2;
    private ShoppingFragment fragment3;
    private MyFragment fragment4;
    private OnButtonClick mclick;
    private SwitchReceiver switchReceiver;
    private final String ACTION_SWITCH = "com.chehubang.duolejie.ACTION_SWITCH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.main_fragment, fragment1).commit();
        mFragment = fragment1;
        switchReceiver = new SwitchReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(switchReceiver, new IntentFilter(ACTION_SWITCH));
    }

    private void initView() {
        home_content = (FrameLayout) findViewById(R.id.main_fragment);
        rbuttonHome = (RadioButton) findViewById(R.id.rb_main_home);
        rbuttonGift = (RadioButton) findViewById(R.id.rb_main_gift);
        rbuttonShopping = (RadioButton) findViewById(R.id.rb_main_shopping);
        rbuttonMy = (RadioButton) findViewById(R.id.rb_main_my);
        rbuttonHome.setOnClickListener(this);
        rbuttonGift.setOnClickListener(this);
        rbuttonShopping.setOnClickListener(this);
        rbuttonMy.setOnClickListener(this);
        fragment1 = new HomeFragment();
        fragment2 = new GiftFragment();
        fragment3 = new ShoppingFragment();
        fragment3.setClickListener(this);
        fragment4 = new MyFragment();
        tmpBtn = rbuttonHome;
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter(this);
    }

    private RadioButton tmpBtn;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rb_main_home:
                tmpBtn = rbuttonHome;
                switchFragment(fragment1);
                break;
            case R.id.rb_main_gift:
                tmpBtn = rbuttonGift;
                switchFragment(fragment2);
                break;
            case R.id.rb_main_shopping:
                if (!TextUtils.isEmpty(UserInfo.getInstance().getId())) {
                    switchFragment(fragment3);
                    if (mclick != null) {
                        mclick.OnHomeButtonClick(3);
                    }
                } else {
                    if (tmpBtn != null) {
                        tmpBtn.setChecked(true);
                    }
                    rbuttonShopping.setChecked(false);
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rb_main_my:
                tmpBtn = rbuttonMy;
                switchFragment(fragment4);
                if (mclick != null) {
                    mclick.OnHomeButtonClick(4);
                }
                break;
            default:
                break;
        }
    }

    public void addButtonClickListener(OnButtonClick clickListener) {
        this.mclick = clickListener;
    }

    private void switchFragment(Fragment fragment) {
        if (mFragment != fragment) {
            if (!fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().hide(mFragment).add(R.id.main_fragment, fragment).commitAllowingStateLoss();
            } else {
                getSupportFragmentManager().beginTransaction().hide(mFragment).show(fragment).commitAllowingStateLoss();
            }
            mFragment = fragment;
        }
    }

    @Override
    public void getDataSuccess(Object model, int action) {

    }

    @Override
    public void getDataFail(String msg, int action) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(switchReceiver);
    }

    @Override
    public void OnHomeButtonClick(int position) {
        switchFragment(fragment1);
    }


    public class SwitchReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra("action");
            if (TextUtils.equals("fragment3", action)) {
                rbuttonShopping.performClick();
            } else if (TextUtils.equals("fragment4", action)) {
                fragment4.onRefreshUserImageHeader();
                rbuttonMy.performClick();
            }
        }
    }
}
