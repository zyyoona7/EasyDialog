package com.zyyoona7.easydialog.custom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import com.zyyoona7.easydialog.R;

public class CustomActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, CustomActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
        AppCompatButton button = findViewById(R.id.btn_google_pd);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GProgressDialog.newInstance()
                        .show(getSupportFragmentManager());
            }
        });
    }
}
