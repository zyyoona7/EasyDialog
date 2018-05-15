package com.zyyoona7.easydialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.zyyoona7.easydialog.custom.CustomActivity;
import com.zyyoona7.easydialog.ios.IOSActivity;
import com.zyyoona7.easydialog.normal.NormalActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppCompatButton btnNormal = findViewById(R.id.btn_normal);
        AppCompatButton btnIOS = findViewById(R.id.btn_ios);
        AppCompatButton btnCustom = findViewById(R.id.btn_custom);

        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalActivity.start(MainActivity.this);
            }
        });

        btnIOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IOSActivity.start(MainActivity.this);
            }
        });

        btnCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomActivity.start(MainActivity.this);
            }
        });
    }
}
