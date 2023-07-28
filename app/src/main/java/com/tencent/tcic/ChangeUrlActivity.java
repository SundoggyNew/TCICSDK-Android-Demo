package com.tencent.tcic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tencent.tcic.app.R;

public class ChangeUrlActivity extends AppCompatActivity {

    private EditText changeUrlET;
    private Button okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_url);

        changeUrlET = findViewById(R.id.et_url);
        okBtn = findViewById(R.id.btn_ok);

        okBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(String.valueOf(changeUrlET.getText()))) {
                    SharedPreferences preferences = getSharedPreferences("App_Config", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("htmlUrl", String.valueOf(changeUrlET.getText()));
                    editor.commit();
                    System.exit(0);
                }
            }
        });
    }
}