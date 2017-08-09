package com.zhuoxin.treasure.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.zhuoxin.treasure.R;
import com.zhuoxin.treasure.commons.ActivityUtils;
import com.zhuoxin.treasure.treasure.Area;
import com.zhuoxin.treasure.treasure.Treasure;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/8/3.
 */

public class MapFragment extends Fragment implements MapFragmentView{
    private static final int REUUEST_COAD =100 ;
    @BindView(R.id.iv_located)
    ImageView mIvLocated;
    @BindView(R.id.btn_HideHere)
    Button mBtnHideHere;
    @BindView(R.id.centerLayout)
    RelativeLayout mCenterLayout;
    @BindView(R.id.iv_scaleUp)
    ImageView mIvScaleUp;
    @BindView(R.id.iv_scaleDown)
    ImageView mIvScaleDown;
    @BindView(R.id.tv_located)
    TextView mTvLocated;
    @BindView(R.id.tv_satellite)
    TextView mTvSatellite;
    @BindView(R.id.tv_compass)
    TextView mTvCompass;
    @BindView(R.id.ll_locationBar)
    LinearLayout mLlLocationBar;
    @BindView(R.id.tv_currentLocation)
    TextView mTvCurrentLocation;
    @BindView(R.id.iv_toTreasureInfo)
    ImageView mIvToTreasureInfo;
    @BindView(R.id.et_treasureTitle)
    EditText mEtTreasureTitle;
    @BindView(R.id.cardView)
    CardView mCardView;
    @BindView(R.id.layout_bottom)
    FrameLayout mLayoutBottom;
    @BindView(R.id.map_frame)
    FrameLayout mMapFrame;
    Unbinder unbinder;
    private BaiduMap mBaiduMap;
    private boolean isFirst=true;
    private LocationClient mLocationClient;
    private ActivityUtils mActivityUtils;
    private LatLng mCurrentStatus;
    private BitmapDescriptor mBitmapDescriptor;
    private Marker mCurrentMarker;
    private BitmapDescriptor mInfoWindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, null);
        unbinder = ButterKnife.bind(this, view);
        mActivityUtils = new ActivityUtils(getActivity());
        if (ContextCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},REUUEST_COAD);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initLocation();
    }
    //当onCreateView执行完毕之后执行
    private void initLocation() {
        //激活百度地图图层的定位功能
        mBaiduMap.setMyLocationEnabled(true);
        //第一步，初始化LocationClient类

        mLocationClient = new LocationClient(getContext().getApplicationContext());

        //第二步，配置定位SDK参数
        LocationClientOption mLocationClientOption=new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        mLocationClientOption.setCoorType("bd09ll");
        //可选，设置是否需要地址信息，默认不需要
        mLocationClientOption.setIsNeedAddress(true);
        //可选，默认false,设置是否使用gps
        mLocationClientOption.setOpenGps(true);

        mLocationClient.setLocOption(mLocationClientOption);

        //第三步，注册位置监听
        mLocationClient.registerLocationListener( myListener );

        //第四补，开启定位
        mLocationClient.start();
    }

    private LatLng mLatLng;
    private BDLocationListener myListener=new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            String addrStr = bdLocation.getAddrStr();
            double latitude = bdLocation.getLatitude();
            double longitude = bdLocation.getLongitude();
            Log.d("TAG","现在位于："+addrStr+",经纬度为："+longitude+","+latitude);

            MyLocationData myLocationData = new MyLocationData.Builder()
                    .accuracy(100f)
                    .latitude(latitude)
                    .longitude(longitude)
                    .build();
            mLatLng = new LatLng(latitude, longitude);
            updateView(mLatLng);
            mBaiduMap.setMyLocationData(myLocationData);
            if (isFirst){
                moveToLocation();
                isFirst=false;
            }
        }
    };

    private void initView() {
        MapStatus builder = new MapStatus
                .Builder()
                .overlook(0f)
                .rotate(0f)
                .zoom(11)
                .build();

        BaiduMapOptions baiduMapOptions = new BaiduMapOptions();
        baiduMapOptions.mapStatus(builder)
                .scaleControlEnabled(false)
                .zoomControlsEnabled(false)
                .zoomGesturesEnabled(true);
        MapView mapView = new MapView(getContext(), baiduMapOptions);
        mBaiduMap = mapView.getMap();
        mMapFrame.addView(mapView, 0);
        mBaiduMap.setOnMapStatusChangeListener(mOnMapState);
        mBitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_dot);
        mInfoWindow = BitmapDescriptorFactory.fromResource(R.mipmap.treasure_expanded);
        mBaiduMap.setOnMarkerClickListener(mMarkerClickListener);
    }
    private BaiduMap.OnMarkerClickListener mMarkerClickListener=new BaiduMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(final Marker marker) {
            if (mCurrentMarker!=null){
                mCurrentMarker.setVisible(true);
            }
            mCurrentMarker=marker;
            mCurrentMarker.setVisible(false);
            InfoWindow infoWindow = new InfoWindow(mInfoWindow, marker.getPosition(), 0, new InfoWindow.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick() {
                    marker.setVisible(true);
                    mBaiduMap.hideInfoWindow();
                }
            });
            mBaiduMap.showInfoWindow(infoWindow);
            return true;
        }
    };

    private BaiduMap.OnMapStatusChangeListener mOnMapState=new BaiduMap.OnMapStatusChangeListener() {
        @Override
        public void onMapStatusChangeStart(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChange(MapStatus mapStatus) {

        }

        @Override
        public void onMapStatusChangeFinish(MapStatus mapStatus) {
            LatLng target = mapStatus.target;
            if (target!=mCurrentStatus){
                updateView(target);
                mCurrentStatus=target;
            }

        }
    };
    //拿到宝藏
    private void updateView(LatLng mapStatus) {

        double latitude = mapStatus.latitude;//纬度
        double longitude = mapStatus.longitude;//经度

        Area area = new Area();
        area.setMaxLat(Math.ceil(latitude));
        area.setMaxLng(Math.ceil(longitude));
        area.setMinLat(Math.floor(latitude));
        area.setMinLng(Math.floor(longitude));

        new MapFragmentPresenter(this).getTreasureInArea(area);

    }

    @OnClick(R.id.tv_located)
    public void moveToLocation() {
        // TODO: 2017/8/6 点击定位
        MapStatus build = new MapStatus.Builder()
                .zoom(14)
                .target(mLatLng)
                .build();
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(build));

    }
    @OnClick({R.id.tv_satellite})//卫星地图切换
    public void switchMapType(){
        int mapType = mBaiduMap.getMapType();
        mapType=mapType==BaiduMap.MAP_TYPE_NORMAL?BaiduMap.MAP_TYPE_SATELLITE:BaiduMap.MAP_TYPE_NORMAL;
        String msg = mapType==BaiduMap.MAP_TYPE_NORMAL?"卫星":"普通";
        mBaiduMap.setMapType(mapType);
        mTvSatellite.setText(msg);
    }
    @OnClick({R.id.tv_compass})//指南针
    public void compass(){
        boolean compassEnabled = mBaiduMap.getUiSettings().isCompassEnabled();
        mBaiduMap.getUiSettings().setCompassEnabled(!compassEnabled);
    }
    @OnClick({R.id.iv_scaleUp,R.id.iv_scaleDown})//缩放
    public void switchMapScale(View view){
        switch (view.getId()){
            case R.id.iv_scaleUp:
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomIn());
                break;
            case R.id.iv_scaleDown:
                mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomOut());
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REUUEST_COAD:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mLocationClient.requestLocation();
                }else{
                    Toast.makeText(getContext(), "权限不足", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
 //-----------------------------实现自视图接口的方法
    @Override
    public void showMessage(String message) {
        mActivityUtils.showToast(message);
    }

    @Override
    public void setTreasureData(List<Treasure> treasureList) {
        // TODO: 2017/8/7
        mBaiduMap.clear();
        Log.d("TAG",treasureList.size()+"");
        for (Treasure mtTreasure:
             treasureList) {
            Bundle bundle = new Bundle();
            bundle.putInt("treasure_id",mtTreasure.getId());
            MarkerOptions options = new MarkerOptions();
            options.anchor(0.5f,0.5f);
            options.extraInfo(bundle);
            options.icon(mBitmapDescriptor);
            options.position(new LatLng(mtTreasure.getLatitude(),mtTreasure.getLongitude()));
            mBaiduMap.addOverlay(options);
        }
    }
}
