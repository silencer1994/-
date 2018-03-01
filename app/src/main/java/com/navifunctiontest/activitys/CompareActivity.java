package com.navifunctiontest.activitys;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.navifunctiontest.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/19 0019.
 */

public class CompareActivity extends Activity {
    private EditText etLatitude;
    private EditText etLongtitude;
    private EditText etElatitude;
    private EditText etElongtitude;
    private ListView lsDatas;
    private ListAdapter adapter;
    private List<String> datas;
    private AdapterView.OnItemClickListener itemListener;
    private Button btnChange;
    private boolean isGaoDe = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_compare);
        context = getApplicationContext();
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        initViews();
    }


    private void initViews() {
//        etLatitude = (EditText) findViewById(R.id.et_slatitude);
//        etLongtitude = (EditText) findViewById(R.id.et_slongtitude);
//        etElatitude = (EditText) findViewById(R.id.et_elatitude);
//        etElongtitude = (EditText) findViewById(R.id.et_elongtitude);
        lsDatas = (ListView) findViewById(R.id.ls_datas);
        btnChange = (Button) findViewById(R.id.btn_change_mode);

        datas = new ArrayList<String>();
        //北京市
        datas.add("1劣,116.36032,39.8736,116.4901,39.93628");
        datas.add("2等,116.26694,40.01121,116.56631,39.73613");
        datas.add("3优,116.30745,39.85357,116.65627,40.09036");
        datas.add("4等,116.18729,40.16565,116.97006,39.81845");
        datas.add("5劣,116.85059,40.46659,116.01288,39.14795");
        datas.add("6等,117.16919,39.10108,116.30676,40.77513");
        datas.add("7差不多,116.78467,40.13857,115.91125,39.45062");
        datas.add("8劣,116.38367,39.69324,116.36719,40.10413");
        datas.add("9劣,116.716,40.19818,116.44958,39.52777");
        datas.add("10劣,115.72449,39.79883,117.09503,39.71014");
        //再来6组进一点

        //跨省
        datas.add("11小劣,108.06152,31.18085,116.45508,31.03034");
        datas.add("12劣,113.77441,34.61151,114.43359,30.43731");
        //跨市
        datas.add("13劣,116.62262,40.12513,115.45876,38.85008");
        datas.add("14劣,108.95966,34.20135,109.54742,34.5052");

        //市内短途
        datas.add("15等,116.46355,39.85242,116.46262,39.86466");
        datas.add("16优,116.39585,39.9041,116.45645,39.84739");
        datas.add("17异,116.31294,39.84092,116.31844,39.98364");
        datas.add("18大劣,116.53198,40.10833,116.21887,39.92743");
        datas.add("19等,116.35345,40.11253,116.33148,39.86632");
        datas.add("20异,116.60065,39.79672,116.17767,39.79461");

        //测试时间禁行
        datas.add("北礼士路7~20,116.352275,39.93938,116.352259,39.938862");
        datas.add("车公庄西向东7~20,116.332726,39.932349,116.331835,39.932406");
        datas.add("平安北里7~22,116.358492,39.932299,116.357935,39.932439");
        datas.add("安德路7~20,116.395315,39.952149,116.396437,39.951512");
        datas.add("南门仓胡同7~22,116.425875,39.929654,116.426712,39.929255");
        datas.add("朝阳门南小街7~20,116.426398,39.921563,116.426294,39.921491");
        datas.add("朝阳北路7~20,116.483348,39.922717,116.484732,39.923622");
        datas.add("文津街7~20,116.381934,39.922577,116.382084,39.922675");
        datas.add("和平里西街7~20,116.418683,39.956898,116.418571,39.956578");
        datas.add("京密路7~20,116.478564,39.980732,116.479465,39.981237");




        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,datas);
        lsDatas.setAdapter(adapter);
        itemListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = datas.get(position);
                String[] latlongs = data.split(",");
                double slongtitude = Double.valueOf(latlongs[1]);
                double slatitide = Double.valueOf(latlongs[2]);
                double elongtitude = Double.valueOf(latlongs[3]);
                double elatitide = Double.valueOf(latlongs[4]);
                if(!isGaoDe){
                    Intent startIntent = new Intent(CompareActivity.this,ShowLeadorActivity.class);
                    startIntent.putExtra("slongtitude",slongtitude);
                    startIntent.putExtra("slatitide",slatitide);
                    startIntent.putExtra("elongtitude",elongtitude);
                    startIntent.putExtra("elatitide",elatitide);
                    startActivity(startIntent);
                }else{
                    Intent intent = new Intent();
                    intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
                    intent.putExtra("KEY_TYPE", 10007);
//                    intent.putExtra("EXTRA_M", 0);
                    intent.putExtra("EXTRA_SNAME", "北京大学");
                    intent.putExtra("EXTRA_SLON",slongtitude);
                    intent.putExtra("EXTRA_SLAT",slatitide);
                    intent.putExtra("EXTRA_DNAME","复旦大学");
                    intent.putExtra("EXTRA_DLON",elongtitude);
                    intent.putExtra("EXTRA_DLAT",elatitide);
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
//
            }
        };
        lsDatas.setOnItemClickListener(itemListener);
    }

    private WifiManager wifiManager;
    Context context;
    public void onClick(View view) {
//        double slatitide = Double.valueOf(etLatitude.getText().toString());
//        double slongtitude = Double.valueOf(etLongtitude.getText().toString());
//        double elatitide = Double.valueOf(etElatitude.getText().toString());
//        double elongtitude = Double.valueOf(etElongtitude.getText().toString());
//        Intent intent = new Intent();
//        intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
//        intent.putExtra("KEY_TYPE", 10007);
//        intent.putExtra("EXTRA_SNAME", "北京大学");
//        intent.putExtra("EXTRA_SLON",slongtitude);
//        intent.putExtra("EXTRA_SLAT",slatitide);
//        intent.putExtra("EXTRA_DNAME","复旦大学");
//        intent.putExtra("EXTRA_DLON",elongtitude);
//        intent.putExtra("EXTRA_DLAT",elatitide);
//        intent.putExtra("EXTRA_DEV",0);
//        intent.putExtra("EXTRA_M",0);
//        sendBroadcast(intent);
        switch (view.getId()){
            case R.id.btn_change_mode:
                if (isGaoDe){
//                    wifiManager.setWifiEnabled(true);
                    btnChange.setText("立得算路(点击切换为高德算路)");
                }else {
//                    wifiManager.setWifiEnabled(false);
                    btnChange.setText("高德算路(点击切换为立得算路)");
                }
                isGaoDe = !isGaoDe;
                break;
//            case R.id.btn_close_wifi:
//                isGaoDe = true;
//                wifiManager.setWifiEnabled(false);
//                break;
//            case R.id.btn_start_wifi:
//                isGaoDe = false;
//                wifiManager.setWifiEnabled(true);
//                break;
            default:
                break;
        }

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
//    //发送高德启动广播
//    private void sendMessage() {
//        Intent intent = new Intent();
//        intent.setAction("AUTONAVI_STANDARD_BROADCAST_RECV");
//        intent.putExtra("KEY_TYPE", 10007);
//        intent.putExtra("EXTRA_SNAME", "北京大学");
//        intent.putExtra("EXTRA_SLON",startLatlng.getLongitude());
//        intent.putExtra("EXTRA_SLAT",startLatlng.getLatitude());
//        intent.putExtra("EXTRA_DNAME","复旦大学");
//        intent.putExtra("EXTRA_DLON",endLatlng.getLongitude());
//        intent.putExtra("EXTRA_DLAT",endLatlng.getLatitude());
//        intent.putExtra("EXTRA_DEV",0);
//        intent.putExtra("EXTRA_M",0);
//        sendBroadcast(intent);
//    }
}
