package com.example.administrator.circlearoundpro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    CustomView customView_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customView_id = (CustomView) findViewById(R.id.customView_id);

        customView_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView_id.startAnimation();
            }
        });
    }
}
