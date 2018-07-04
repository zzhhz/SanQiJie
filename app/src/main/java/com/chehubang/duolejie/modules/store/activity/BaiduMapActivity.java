package com.chehubang.duolejie.modules.store.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.chehubang.duolejie.R;
import com.chehubang.duolejie.model.Address;
import com.chehubang.duolejie.utils.log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ZZH on 2018/2/10.
 *
 * @Date: 2018/2/10
 * @Email: zzh_hz@126.com
 * @QQ: 1299234582
 * @Author: zzh
 * @Description:
 */
public class BaiduMapActivity extends AppCompatActivity {
    @BindView(R.id.map_list)
    ListView mListView;
    @BindView(R.id.mapView)
    MapView mMapView;

    private BaiduMap mBaiduMap;
    private PoiSearch mPoiSearch = null;
    GeoCoder geoSearch = null;
    private LatLng lastLatLng;
    private List<Address> addressList;
    AddressPoiListAdapter poiAdapter;

    private double currentLat;
    private double currentLng;
    public MyLocationListenerExt myListener = new MyLocationListenerExt();
    // 定位相关
    LocationClient mLocClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        addressList = new ArrayList<>();
        lastLatLng = new LatLng(0, 0);
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);// 设置地图的缩放比例
        mBaiduMap.setMapStatus(msu);// 将前面的参数交给BaiduMap类
        geoSearch = GeoCoder.newInstance();
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                if (lastLatLng.latitude == mapStatus.target.latitude
                        && lastLatLng.longitude == mapStatus.target.longitude) {
                    return;
                } else {
                    geoSearch.reverseGeoCode(new ReverseGeoCodeOption()
                            .location(new LatLng(mapStatus.target.latitude,
                                    mapStatus.target.longitude)));
                    lastLatLng = new LatLng(mapStatus.target.latitude,
                            mapStatus.target.longitude);
                }
            }
        });

        mLocClient = new LocationClient(this);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        mLocClient.setLocOption(option);
        mLocClient.start();
        mLocClient.registerLocationListener(myListener);

        geoSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
                if (addressList != null) {
                    addressList.clear();
                }
                if (result.getPoiList() != null
                        && result.getPoiList().size() > 0) {
                    for (int i = 0; i < result.getPoiList().size(); i++) {
                        PoiInfo info = result.getPoiList().get(i);
                        Address paddress = new Address();
                        paddress.setAddress(result.getAddress());
                        paddress.setArea(result.getAddressDetail().district);
                        paddress.setCity(result.getAddressDetail().city);
                        paddress.setProvince(result
                                .getAddressDetail().province);
                        paddress.setDetailName(info.address);
                        paddress.setAddress(info.name);
                        paddress.setLongitude(info.location.longitude);
                        paddress.setLatitude(info.location.latitude);
                        if (i == 0) {
                            paddress.setCurrent(true);
                        } else {
                            paddress.setCurrent(false);
                        }
                        addressList.add(paddress);
                    }
                }
                poiAdapter.setAddressList(addressList);
            }
        });
        poiAdapter = new AddressPoiListAdapter(this);
        mListView.setAdapter(poiAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Address item = poiAdapter.getItem(position);

                Intent intent = new Intent();
                intent.putExtra("data", item.getCity() + "," + item.getDetailName() + item.getAddress());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 定位SDK监听函数
     */
    class MyLocationListenerExt extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            lastLatLng = new LatLng(location.getLatitude(),
                    location.getLongitude());
            currentLat = location.getLatitude();
            currentLng = location.getLongitude();
            log.d(currentLat + ", " + currentLng);
            if (currentLat > 0 && currentLng > 0) {
                LatLng ll = new LatLng(currentLat, currentLng);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
                geoSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ll));
            }
        }
    }

    public void onClickView(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
