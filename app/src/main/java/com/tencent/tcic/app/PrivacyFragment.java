package com.tencent.tcic.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import androidx.fragment.app.Fragment;

public class PrivacyFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_privacy, container, false);
        WebView webView = root.findViewById(R.id.privacy_webview);
        webView.loadUrl("https://www.qq.com/privacy.htm");
        return root;
    }
}
