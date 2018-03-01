package com.navifunctiontest.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.leador.api.maps.MapController;
import com.leador.api.maps.MapView;
import com.leador.api.maps.model.BitmapDescriptorFactory;
import com.leador.api.maps.model.LatLng;
import com.leador.api.maps.model.MarkerOptions;
import com.leador.api.navi.LeadorNavi;
import com.leador.api.navi.LeadorNaviListener;
import com.leador.api.navi.enums.PathPlanningStrategy;
import com.leador.api.navi.model.LeadorLaneInfo;
import com.leador.api.navi.model.LeadorNaviCross;
import com.leador.api.navi.model.LeadorNaviLocation;
import com.leador.api.navi.model.LeadorNaviPath;
import com.leador.api.navi.model.LeadorNaviTrafficFacilityInfo;
import com.leador.api.navi.model.LeadorServiceFacilityInfo;
import com.leador.api.navi.model.NaviInfo;
import com.leador.api.navi.model.NaviLatLng;
import com.leador.api.navi.view.RouteOverLay;
import com.navifunctiontest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class ShowLeadorActivity extends Activity implements LeadorNaviListener {
    //copy代码
    private boolean calculateSuccess;
    RouteOverLay curRouteOverLay = null; //算路结果
    private MapView mapView;
    private MapController mMap;
    private LeadorNavi mMapNavi;
    List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    private MarkerOptions options1;
    private MarkerOptions options2;
    private TextView tvTime;
    private TextView tvLength;
    private Handler handler = null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Intent intent = getIntent();
        double slongtitude = intent.getDoubleExtra("slongtitude", 0);
        double slatitide = intent.getDoubleExtra("slatitide", 0);
        double elongtitude = intent.getDoubleExtra("elongtitude", 0);
        double elatitide = intent.getDoubleExtra("elatitide", 0);
        setContentView(R.layout.activity_pshowleador);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvLength = (TextView) findViewById(R.id.tv_length);
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        int allTime = (int) msg.obj;
                        tvTime.setText(toHours(allTime));
                        return true;
                    case 1:
                        int allLength = (int) msg.obj;
                        tvLength.setText(toLength(allLength));
                        return true;
                    default:
                        break;
                }
                return false;
            }
        });

        //copy代码
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mMap = mapView.getMap();
        mMapNavi = LeadorNavi.getInstance(getApplicationContext());
        mMapNavi.addLeadorNaviListener(this);
        mMapNavi.setRouteProtocol(1);
        startList.add(new NaviLatLng(slatitide, slongtitude));
        endList.add(new NaviLatLng(elatitide,elongtitude));
        options1 = new MarkerOptions();
        options2 = new MarkerOptions();
        options1.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.startpoint))).position(new LatLng(slatitide,slongtitude));
        options2.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.endpoint))).position(new LatLng(elatitide,elongtitude));
        //额外增加代码
        showLane();
    }

    /**
     * 转化为千米
     */
    private String toLength(int length) {
       double dis = Math.round(length / 100d) / 10d;
        return dis + " 公里";
    }

    /**
     * 转换成小时分钟
     * @param allTime
     */
    private String toHours(int allTime) {
             int day = allTime / 86400;
             int hour = (allTime - day*86400) / 3600;
             int minute = (allTime - day*86400 - hour*3600) / 60;
        if(day==0){
            if(hour==0){
                return minute + " 分钟";
            }else {
                return hour + " 小时 " + minute + " 分钟";
            }
        }else {
            if(hour==0){
                return day + " 天 " + minute + " 分钟";
            }else {
                return day + " 天 " + hour + " 小时 " + minute + " 分钟";
            }
        }
    }

    private void showLane() {
        int strategy = PathPlanningStrategy.DRIVING_DEFAULT;
        mMapNavi.calculateDriveRoute(startList, endList, wayList, strategy,false);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        mMapNavi.stopNavi();
        mMapNavi.destroy();
    }

    @Override
    public void onCalculateRouteSuccess() {
        //清除原有路线
        if(null != curRouteOverLay)
        {
            curRouteOverLay.removeFromMap();
        }

        //增加新的路线
        LeadorNaviPath path = mMapNavi.getNaviPath();
        // TODO: 2017/4/25 0025
//        tvTime.setText(path.getAllTime());
//        tvLength.setText(path.getAllLength());
        Message messageTime = Message.obtain();
        messageTime.what = 0;
        messageTime.obj = path.getAllTime();
        handler.sendMessage(messageTime);
        Message messageLength = Message.obtain();
        messageLength.what = 1;
        messageLength.obj = path.getAllLength();
        handler.sendMessage(messageLength);
        curRouteOverLay = new RouteOverLay(mMap, path, this);
        curRouteOverLay.addToMap();
        ArrayList<MarkerOptions> options = new ArrayList<>();
        options.add(options1);
        options.add(options2);
        mMap.addMarkers(options, true);

        //设置环境变量
        calculateSuccess = true;
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
        Log.e("ASDF", "onCalculateRouteFailure: "+errorInfo );
        Toast.makeText(this,"算路失败",Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(LeadorNaviLocation leadorNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }


    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onUpdateTrafficFacility(LeadorNaviTrafficFacilityInfo[] leadorNaviTrafficFacilityInfos) {

    }

    @Override
    public void showCross(LeadorNaviCross leadorNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showLaneInfo(LeadorLaneInfo[] leadorLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] ints) {

    }

    @Override
    public void updateServiceFacility(LeadorServiceFacilityInfo[] leadorServiceFacilityInfos) {

    }

    @Override
    public boolean isSpeeking() {
        return false;
    }

}
