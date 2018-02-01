package com.hangaji2017.hangaji_final;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by sky on 2017-09-07.
 */

public class MainScreenActivity extends AppCompatActivity {

    RelativeLayout relativeLayout;
    HorizontalScrollView hScroll;
    // 각 한강공원의 위치 정보 (위, 경도 좌표값)

    public static final int GANGSEO = 0;
    public static final int YANGHWA = 1;
    public static final int SUNYOUDO = 2;
    public static final int NANJI = 3;
    public static final int YEOUIDO = 4;
    public static final int MANGWON = 5;
    public static final int ICHON = 6;
    public static final int BANPO = 7;
    public static final int JAMWON = 8;
    public static final int TTUKSEOM = 9;
    public static final int JAMSIL = 10;
    public static final int GWANGNARU = 11;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hangangmap);
        Toast.makeText(this, "공원 이름을 터치하시면\n지도를 확인할 수 있어요!", Toast.LENGTH_SHORT).show();
        changeMap();

//        Toast.makeText(this, "좌우로 스크롤을 넘겨보세요!", Toast.LENGTH_SHORT).show();

    } // protected void onCreate()

    // goDetail() : 공원 이름을 터치했을 때 해당 공원의 디테일(지도) 화면으로 갈 수 있는 메소드
    public void goDetail(View view) {
        switch(view.getId()) {
            case R.id.gangseo:
                Intent gangseoIntent = new Intent(getApplicationContext(), DaumMapActivity.class);
                gangseoIntent.putExtra("Han_Code", GANGSEO);
                startActivity(gangseoIntent);
                break;
            case R.id.yanghwa:
                Intent yanghwaIntent = new Intent(getApplicationContext(), DaumMapActivity.class);
                yanghwaIntent.putExtra("Han_Code", YANGHWA);
                startActivity(yanghwaIntent);
                break;
            case R.id.sunyoudo:
                Intent sunyoudoIntent = new Intent(getApplicationContext(), DaumMapActivity.class);
                sunyoudoIntent.putExtra("Han_Code", SUNYOUDO);
                startActivity(sunyoudoIntent);
                break;
            case R.id.nanji:
                Intent nanjiIntent = new Intent(getApplicationContext(), DaumMapActivity.class);
                nanjiIntent.putExtra("Han_Code", NANJI);
                startActivity(nanjiIntent);
                break;
            case R.id.yeouido:
                Intent yeouidoIntent = new Intent(getApplicationContext(), DaumMapActivity.class);
                yeouidoIntent.putExtra("Han_Code", YEOUIDO);
                startActivity(yeouidoIntent);
                break;
            case R.id.mangwon:
                Intent mangwonIntent = new Intent(getApplicationContext(), DaumMapActivity.class);
                mangwonIntent.putExtra("Han_Code", MANGWON);
                startActivity(mangwonIntent);
                break;
            case R.id.ichon:
                Intent ichonIntent = new Intent(getApplicationContext(), DaumMapActivity.class);
                ichonIntent.putExtra("Han_Code", ICHON);
                startActivity(ichonIntent);
                break;
            case R.id.banpo:
                Intent banpoIntent = new Intent(getApplicationContext(), DaumMapActivity.class);
                banpoIntent.putExtra("Han_Code", BANPO);
                startActivity(banpoIntent);
                break;
            case R.id.jamwon:
                Intent jamwonIntent = new Intent(getApplicationContext(), DaumMapActivity.class);
                jamwonIntent.putExtra("Han_Code", JAMWON);
                startActivity(jamwonIntent);
                break;
            case R.id.ttukseom:
                Intent ttukseomIntent = new Intent(getApplicationContext(), DaumMapActivity.class);
                ttukseomIntent.putExtra("Han_Code", TTUKSEOM);
                startActivity(ttukseomIntent);
                break;
            case R.id.jamsil:
                Intent jamsilIntent = new Intent(getApplicationContext(), DaumMapActivity.class);
                jamsilIntent.putExtra("Han_Code", JAMSIL);
                startActivity(jamsilIntent);
                break;
            case R.id.gwangnaru:
                Intent gwangnaruIntent = new Intent(getApplicationContext(), DaumMapActivity.class);
                gwangnaruIntent.putExtra("Han_Code", GWANGNARU);
                startActivity(gwangnaruIntent);
                break;
        }
    } //goDetail

    int intTime;
    final String WEATHER_URL = "http://www.kma.go.kr/wid/queryDFSRSS.jsp?zone=1156054000";

    // changeMap() : 시간에 따라 한강지도를 낮과 밤으로 이미지를 바꾸는 메소드
    private void changeMap()
    {
        long CurTime = System.currentTimeMillis();
        Date date = new Date(CurTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        String formatDate = simpleDateFormat.format(date);

        intTime = Integer.parseInt(formatDate); //아래의 날씨정보에서도 사용하기 위해 intTime을 119번 줄에 선언했어!

        relativeLayout = (RelativeLayout) findViewById(R.id.mapback);

        GetXMLTask task = new GetXMLTask(this);
        task.execute(WEATHER_URL);

        if ( 6 <= intTime && intTime < 18 ) { //새벽 6시 ~ 오후 6시 사이
            relativeLayout.setBackgroundResource(R.drawable.push_hangangmap_day);
        }
        else { //오후 6시 ~ 새벽 6시 사이
            relativeLayout.setBackgroundResource(R.drawable.hangang_night);
        }
    } // changeMap()

    @Override
    public void onBackPressed() {
        // AlertDialog(Builder) 객체 생성
        AlertDialog.Builder finishDialog = new AlertDialog.Builder(this);
        // AlertDialog(Builder) 메인 메세지 설정
        finishDialog.setMessage("한강가자, 지금 앱을 종료하시겠습니까?");
        // AlertDialog(Builder) 긍정 버튼 설정
        finishDialog.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
//                        어플리케이션 종료
                System.gc();
                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
//                        Intent intent = new Intent(Intent.ACTION_MAIN);
//                        intent.addCategory(Intent.CATEGORY_HOME);
//                        startActivity(intent);
            }
        });

        // AlertDialog(Builder) 부정 버튼 설정
        finishDialog.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 메세지 창 취소
                dialog.cancel();
            }
        });
        // AlertDialog(Builder) 활성화
        finishDialog.show();
    }

    ImageView weatherView;
    Document doc = null;

    // private inner class extending AsyncTask
    @SuppressLint("NewApi")
    private class GetXMLTask extends AsyncTask<String, Void, Document> {
        private Activity context;

        public GetXMLTask(Activity context) {
            this.context = context;
        }

        @Override
        protected Document doInBackground(String... urls) {

            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db;

                db = dbf.newDocumentBuilder();
                doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Parsing Error",
                        Toast.LENGTH_SHORT).show();
            }
            return doc;
        }


        int checkTime(){
            int weatherTime = -1;

            if (intTime < 3) weatherTime = 0;
            else if (intTime < 6) weatherTime = 1;
            else if (intTime < 9) weatherTime = 2;
            else if (intTime < 12) weatherTime = 3;
            else if (intTime < 15) weatherTime = 4;
            else if (intTime < 18) weatherTime = 5;
            else if (intTime < 21) weatherTime = 6;
            else if (intTime < 24) weatherTime = 7;

            return weatherTime;
        }

        @Override
        protected void onPostExecute(Document doc) {

            NodeList nodeList = doc.getElementsByTagName("data");

            Node node = nodeList.item(checkTime());
            NodeList test = node.getChildNodes();
            Element fstElmnt = (Element) node;

            NodeList weatherList = fstElmnt.getElementsByTagName("wfKor");
            Element weatherName = (Element) weatherList.item(0);
            weatherList = weatherName.getChildNodes();

            weatherView = (ImageView) findViewById(R.id.weatherView);

            switch (((Node) weatherList.item(0)).getNodeValue()){
                case "맑음":
                    if ( 6 <= intTime && intTime< 18 ) { //새벽 6시 ~ 오후 6시 사이
                        weatherView.setImageResource(R.drawable.day_nice);
                    }
                    else { //오후 6시 ~ 새벽 6시 사이
                        weatherView.setImageResource(R.drawable.night_nice);
                    }
                    break;
                case "구름 조금":
                    if ( 6 <= intTime && intTime< 18 ) { //새벽 6시 ~ 오후 6시 사이
                        weatherView.setImageResource(R.drawable.day_normal);
                    }
                    else { //오후 6시 ~ 새벽 6시 사이
                        weatherView.setImageResource(R.drawable.night_normal);
                    }
                    break;
                case "구름 많음":
                case "흐림":
                    if ( 6 <= intTime && intTime< 18 ) { //새벽 6시 ~ 오후 6시 사이
                        weatherView.setImageResource(R.drawable.day_cloud);
                    }
                    else { //오후 6시 ~ 새벽 6시 사이
                        weatherView.setImageResource(R.drawable.night_cloud);
                    }
                    break;
                case "비":
                case "눈/비":
                    if ( 6 <= intTime && intTime< 18 ) { //새벽 6시 ~ 오후 6시 사이
                        weatherView.setImageResource(R.drawable.day_rain);
                    }
                    else { //오후 6시 ~ 새벽 6시 사이
                        weatherView.setImageResource(R.drawable.night_rain);
                    }
                    break;
                case "눈":
                    if ( 6 <= intTime && intTime< 18 ) { //새벽 6시 ~ 오후 6시 사이
                        weatherView.setImageResource(R.drawable.day_rain);
                    }
                    else { //오후 6시 ~ 새벽 6시 사이
                        weatherView.setImageResource(R.drawable.night_rain);
                    }
                    break;
            }
        }
    }// 날씨정보 Task Class

}