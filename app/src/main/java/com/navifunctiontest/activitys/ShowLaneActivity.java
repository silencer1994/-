package com.navifunctiontest.activitys;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import com.leador.api.services.core.LatLonPoint;
import com.leador.api.services.geocoder.GeocodeAddress;
import com.leador.api.services.geocoder.GeocodeQuery;
import com.leador.api.services.geocoder.GeocodeResult;
import com.leador.api.services.geocoder.GeocodeSearch;
import com.leador.api.services.geocoder.RegeocodeResult;
import com.navifunctiontest.APP;
import com.navifunctiontest.R;
import com.navifunctiontest.utils.DateUtils;
import com.navifunctiontest.utils.LMapUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/4/20 0020.
 */

public class ShowLaneActivity extends Activity implements LeadorNaviListener {
    //copycode
    NaviLatLng endLatlng = new NaviLatLng(39.955846, 116.352765);
    NaviLatLng startLatlng = new NaviLatLng(39.925041, 116.437901);
    NaviLatLng tranLatlng = new NaviLatLng(39.955846,116.352765);
    List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    private MapView mapView;
    private MapController mMap;
    private LeadorNavi mMapNavi;
    private boolean mapClickStartReady;
    private boolean mapClickEndReady;
    private boolean mapClickTransReady;
    private Marker mStartMarker;
    private Marker mEndMarker;
    private Marker mTransMarker;

    RouteOverLay curRouteOverLay = null; //算路结果
    private boolean calculateSuccess;
    private WifiManager wifiManager;
    Context context;

    public Handler handler;
    private TextView tvTime;
    private TextView tvLength;
    private boolean wifiClosed = false;
    private EditText etGeoName;
    private GeocodeSearch geocoderSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setContentView(R.layout.activity_showlane);
        setContentView(R.layout.activity_pshowlane);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvLength = (TextView) findViewById(R.id.tv_length);
        etGeoName = (EditText) findViewById(R.id.et_geo_name);
        geocoderSearch = new GeocodeSearch(this);
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
        context = getApplicationContext();
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //copycode
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        mMap = mapView.getMap();


        mMapNavi = LeadorNavi.getInstance(getApplicationContext());
        mMapNavi.addLeadorNaviListener(this);
        //设置网络超时
        mMapNavi.setSoTimeout(15000);
        initMarkers();
        initListener();
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
                if (mapClickTransReady) {
                    tranLatlng = new NaviLatLng(latLng.latitude, latLng.longitude);
                    mTransMarker.setPosition(latLng);
                    wayList.clear();
                    wayList.add(tranLatlng);
                }
                mapClickEndReady = false;
                mapClickStartReady = false;
                mapClickTransReady = false;
            }
        });
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int i) {

            }

            @Override
            public void onGeocodeSearched(GeocodeResult result, int i) {
                if (result != null && result.getGeocodeAddressList() != null
                        && result.getGeocodeAddressList().size() > 0) {
                    GeocodeAddress address = result.getGeocodeAddressList().get(0);
                    LatLonPoint latLonPoint = address.getLatLonPoint();
                    GeocodeQuery s =  result.getGeocodeQuery();
                    com.leador.api.services.core.LatLonPoint mLatLonPoint = new com.leador.api.services.core.LatLonPoint(latLonPoint.getLatitude(),latLonPoint.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                            LMapUtil.convertToLatLng(mLatLonPoint), 15));
                }
            }
        });
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
    /**
     *初始化marker
     */
    private void initMarkers() {
        mStartMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.startpoint))));
        mTransMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.waypoint))));
        mEndMarker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                        .decodeResource(getResources(), R.drawable.endpoint))));
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_start:
                Toast.makeText(this, "请在地图上点选起点", Toast.LENGTH_SHORT).show();
                mapClickStartReady = true;
                break;
            case R.id.btn_end:
                Toast.makeText(this, "请在地图上点选终点", Toast.LENGTH_SHORT).show();
                mapClickEndReady = true;
                break;
            case R.id.btn_tran:
                Toast.makeText(this, "请在地图上点途经点", Toast.LENGTH_SHORT).show();
                mapClickTransReady = true;
                break;
            case R.id.btn_clean_all:
                mMap.clear();
                startList.clear();
                wayList.clear();
                endList.clear();
                initMarkers();
                break;
            case R.id.btn_gaode:
//                wifiManager.setWifiEnabled(false);
                sendMessage();
                break;
            case R.id.btn_leador:
                calculateRoute();
//                if(isNetworkAvailable(getApplicationContext())){
//                }else{
//                    wifiManager.setWifiEnabled(true);
//                    Toast.makeText(getApplicationContext(),"正在开启wifi,连接后再次点击进行算路",Toast.LENGTH_SHORT).show();
//                }
                break;
            case R.id.btn_save:
                wirteAppend(APP.getInstance(),"起点:"+startList.toString()+"-终点:"+endList.toString()+"-途经点:"+wayList.toString()+"-时间:"+ DateUtils.getTodayDateTime(),"GaoDeCheJi.txt");
//                Log.e("ASDF","起点:"+startList.toString()+"-终点:"+endList.toString()+"-途经点:"+wayList.toString() );
                break;
            case R.id.btn_geo:
                if(etGeoName.getText().length()==0){
                    getLatlon(etGeoName.getHint().toString());
                }else{
                    getLatlon(etGeoName.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    /**
     * 响应地理编码
     */
    public void getLatlon(final String name) {
//        showDialog();
        GeocodeQuery query = new GeocodeQuery(name,"010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼。
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }
//    /**
//     * 显示进度条对话框
//     */
//    public void showDialog() {
//        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        progDialog.setIndeterminate(false);
//        progDialog.setCancelable(true);
//        progDialog.setMessage("正在获取地址");
//        progDialog.show();
//    }

    /**
     * 追加文件
     * @param context
     * @param msg
     */
    public static void wirteAppend(Context context,String msg,String uri){
//        String path = PackageUtils.getApplicationCacheDirectory(context) + "/";
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "GaoDeJiChe";

        File f = new File(path);
        if(!f.exists()){
            f.mkdirs();
        }

        String path1 = path + "/" + uri;
        File file = new File(path1);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter filerWriter = new FileWriter(file, true);
            BufferedWriter bufWriter = new BufferedWriter(filerWriter);
            bufWriter.write(msg);
            bufWriter.newLine();
            bufWriter.close();
            filerWriter.close();
            Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 目录不存在则创建
     * @param file
     * @return
     */
    boolean isFolderExists(File file) {
        if (!file.exists()) {
            if (file.mkdirs()) {
                return true;
            } else {
                return false;

            }
        }
        return true;

    }
    //发送高德启动广播
    private void sendMessage() {
        Intent intent = new Intent();
        intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
        intent.putExtra("KEY_TYPE", 10007);
        intent.putExtra("EXTRA_SNAME", "北京大学");
        intent.putExtra("EXTRA_SLON",startLatlng.getLongitude());
        intent.putExtra("EXTRA_SLAT",startLatlng.getLatitude());
        intent.putExtra("EXTRA_DNAME","复旦大学");
        intent.putExtra("EXTRA_DLON",endLatlng.getLongitude());
        intent.putExtra("EXTRA_DLAT",endLatlng.getLatitude());
        intent.putExtra("EXTRA_DEV",0);
        intent.putExtra("EXTRA_M",0);
        sendBroadcast(intent);

        //                    /高德地图车镜版本（后视镜）使用该包名/
//                            String pkgName = "com.autonavi.amapautolite";
//                    /高德地图车机版本 使用该包名/

        String pkgName = "com.autonavi.amapauto";
        Intent launchIntent = new Intent();
        launchIntent.setComponent(
                new ComponentName(pkgName,
                        "com.autonavi.auto.remote.fill.UsbFillActivity"));
        startActivity(launchIntent);
    }

    //算路逻辑
    private void calculateRoute() {
        int strategy = PathPlanningStrategy.DRIVING_DEFAULT;
        mMapNavi.setRouteProtocol(1);
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
//        //清除控制点
//        startList.clear();
//        endList.clear();
//        wayList.clear();
        //清除原有路线
        if(null != curRouteOverLay)
        {
            curRouteOverLay.removeFromMap();
        }
        //增加新的路线
        LeadorNaviPath path = mMapNavi.getNaviPath();
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

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

}
