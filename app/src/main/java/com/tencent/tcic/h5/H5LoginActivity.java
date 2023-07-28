package com.tencent.tcic.h5;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.tcic.ChangeUrlActivity;
import com.tencent.tcic.TCICClassConfig;
import com.tencent.tcic.TCICConstants;
import com.tencent.tcic.app.R;
import com.tencent.tcic.pages.TCICClassActivity;
import com.tencent.tcic.util.Utils;
import com.tencent.tcic.widgets.TCICWebViewManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class H5LoginActivity extends AppCompatActivity implements LoginJSBridge.LoginListener, LoginWebViewClient.LoadListener, ShakeUtils.OnShakeListener {
    private static final String TAG = "H5LoginActivity";
    private WebView mLoginWebView;
    private FrameLayout loginContainer;
    private String testLoginUrl = "https://class.qcloudclass.com/1.8.0/login.html?nativeversion=" + TCICConstants.CORE_VERSION;
    // private String testLoginUrl = "http://192.168.3.26:8080/login.html";
    private LoginJSBridge jsBridge;
    private ShakeUtils mShakeUtils;
    private String mHtmlUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_h5_login);

        SharedPreferences preferences = getSharedPreferences("App_Config", Context.MODE_PRIVATE);
        mHtmlUrl = preferences.getString("htmlUrl", "");
        if (!TextUtils.isEmpty(mHtmlUrl)) {
            testLoginUrl = mHtmlUrl + "/login.html?nativeversion=" + TCICConstants.CORE_VERSION;
        }

        loginContainer = (FrameLayout) findViewById(R.id.login_container);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

        mLoginWebView = new WebView(this);
        mLoginWebView.setBackgroundColor(Color.BLACK);
        loginContainer.addView(mLoginWebView, layoutParams);

        initWebSettings();
        loadLoginView();

        mShakeUtils = new ShakeUtils(H5LoginActivity.this);
    }

    @Override
    public void onShake(double speed) {
        Log.d("onShake", "onShake speed:" + speed);
        if (speed > 900) {
            Intent intent = new Intent(H5LoginActivity.this,
                    ChangeUrlActivity.class);
            startActivity(intent);
        }
    }

    private void initWebSettings() {
        WebSettings webSettings = mLoginWebView.getSettings();
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(0);
        }

        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setTextZoom(100);
        webSettings.setAppCacheMaxSize(10485760L);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(false);
        webSettings.setSavePassword(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        if (Build.VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    private void loadLoginView() {
        jsBridge = new LoginJSBridge(this);
        mLoginWebView.addJavascriptInterface(jsBridge, "loginNative");
        mLoginWebView.loadUrl(testLoginUrl);
        LoginWebViewClient client = new LoginWebViewClient();
        client.setListener(this);
        mLoginWebView.setWebViewClient(client);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mShakeUtils != null) {
            mShakeUtils.bindShakeListener(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mShakeUtils != null) {
            mShakeUtils.unBindShakeListener(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseContainer();
    }

    private void releaseContainer() {
        if (mLoginWebView != null) {
            mLoginWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mLoginWebView.clearHistory();

            ((ViewGroup) mLoginWebView.getParent()).removeView(mLoginWebView);
            mLoginWebView.destroy();
            mLoginWebView = null;
        }
    }

    @Override
    public void onJoinClassBySign(String sign, String env, String lng, String classSubType) {
        Log.w(TAG, "onJoinClassBySign sign: " + sign + " env: " + env + " lng: " + lng + " subType: " + classSubType);
        TCICWebViewManager.getInstance().setClassLanuage(this, env, lng);
        Intent intent = new Intent(H5LoginActivity.this,
                TCICClassActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        TCICClassConfig initConfig = new TCICClassConfig.Builder()
                .deviceType(Utils.isTablet(H5LoginActivity.this)
                        ? TCICConstants.DEVICE_TABLET
                        : TCICConstants.DEVICE_PHONE)
                .sign(sign)
                .preferPortrait(false)
                .coreEnv(env)
                .build();
        bundle.putParcelable(TCICConstants.KEY_INIT_CONFIG, initConfig);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onJoinClass(String schoolId, String classId, String userId, String token, String env, String lng, String classSubType, int camera, int mic, int speaker, int defaultDeviceOrientation) {
        Log.w(TAG, "onJoinClass schoolId: " + schoolId + " classId:" + classId + " userId: " + userId + " token:" + token + " env:" + env + " lng: " + lng + " subType: " + classSubType + " camera: " + camera + " mic: " + mic + " speaker: " + speaker + " defaultDeviceOrientation: " + defaultDeviceOrientation);
        TCICWebViewManager.getInstance().setClassLanuage(this, env, lng);
        if (!TextUtils.isEmpty(mHtmlUrl)) {
            TCICWebViewManager.getInstance().setHtmlUrl(mHtmlUrl);
        }
        Intent intent = new Intent(H5LoginActivity.this,
                TCICClassActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        Bundle bundle = new Bundle();
        TCICClassConfig initConfig = new TCICClassConfig.Builder()
                .schoolId(Integer.parseInt(schoolId))
                .classId(Long.parseLong(classId))
                .userId(userId)
                .deviceType(Utils.isTablet(this)
                        ? TCICConstants.DEVICE_TABLET
                        : TCICConstants.DEVICE_PHONE)
                .token(token)
                .preferPortrait(defaultDeviceOrientation != 0)
                .coreEnv(env)
                .camera(camera)
                .mic(mic)
                .speaker(speaker)
                .lng(lng)
                .build();
        bundle.putParcelable(TCICConstants.KEY_INIT_CONFIG, initConfig);

        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onCloseLoginView() {
        releaseContainer();
        finish();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.i(TAG, "onPageFinished: " + url);
    }

    @Override
    public void onLoadError(int code, String message) {
        Log.w(TAG, "onLoadError: " + code + ", " + message);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.login_error);
        builder.setMessage(String.format(getString(R.string.login_error_msg), code, message));
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
