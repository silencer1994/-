package com.navifunctiontest;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

//import com.leador.api.navi.LeadorNavi;
import com.navifunctiontest.activitys.CompareActivity;
import com.navifunctiontest.activitys.ShowLaneActivity;
import com.navifunctiontest.activitys.TruckActivity;
import com.navifunctiontest.copycode.FeatureView;

public class MainActivity extends ListActivity {
    static TelephonyManager localTelephonyManager;
    private static class DemoDetails {
        private final String titleId;
        private final String descriptionId;
        private final Class<? extends android.app.Activity> activityClass;

        public DemoDetails(String titleId, String descriptionId,
                           Class<? extends android.app.Activity> activityClass) {
            super();
            this.titleId = titleId;
            this.descriptionId = descriptionId;
            this.activityClass = activityClass;
        }
    }

    private static class CustomArrayAdapter extends ArrayAdapter<DemoDetails> {
        public CustomArrayAdapter(Context context, DemoDetails[] demos) {
            super(context, R.layout.feature, R.id.title, demos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FeatureView featureView;
            if (convertView instanceof FeatureView) {
                featureView = (FeatureView) convertView;
            } else {
                featureView = new FeatureView(getContext());
            }
            DemoDetails demo = getItem(position);
            featureView.setTitleId(demo.titleId);
            featureView.setDescriptionId(demo.descriptionId);
            return featureView;
        }
    }

//            (TelephonyManager)getApplication().getSystemService(Context.TELEPHONY_SERVICE);
    private static final DemoDetails[] demos = {
//            static TelephonyManager localTelephonyManager = (TelephonyManager)().getSystemService(Context.TELEPHONY_SERVICE);
            new DemoDetails("货车导航测试", "限重与限时限行测试", TruckActivity.class),
            new DemoDetails("高德车机对比测试","高德车机对比测试",CompareActivity.class),
            new DemoDetails("高德机车选点","张本兴点这里，自由选点", ShowLaneActivity.class)
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localTelephonyManager = (TelephonyManager)getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        Log.e("ASDF", "onCreate: "+localTelephonyManager.getDeviceId());
//        setTitle("导航功能测试"+ LeadorNavi.getVersion());
        ListAdapter adapter = new CustomArrayAdapter(
                this.getApplicationContext(), demos);
        setListAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        DemoDetails demo = (DemoDetails) getListAdapter().getItem(position);
        startActivity(new Intent(this.getApplicationContext(),
                demo.activityClass));
    }
}
