package com.navifunctiontest.activitys;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.leador.api.maps.CameraUpdateFactory;
import com.leador.api.maps.MapController;
import com.leador.api.maps.MapView;
import com.leador.api.maps.model.BitmapDescriptorFactory;
import com.leador.api.maps.model.LatLng;
import com.leador.api.maps.model.Marker;
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
import com.navifunctiontest.copycode.SimpleNaviActivity;
import com.navifunctiontest.copycode.TTSController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/5/005.
 */
public class TruckActivity extends Activity implements LeadorNaviListener {
    NaviLatLng endLatlng = new NaviLatLng(39.955846, 116.352765);
    NaviLatLng startLatlng = new NaviLatLng(39.925041, 116.437901);

    List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    private MapView mapView;
    private MapController mMap;
    private LeadorNavi mMapNavi;
    private TTSController ttsManager;
    private boolean mapClickStartReady;
    private boolean mapClickEndReady;
    private Marker mStartMarker;
    private Marker mEndMarker;

    RouteOverLay curRouteOverLay = null; //算路结果

    //自己的代码
    private EditText edCarWord;
    private EditText edCarHeight;
    private EditText edCarWeight;
    private ToggleButton tglHeight;
    private ToggleButton tglTime;
    private ToggleButton tglCarType;
    private RelativeLayout rlNormal;
    private LinearLayout llTruckInfo;
    private boolean mapClickTransitReady;
    private NaviLatLng transitLatlng;
    private Marker mTransitMarker;

    //Popupwindows
    private Button myButton;
    List<String> items=new ArrayList<String>();
    PopupWindow popupWindow;
    private ArrayAdapter<String> adapter;
    private View.OnClickListener btLis = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            initPopWindow();
        }};
    private AdapterView.OnItemClickListener lvLis = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            switch (items.get(position).toString()){
                case "限高避让":
                    //换一个起点
                    wayList.clear();
                    mMap.clear();
                    startLatlng = new NaviLatLng(40.04877,116.58864);
                    endLatlng = new NaviLatLng(40.05024,116.58496);
                    calculateRoute(null);
//                Looper.loop();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "路桥限高3米", Toast.LENGTH_SHORT).show();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.049480,116.586922),16));
                        }
                    });

                    break;
                case "限高不避让":
                    wayList.clear();
                    mMap.clear();
                    startLatlng = new NaviLatLng(40.049480,116.586922);
//                endLatlng = new NaviLatLng(116.58496,40.05024);
                    endLatlng = new NaviLatLng(40.05024,116.58496);
                    calculateRoute(null);
//                Looper.loop();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "路桥限高3米", Toast.LENGTH_SHORT).show();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.049480,116.586922),16));
                        }
                    });
                    break;
                case "限重避让":
                    wayList.clear();
                    mMap.clear();
                    startLatlng = new NaviLatLng(39.9272,116.64725);
//                  //                endLatlng = new NaviLatLng(116.64979,39.93203);
                    endLatlng = new NaviLatLng(39.93203,116.64979);
                    calculateRoute(null);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "路桥限重30吨", Toast.LENGTH_SHORT).show();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.93203,116.64979),16));
                        }
                    });
                    break;
                case "限重不避让":
                    wayList.clear();
                    mMap.clear();;
//                startLatlng = new NaviLatLng(116.64725,39.9272);
                startLatlng = new NaviLatLng(39.9272,116.64725);
                //                endLatlng = new NaviLatLng(116.64925,39.93097);
                endLatlng = new NaviLatLng(39.93097,116.64925);

                calculateRoute(null);
                runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "路桥限重30吨", Toast.LENGTH_SHORT).show();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(39.93203,116.64979),16));
                        }
                    });
                    break;
                default:
                break;
            }

        }
    };
    private void initPopWindow(){

        View contentView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.popupwindow, null);
        contentView.setBackgroundColor(Color.BLACK);

        if (popupWindow == null) {
            popupWindow = new PopupWindow(findViewById(R.id.mainLayout), 300, 500);
            popupWindow.setContentView(contentView);
            ListView listView = (ListView) contentView.findViewById(R.id.list);
            adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, items);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(lvLis);
        }
        //获得popupwindow外的焦点
        ColorDrawable cd = new ColorDrawable(0000);
        popupWindow.setBackgroundDrawable(cd);

        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(myButton);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_gps_navi_layout);

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        mMap = mapView.getMap();

        mMapNavi = LeadorNavi.getInstance(getApplicationContext());
        mMapNavi.addLeadorNaviListener(this);


        //设置网络超时
        mMapNavi.setSoTimeout(15000);

        ttsManager = TTSController.getInstance(getApplicationContext());
        ttsManager.init();
        ttsManager.startSpeaking();
        mStartMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.startpoint))));
        mEndMarker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.endpoint))));
        mTransitMarker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.waypoint))));


        mMap.setOnMapClickListener(new MapController.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if (mapClickStartReady) {
                    startLatlng = new NaviLatLng(latLng.latitude, latLng.longitude);
                    mStartMarker.setPosition(latLng);
                    startList.clear();
                    startList.add(startLatlng);
                }

                if (mapClickEndReady) {
                    endLatlng = new NaviLatLng(latLng.latitude, latLng.longitude);
                    mEndMarker.setPosition(latLng);
                    endList.clear();
                    endList.add(endLatlng);
                }

                if(mapClickTransitReady){
                    transitLatlng = new NaviLatLng(latLng.latitude, latLng.longitude);
                    mTransitMarker.setPosition(latLng);
                    wayList.clear();
                    wayList.add(transitLatlng);
                }

                mapClickEndReady = false;
                mapClickStartReady = false;
                mapClickTransitReady = false;
            }
        });

        initViews();
        addDatas();
    }

    /**
     * 添加限高限重的测试数据
     */
    private void addDatas() {
        items.add("限高避让");
        items.add("限高不避让");
        items.add("限重避让");
        items.add("限重不避让");
    }

    /**
     * 自己写的控件初始化
     */
    private void initViews() {
        edCarHeight = (EditText) findViewById(R.id.ed_car_height);
        edCarWeight = (EditText) findViewById(R.id.ed_car_weight);
        edCarWord = (EditText) findViewById(R.id.ed_car_word);
        tglHeight = (ToggleButton) findViewById(R.id.tglWeight);
        tglTime = (ToggleButton) findViewById(R.id.tglTime);
        tglCarType = (ToggleButton) findViewById(R.id.tglCarType);
        rlNormal = (RelativeLayout) findViewById(R.id.rl_normal);
        llTruckInfo = (LinearLayout) findViewById(R.id.ll_truck_info);
        myButton = (Button)findViewById(R.id.avoidHW);
        myButton.setOnClickListener(btLis);
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
        ttsManager.destroy();
        mMapNavi.destroy();
    }


    public void calculateRoute(View view) {
        startList.clear();
        endList.clear();
        wayList.clear();
        startList.add(startLatlng);
        endList.add(endLatlng);
//        wayList.add(transitLatlng);
        int strategy = PathPlanningStrategy.DRIVING_DEFAULT;

        //自己添加的代码
        //设置货车算路
        //TODO
        int success = mMapNavi.setRouteCalcMode(isTruck());
        Log.e("ASDF", "calculateRoute: "+success);
        if(tglHeight.isChecked()){
//        setTruckInfomation(float height, float weight, float length, float width)
//        设置车高,重,长,宽,用于网络限行算路
            float height = Float.valueOf(edCarHeight.getText().toString());
            float weight = Float.valueOf(edCarWeight.getText().toString());
            mMapNavi.setTruckInfomation(height,weight,0,0);
        }else{
            mMapNavi.setTruckInfomation(0,0,0,0);
        }
//        int setLicensePlateNumber(java.lang.String number,int isTruckType)
//        number - 车牌号
//        isTruckType - 车型,0小车,1货车
        mMapNavi.setLicensePlateNumber(edCarWord.getText().toString(),isTruck());
        //TODO
//        mMapNavi.getLinkLimitStatus();
//        参数:
//        departureTime - 预计出行时间
//        返回:
//        0设置失败, 1设置成功
        if(tglTime.isChecked()){
            int setDepartureTime = mMapNavi.setDepartureTime(1493622000);
            Log.e("ASDF", "预计出行时间: "+setDepartureTime );
        }else{
            int setDepartureTime = mMapNavi.setDepartureTime(1493596800);
            Log.e("ASDF", "预计出行时间: "+setDepartureTime );
        }
        //TODO
        if(wayList.size()>0){
//            Log.e("ASDF", "wayList: "+wayList.get(0).getLongitude() +"   "+wayList.get(0).getLatitude());
        }
        mMapNavi.calculateDriveRoute(startList, endList, wayList, strategy,false);
    }

    /**
     * 是否为货车算路
     * @return 1为货车算路,0为小车算路
     */
    public int isTruck(){
        if(tglCarType.isChecked()){
            return 1;
        }else {
            return 0;
        }
    }
    private boolean calculateSuccess;

    @Override
    public void onCalculateMultipleRoutesSuccess(int[] routeIds) {

    }

    public void setTruckInfoOver(View view){
        llTruckInfo.setVisibility(View.GONE);
        rlNormal.setVisibility(View.VISIBLE);
    }

    public void setTruckInfo(View view){
//        Intent intent = new Intent(this,TruckInfo.class);
//        startActivity(intent);
        rlNormal.setVisibility(View.GONE);
        llTruckInfo.setVisibility(View.VISIBLE);
    }

    public void goToActivity(View view) {
        if (calculateSuccess) {
            //SimpleNaviActivity非常简单，就是startNavi而已（因为导航道路已在这个activity生成好）
            Intent intent = new Intent(this, SimpleNaviActivity.class);
//            boolean isGpsMode=view.getId()==R.id.start_sim_navi?false:true;
            boolean isGpsMode=view.getId()==R.id.start_sim_navi?true:false;
            intent.putExtra("GPS",isGpsMode);
            startActivity(intent);
        } else {
            Toast.makeText(this, "请先算路", Toast.LENGTH_SHORT).show();
        }
    }

    public void chooseStart(View view) {
        Toast.makeText(this, "请在地图上点选起点", Toast.LENGTH_SHORT).show();
        mapClickStartReady = true;
    }
    public void chooseTransit(View view){
        Toast.makeText(this, "请在地图上点选途经点", Toast.LENGTH_SHORT).show();
        mapClickTransitReady = true;
    }

    public void chooseEnd(View view) {
        Toast.makeText(this, "请在地图上点选终点", Toast.LENGTH_SHORT).show();
        mapClickEndReady = true;
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int type) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(LeadorNaviLocation location) {

    }

    @Override
    public void onGetNavigationText(int type, String text) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteSuccess() {
        //清除控制点
        startList.clear();
        endList.clear();
        wayList.clear();

        //清除原有路线
        if(null != curRouteOverLay)
        {
            curRouteOverLay.removeFromMap();
        }

        //增加新的路线
        LeadorNaviPath path = mMapNavi.getNaviPath();
        curRouteOverLay = new RouteOverLay(mMap, path, this);
        curRouteOverLay.addToMap();

        //设置环境变量
        calculateSuccess = true;
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {
        Log.e("ASDF", "onCalculateRouteFailure: "+errorInfo );
        Toast.makeText(this,"算路失败",Toast.LENGTH_SHORT).show();
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
         return ttsManager.isSpeeking();
    }

    @Override
    public void onBackPressed() {
        if(llTruckInfo.getVisibility()==View.VISIBLE){
            setTruckInfoOver(null);
        }else{
            super.onBackPressed();
        }
    }
}
