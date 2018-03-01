package com.navifunctiontest.copycode;

import android.app.Activity;
import android.os.Bundle;

import com.leador.api.navi.LeadorNavi;
import com.leador.api.navi.LeadorNaviListener;
import com.leador.api.navi.LeadorNaviView;
import com.leador.api.navi.LeadorNaviViewListener;
import com.leador.api.navi.LeadorNaviViewOptions;
import com.leador.api.navi.model.LeadorLaneInfo;
import com.leador.api.navi.model.LeadorNaviCross;
import com.leador.api.navi.model.LeadorNaviLocation;
import com.leador.api.navi.model.LeadorNaviTrafficFacilityInfo;
import com.leador.api.navi.model.LeadorServiceFacilityInfo;
import com.leador.api.navi.model.NaviInfo;
import com.navifunctiontest.R;


/**
 * 类说明: 简单的导航界面。
 */

public class SimpleNaviActivity extends Activity implements LeadorNaviListener,LeadorNaviViewListener {

    private LeadorNaviView mLeadorMapNaviView;
    private LeadorNavi mLeadorMapNavi;
    private TTSController mTtsManager;

    private boolean startGpsNavi =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic_navi);
        mLeadorMapNaviView = (LeadorNaviView) findViewById(R.id.navi_view);
        mLeadorMapNaviView.onCreate(savedInstanceState);

        mTtsManager = TTSController.getInstance(getApplicationContext());
        mTtsManager.startSpeaking();

        mLeadorMapNavi = LeadorNavi.getInstance(getApplicationContext());
        mLeadorMapNavi.addLeadorNaviListener(this);
        mLeadorMapNavi.addLeadorNaviListener(mTtsManager);
        mLeadorMapNaviView.setMapNaviViewListener(this);

        mLeadorMapNaviView.getViewOptions().setNaviViewTopic(LeadorNaviViewOptions.PINK_COLOR_TOPIC);

        mLeadorMapNavi.startNavi(getIntent().getBooleanExtra("GPS",false)? LeadorNavi.GPSNaviMode: LeadorNavi.EmulatorNaviMode);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLeadorMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLeadorMapNaviView.onPause();
        mTtsManager.stopSpeaking();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLeadorMapNaviView.onDestroy();
        mLeadorMapNavi.stopNavi();
    }


    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {
        if(i == LeadorNavi.GPSNaviMode)
        {
            startGpsNavi = true;
        }
        else
        {
            startGpsNavi = false;
        }
    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(LeadorNaviLocation mapNaviLocation) {

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
    public void onCalculateRouteSuccess() {
        if(startGpsNavi)
            mLeadorMapNavi.startNavi(LeadorNavi.GPSNaviMode);
    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int wayID) {

    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onUpdateTrafficFacility(LeadorNaviTrafficFacilityInfo[] leadorNaviTrafficFacilityInfos) {

    }



    @Override
    public void showLaneInfo(LeadorLaneInfo[] laneInfos, byte[] laneBackgroundInfo, byte[] laneRecommendedInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] routeIds) {

    }

    @Override
    public void showCross(LeadorNaviCross leadorNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void updateServiceFacility(LeadorServiceFacilityInfo[] leadorServiceFacilityInfos) {

    }

    @Override
    public boolean isSpeeking() {
        return mTtsManager.isSpeeking();
    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {
        finish();
    }

    @Override
    public boolean onNaviBackClick() {
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public boolean onReRouteForTraffic() {
        return false;
    }
}
