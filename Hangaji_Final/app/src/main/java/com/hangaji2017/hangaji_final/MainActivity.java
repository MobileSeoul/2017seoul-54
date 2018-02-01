package com.hangaji2017.hangaji_final;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        changeMain();

        Button btn = (Button)findViewById(R.id.start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainScreenActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {

    }

    // changeMain() : 시간에 따라 메인화면을 교체하는 메서드
    public void changeMain() {
        long CurTime = System.currentTimeMillis();
        Date date = new Date(CurTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        String formatDate  = simpleDateFormat.format(date);
        int intTime = Integer.parseInt(formatDate);

        linearLayout = (LinearLayout) findViewById(R.id.backgroundLayout);
        startBtn = (Button) findViewById(R.id.start);

        if (intTime < 18 && intTime > 6) {
            linearLayout.setBackgroundResource(R.drawable.main_day);
            startBtn.setBackgroundResource(R.drawable.btn_main_day);
        }
        else {
            linearLayout.setBackgroundResource(R.drawable.main_night);
            startBtn.setBackgroundResource(R.drawable.btn_main_night);
        }
    } // changeMain()
}
