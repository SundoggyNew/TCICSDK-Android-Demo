package com.tencent.tcic.h5;


import android.util.Log;

import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class LoginWebViewClient extends WebViewClient {
    private String TAG = "LoginWebViewClient";
    public interface LoadListener {
        void onPageFinished(WebView view, String url);
        void onLoadError(int code, String message);
    }
    private LoadListener listener;
    private boolean isFinished = false;     // 标记加载状态避免重复上抛

    public void setListener(LoadListener listener) {
        this.listener = listener;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if (this.listener != null && !this.isFinished) {
            this.isFinished = true;
            this.listener.onPageFinished(view, url);
        };
    }

    @Override
    public void onReceivedError(WebView webView, WebResourceRequest webResourceRequest, WebResourceError webResourceError) {
        super.onReceivedError(webView, webResourceRequest, webResourceError);
        Log.i(TAG, String.format("onReceivedError::enter=>%d, %s", webResourceError.getErrorCode(), webResourceError.getDescription().toString()));
        if (this.listener != null && webResourceRequest.isForMainFrame() && !this.isFinished) {
            this.isFinished = true;
            this.listener.onLoadError(webResourceError.getErrorCode(), webResourceError.getDescription().toString());
        };
    }

    @Override
    public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
        super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
        Log.i(TAG, String.format("onReceivedError::enter=>%d, %s", webResourceResponse.getStatusCode(), webResourceResponse.getReasonPhrase()));
        if (this.listener != null && webResourceRequest.isForMainFrame() && !this.isFinished) {
            this.isFinished = true;
            this.listener.onLoadError(webResourceResponse.getStatusCode(), webResourceResponse.getReasonPhrase());
        };
    }
}
