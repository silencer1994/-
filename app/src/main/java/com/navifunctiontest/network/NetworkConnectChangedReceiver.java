package com.navifunctiontest.network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.navifunctiontest.APP;

/**
 * 网络改变监控广播
 * <p>
 * 监听网络的改变状态,只有在用户操作网络连接开关(wifi,mobile)的时候接受广播,
 * 然后对相应的界面进行相应的操作，并将 状态 保存在我们的APP里面
 * <p>
 * <p>
 * Created by xujun
 */
public class NetworkConnectChangedReceiver extends BroadcastReceiver {

//    private static final String TAG = "xujun";
//    public static final String TAG1 = "xxx";

    @Override
    public void onReceive(Context context, Intent intent) {
//        if(intent.getAction().equals(WifiManager.RSSI_CHANGED_ACTION)){
//            //signal strength changed
//        }
//        else if(intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){//wifi连接上与否
//            System.out.println("网络状态改变");
//            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
//            if(info.getState().equals(NetworkInfo.State.DISCONNECTED)){
//                System.out.println("wifi网络连接断开");
//                Toast.makeText(APP.getInstance(), "wifi网络连接断开", Toast.LENGTH_SHORT).show();
//            }
////            else if(info.getState().equals(NetworkInfo.State.CONNECTED)){
////
////                WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
////                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
////                //获取当前wifi名称
////                System.out.println("连接到网络 " + wifiInfo.getSSID());
////
////            }
//
//        }
//        else
            if(intent.getAction().equals(WifiManager.WIFI_STATE_CHANGED_ACTION)){//wifi打开与否
            int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
            if(wifistate == WifiManager.WIFI_STATE_DISABLED){
//                System.out.println("系统关闭wifi");
                Toast.makeText(APP.getInstance(), "wifi已关闭,可以切换到高德机车", Toast.LENGTH_SHORT).show();
            }
//            else if(wifistate == WifiManager.WIFI_STATE_ENABLED){
//                System.out.println("系统wifi开启");
//            }
        }
    }


}