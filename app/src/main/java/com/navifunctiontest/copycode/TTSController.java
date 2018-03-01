package com.navifunctiontest.copycode;

import android.content.Context;
import android.os.Bundle;


import com.iflytek.cloud.speech.SpeechConstant;
import com.iflytek.cloud.speech.SpeechError;
import com.iflytek.cloud.speech.SpeechListener;
import com.iflytek.cloud.speech.SpeechSynthesizer;
import com.iflytek.cloud.speech.SpeechUser;
import com.iflytek.cloud.speech.SynthesizerListener;
import com.leador.api.navi.LeadorNaviListener;
import com.leador.api.navi.model.LeadorLaneInfo;
import com.leador.api.navi.model.LeadorNaviCross;
import com.leador.api.navi.model.LeadorNaviLocation;
import com.leador.api.navi.model.LeadorNaviTrafficFacilityInfo;
import com.leador.api.navi.model.LeadorServiceFacilityInfo;
import com.leador.api.navi.model.NaviInfo;
import com.navifunctiontest.R;

/**
 * 语音播报组件
 */
public class TTSController implements SynthesizerListener, LeadorNaviListener {

    public static TTSController ttsManager;
    boolean isfinish = true;
    private Context mContext;

    // 合成对象.
    private SpeechSynthesizer mSpeechSynthesizer;
    /**
     * 用户登录回调监听器.
     */
    private SpeechListener listener = new SpeechListener() {

        @Override
        public void onData(byte[] arg0) {
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error != null) {

            }
        }

        @Override
        public void onEvent(int arg0, Bundle arg1) {
        }
    };

    TTSController(Context context) {
        mContext = context;
    }

    public static TTSController getInstance(Context context) {
        if (ttsManager == null) {
            ttsManager = new TTSController(context);
        }
        return ttsManager;
    }

    public void init() {
        SpeechUser.getUser().login(mContext, null, null,
                "appid=" + mContext.getString(R.string.app_id), listener);
        // 初始化合成对象.
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext);
        initSpeechSynthesizer();
    }

    /**
     * 使用SpeechSynthesizer合成语音，不弹出合成Dialog.
     *
     * @param
     */
    public void playText(String playText) {
        if (!isfinish) {
            return;
        }
        if (null == mSpeechSynthesizer) {
            // 创建合成对象.
            mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(mContext);
            initSpeechSynthesizer();
        }
        // 进行语音合成.
        mSpeechSynthesizer.startSpeaking(playText, this);
    }

    public void stopSpeaking() {
        if (mSpeechSynthesizer != null)
            mSpeechSynthesizer.stopSpeaking();
    }

    public void startSpeaking() {
        isfinish = true;
    }

    private void initSpeechSynthesizer() {
        // 设置发音人
        mSpeechSynthesizer.setParameter(SpeechConstant.VOICE_NAME,
                mContext.getString(R.string.preference_default_tts_role));
        // 设置语速
        mSpeechSynthesizer.setParameter(SpeechConstant.SPEED,
                "" + mContext.getString(R.string.preference_key_tts_speed));
        // 设置音量
        mSpeechSynthesizer.setParameter(SpeechConstant.VOLUME,
                "" + mContext.getString(R.string.preference_key_tts_volume));
        // 设置语调
        mSpeechSynthesizer.setParameter(SpeechConstant.PITCH,
                "" + mContext.getString(R.string.preference_key_tts_pitch));

    }

    @Override
    public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCompleted(SpeechError arg0) {
        // TODO Auto-generated method stub
        isfinish = true;
    }

    @Override
    public void onSpeakBegin() {
        // TODO Auto-generated method stub
        isfinish = false;

    }

    @Override
    public void onSpeakPaused() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSpeakProgress(int arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSpeakResumed() {
        // TODO Auto-generated method stub

    }

    public void destroy() {
        if (mSpeechSynthesizer != null) {
            mSpeechSynthesizer.stopSpeaking();
        }
    }

    @Override
    public void onArriveDestination() {
        // TODO Auto-generated method stub
        this.playText("到达目的地");
    }

    @Override
    public void onArrivedWayPoint(int wayID) {
        // TODO Auto-generated method stub
        this.playText("到达第" + wayID + "个途径点");
    }

    @Override
    public void onCalculateRouteFailure(int arg0) {
        this.playText("路径计算失败，请检查网络或输入参数");
    }

    @Override
    public void onCalculateRouteSuccess() {
        String calculateResult = "路径计算就绪";

        this.playText(calculateResult);
    }

    @Override
    public void onEndEmulatorNavi() {
        this.playText("模拟导航结束");

    }

    /**
     * 播放语音内容
     * @param arg0 语音类型
     * @param arg1 语音内容
     */
    @Override
    public void onGetNavigationText(int arg0, String arg1) {
        // TODO Auto-generated method stub
        this.playText(arg1);
    }

    @Override
    public void onInitNaviFailure() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onInitNaviSuccess() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLocationChange(LeadorNaviLocation arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReCalculateRouteForYaw() {

        this.playText("您已偏航");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onStartNavi(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onGpsOpenStatus(boolean enabled) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

        // TODO Auto-generated method stub

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
        return mSpeechSynthesizer.isSpeaking();
    }
}
