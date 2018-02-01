package com.hangaji2017.hangaji_final;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView iv_background = findViewById(R.id.background);
        iv_background.setBackgroundResource(R.drawable.background);
        Log.d("_test", "Glide OK");
        Log.d("_test", getKeyHash());

        // 6.0이상의 운영체제 권한을 체크.
        checkPermission();
    } // onCreate()

    public String getKeyHash() {

        try {

            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            if (packageInfo == null)
                return null;

            for (Signature signature : packageInfo.signatures) {
                try {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
                } catch (NoSuchAlgorithmException e) {
                    Log.w("_test", "Unable to get MessageDigest. signature=" + signature, e);
                }
            }
            return null;

        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }

    }


    // 안드로이드 os 6.0(마쉬멜로우) 이상의 운영체제에 대해서 사용자에게 권한요청 처리함.
    private void checkPermission() {
        Log.d("_test", "checkPermission() ");

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 권한이 없을 경우
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    // 사용자가 임의로 권한을 취소시킨 경우
                    // 권한 재요청
//                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 119);
                } else {
                    // 권한 요청 (최초 요청)
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, // wifi 기반 위치
                            Manifest.permission.ACCESS_COARSE_LOCATION, // 기지국 기반 위치
                            Manifest.permission.ACCESS_WIFI_STATE, // 기지국 기반 위치
                            Manifest.permission.ACCESS_NETWORK_STATE, // 기지국 기반 위치
                            Manifest.permission.READ_EXTERNAL_STORAGE, // 기지국 기반 위치
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, // 기지국 기반 위치
                            Manifest.permission.CALL_PHONE, // 기지국 기반 위치
                            Manifest.permission.INTERNET, // 기지국 기반 위치
                    }, 119); // new String[] 무명 배열 선언 / anonymous array
                } // else
            } else {
                // 사용자에게 수락받은경우
                start_loading();
            } // else
        } else {
            // 마쉬멜로우(6) 보다 낮은 운영체제는 사용자에게 허락받을필요없음
            start_loading();
        } // else
    } // private void checkPermission

    void start_loading() {
        new loading().execute("");
    }

    void startActivity() {
        // Intent intent = new Intent(getBaseContext(),MainActivity.class);
        // startActivity(intent);
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("_test", "onRequestPermissionsResult() ");
        // wifi 기반 위치 퍼미션 거부된 경우
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//            finish();
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            gsDialog.setTitle("위치 권한 설정");
            gsDialog.setMessage("위치 관련 권한을 설정해주지 않으시면 어플리케이션 사용이 불가능합니다. 관련 권한을 허용해주세요.");
            gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
//                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    intent.addCategory(Intent.CATEGORY_DEFAULT);
//                    startActivity(intent);
                    startActivity(new Intent(getApplicationContext(), Splash.class));
                }
            })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            System.gc();
                            moveTaskToBack(true);
                            finish();
                            android.os.Process.killProcess(android.os.Process.myPid());
                            return;
                        }
                    }).create().show();
            return;
        }
        start_loading();

    }


    class loading extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            startActivity();
        }
    }


}
