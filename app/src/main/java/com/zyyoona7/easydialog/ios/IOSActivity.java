package com.zyyoona7.easydialog.ios;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.zyyoona7.custom.ActionSheetDialog;
import com.zyyoona7.easydialog.R;

public class IOSActivity extends AppCompatActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, IOSActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ios);

        Button actionSheetBtn = findViewById(R.id.btn_action_sheet);
        actionSheetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionSheetDialog dialog = ActionSheetDialog.newInstance()
                        .setItems("拍照","相册")
                        .setHighlightItems(true,0);
                dialog.show(getSupportFragmentManager());
            }
        });
    }
}
