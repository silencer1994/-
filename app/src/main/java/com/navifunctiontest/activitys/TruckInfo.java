package com.navifunctiontest.activitys;

import android.app.Activity;
import android.os.Bundle;

import com.leador.api.maps.MapController;
import com.leador.api.maps.MapView;
import com.leador.api.maps.model.Marker;
import com.leador.api.navi.LeadorNavi;
import com.leador.api.navi.model.NaviLatLng;
import com.leador.api.navi.view.RouteOverLay;
import com.navifunctiontest.R;
import com.navifunctiontest.copycode.TTSController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/5/005.
 */
public class TruckInfo extends Activity {
    List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    private MapView mapView;
    private MapController mMap;
    private LeadorNavi mMapNavi;
    private boolean mapClickStartReady;
    private boolean mapClickEndReady;
    private Marker mStartMarker;
    private Marker mEndMarker;

    RouteOverLay curRouteOverLay = null; //算路结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_info);
    }
}
