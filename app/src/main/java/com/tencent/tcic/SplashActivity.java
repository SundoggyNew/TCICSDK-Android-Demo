package com.tencent.tcic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.play.core.splitinstall.SplitInstallManager;
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory;
import com.google.android.play.core.splitinstall.SplitInstallRequest;
import com.google.android.play.core.splitinstall.SplitInstallSessionState;
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener;
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus;
import com.tencent.tcic.h5.H5LoginActivity;
import com.tencent.tcic.app.R;
import com.tencent.tcic.h5.utils.FileUtil;

import java.io.File;

/**
 * 功能描述：闪屏页
 *
 * @author eric
 */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    private int mySessionId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        loadAndLaunchDynamicFeatureModule("com.tencent.tcic.dynamicfeature");
        loadX5Core();
    }

    private void loadAndLaunchDynamicFeatureModule(String name) {
        // Skip loading if the module already is installed. Perform success action directly.
        SplitInstallManager splitInstallManager =
                SplitInstallManagerFactory.create(this);
        if (splitInstallManager.getInstalledModules().contains(name)) {
            return;
        }

        SplitInstallRequest request = SplitInstallRequest.newBuilder()
                .addModule("dynamicfeature")
                .build();

        // 设置DynamicFeature安装监听器
        splitInstallManager.registerListener(new SplitInstallStateUpdatedListener() {
            @Override
            public void onStateUpdate(SplitInstallSessionState state) {
                if (state.sessionId() == mySessionId) {
                    switch (state.status()) {
                        case SplitInstallSessionStatus.DOWNLOADING:
                            Log.i(TAG, "tbs onStateUpdate: DOWNLOADING");
                            break;
                        case SplitInstallSessionStatus.INSTALLING:
                            Log.i(TAG, "tbs onStateUpdate: INSTALLING");
                            break;
                        case SplitInstallSessionStatus.INSTALLED:
                            Log.i(TAG, "tbs onStateUpdate: INSTALLED");

                            loadX5Core();
                            break;
                        case SplitInstallSessionStatus.FAILED:
                            Log.i(TAG, "tbs onStateUpdate: FAILED");
                            break;
                    }
                }
            }
        });
        // 开始DynamicFeature模块下载
        splitInstallManager.startInstall(request)
                .addOnSuccessListener(sessionId -> {
                    // DynamicFeature链接建立完成
                    mySessionId = sessionId;
                    Log.i(TAG, "tbs addOnSuccessListener: " + sessionId);
                })
                .addOnFailureListener(exception -> {
                });
    }
    private void loadX5Core() {
        // 加载X5内核
        File core = FileUtil.copyAssetsToSDCard(SplashActivity.this, "tbs_core_046471_20230726172002_nolog_fs_obfs_arm64-v8a_release.tbs", "tbs", "tbs.apk");
        TCICManager.getInstance().initX5Core("your_license_key", new TBSSdkManageCallback() {
            @Override
            public void onCoreInitFinished() {

            }

            @Override
            public void onViewInitFinished(boolean isX5Core) {
                Log.i(TAG, "onViewInitFinished" + isX5Core);
                TCICManager.getInstance().runOnMainThread(() -> {
                    startActivity(new Intent(SplashActivity.this, H5LoginActivity.class));
                    finish();
                });
            }
        }, core, 46471);
    }
}
