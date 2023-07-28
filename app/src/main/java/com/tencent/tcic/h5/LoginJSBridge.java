package com.tencent.tcic.h5;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.tencent.tcic.TCICManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * created by zyh
 * on 2022/1/20
 */
public class LoginJSBridge {
    static private String TAG = "LoginJSBridge";
    public interface LoginListener {
        void onJoinClassBySign(String sign, String params, String lng, String classSubType);

        void onJoinClass(String schoolId, String classId, String userId, String token, String env, String lng, String classSubType, int camera, int mic, int speaker, int defaultDeviceOrientation);
    
        void onCloseLoginView();
    }

    private LoginListener listener;

    public LoginJSBridge(LoginListener listener) {
        this.listener = listener;
    }

    @JavascriptInterface
    public void joinClassBySign(String data) {
        Log.i(TAG, "joinClassBySign=>" + data);

        TCICManager.getInstance().runOnMainThread(() -> {
            try {
                JSONObject jsonData = new JSONObject(data);
                if (listener != null) {
                    listener.onJoinClassBySign(
                            jsonData.getString("sign"),
                            jsonData.optString("env", ""),
                            jsonData.optString("lng", "zh"),
                            jsonData.optString("classSubType"));
                }
            } catch (JSONException e) {
                Log.i(TAG, e.toString());
            }
        });
    }

    @JavascriptInterface
    public void joinClass(String data) {
        Log.i(TAG, "joinClass=>" + data);

        TCICManager.getInstance().runOnMainThread(() -> {
            try {
                JSONObject jsonData = new JSONObject(data);
                if (listener != null) {
                    listener.onJoinClass(
                            jsonData.getString("schoolid"),
                            jsonData.getString("classid"),
                            jsonData.getString("userid"),
                            jsonData.getString("token"),
                            jsonData.optString("env", ""),
                            jsonData.optString("lng", "zh"),
                            jsonData.optString("classSubType"),
                            jsonData.optInt("camera", 1),
                            jsonData.optInt("mic", 1),
                            jsonData.optInt("speaker", 1),
                            jsonData.optInt("defaultDeviceOrientation", 0));
                }
            } catch (JSONException e) {
                Log.i(TAG, e.toString());
            }
        });
    }

    @JavascriptInterface
    public void closeLoginView(){
        if(listener!=null){
            listener.onCloseLoginView();
        }
    }
}
