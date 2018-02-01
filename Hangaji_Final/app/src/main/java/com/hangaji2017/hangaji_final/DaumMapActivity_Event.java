package com.hangaji2017.hangaji_final;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.go.seoul.airquality.AirQualityButtonTypeB;


public class DaumMapActivity_Event extends AppCompatActivity implements
        MapView.CurrentLocationEventListener,
        MapView.MapViewEventListener,
        MapView.POIItemEventListener, MapView.OpenAPIKeyAuthenticationResultListener {

    ToggleButton toiletButton, drinkButton, marketButton;
    ToggleButton locationButton;

    public static final int TOILET_MARKER = 101;
    public static final int MARKET_MARKER = 102;
    public static final int DRINK_MARKER = 103;

    public static String[] dongNames = {"상암동", "방화동", "당산동", "망원동", "여의도동", "반포동", "이촌동", "잠원동", "자양동", "잠실동", "암사동"};

    private static final double[] D_GANGSEO = {37.5745578,126.8442906}; // 강서
    private static final double[] D_YANGHWA = {37.5412865, 126.8959282}; // 양화
    private static final double[] D_NANJI = {37.5658649,126.8744585}; // 난지

    private static final double[] D_MANGWON = {37.5565886, 126.8915714}; // 망원
    private static final double[] D_SUNYOUDO = {37.5437267, 126.8974939}; // 선유도
    private static final double[] D_YEOUIDO = {37.5273289, 126.9327715}; // 여의도

    private static final double[] D_ICHON = {37.5159576, 126.9736436}; // 이촌
    private static final double[] D_BANPO = {37.5102642, 126.9939751}; // 반포
    private static final double[] D_JAMSIL = {37.5179526, 127.0826227}; // 잠원

    private static final double[] D_TTUKSEOM = {37.5290966, 127.0691617}; // 뚝섬
    private static final double[] D_JAMWON = {37.5272903, 127.016528}; // 잠실
    private static final double[] D_GWANGNARU = {37.5499839, 127.119434}; // 광나루

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

    private static double[] getDoubleLocation = null;

    MapPOIItem[] toiletItems, marketItems, drinkItems;
    ArrayList<MapPOIItem> toiletList, marketList, drinkList;

    public static boolean isToiletParseEnd;
    public static boolean isDrinkParseEnd;
    public static boolean isMarketParseEnd;


    // Integer getDoubleLocation = Manager.getInstance().getLocation();

    net.daum.mf.map.api.MapView mapView;
    String daumApiKey = "6c48a99cc822234281cffccb62c39b40";
//    String daumApiKey = "4986015b4b8837eadf4543df093c0ba6";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("_test", "MainActivity_Event::onCreate()");
        setContentView(R.layout.activity_main);

        isToiletParseEnd = false;
        isDrinkParseEnd = false;
        isMarketParseEnd = false;

        toiletList = new ArrayList<MapPOIItem>();
        marketList = new ArrayList<MapPOIItem>();
        drinkList = new ArrayList<MapPOIItem>();

        mapView = new MapView(this);
        mapView.setDaumMapApiKey(daumApiKey);
//        mapView.setOpenAPIKeyAuthenticationResultListener();

        mapView.setCurrentLocationEventListener(this);
        mapView.setMapViewEventListener(this);
        mapView.setPOIItemEventListener(this);

        final ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.daum_map_view);
        mapViewContainer.addView(mapView);

        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);

        //홈 버튼에 홈으로 가는 기능 연결
        Button homeBtn = (Button) findViewById(R.id.home);
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        }); //home.onClick()

        // TODO: 2017-10-26 대기 이미지
//        대기 API 버튼 기능
        AirQualityButtonTypeB typeB = (AirQualityButtonTypeB) findViewById(R.id.airButton);
        typeB.setButtonImage(R.drawable.air_unpressed);
        typeB.setOpenAPIKey("68536672626d6f6e313031746564754d");


        Intent getIntent = getIntent();
        Bundle bundle = getIntent.getExtras();
        int code = bundle.getInt("Han_Code");
        if (bundle != null) {
            switch (code) {
                case GANGSEO:
                    changePark(R.drawable.gangseo_day, R.drawable.gangseo_night);
                    getDoubleLocation = D_GANGSEO;
                    break;
                case YANGHWA:
                    changePark(R.drawable.yanghwa_day, R.drawable.yanghwa_night);
                    getDoubleLocation = D_YANGHWA;
                    break;
                case SUNYOUDO:
                    changePark(R.drawable.sunyoudo_day, R.drawable.sunyoudo_night);
                    getDoubleLocation = D_SUNYOUDO;
                    break;
                case NANJI:
                    changePark(R.drawable.nanji_day, R.drawable.nanji_night);
                    getDoubleLocation = D_NANJI;
                    break;
                case YEOUIDO:
                    changePark(R.drawable.yeouido_day, R.drawable.yeouido_night);
                    getDoubleLocation = D_YEOUIDO;
                    break;
                case MANGWON:
                    changePark(R.drawable.mangwon_day, R.drawable.mangwon_night);
                    getDoubleLocation = D_MANGWON;
                    break;
                case ICHON:
                    changePark(R.drawable.ichon_day, R.drawable.ichon_night);
                    getDoubleLocation = D_ICHON;
                    break;
                case BANPO:
                    changePark(R.drawable.banpo_day, R.drawable.banpo_night);
                    getDoubleLocation = D_BANPO;
                    break;
                case JAMWON:
                    changePark(R.drawable.jamwon_day, R.drawable.jamwon_night);
                    getDoubleLocation = D_JAMWON;
                    break;
                case TTUKSEOM:
                    changePark(R.drawable.ttukseom_day, R.drawable.ttukseom_night);
                    getDoubleLocation = D_TTUKSEOM;
                    break;
                case JAMSIL:
                    changePark(R.drawable.jamsil_day, R.drawable.jamsil_night);
                    getDoubleLocation = D_JAMSIL;
                    break;
                case GWANGNARU:
                    changePark(R.drawable.gwangnaru_day, R.drawable.gwangnaru_night);
                    getDoubleLocation = D_GWANGNARU;
                    break;
            }
        }

        mapView.setCustomCurrentLocationMarkerTrackingImage(R.drawable.currentlocationmarker, new MapPOIItem.ImageOffset(20, 0));
        locationButton = (ToggleButton) findViewById(R.id.locationButton);
        locationButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    chkGpsService();
//                    mapView.setCustomCurrentLocationMarkerImage(R.drawable.currentlocationmarker, new MapPOIItem.ImageOffset(20, 0));
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading);
                    mapView.setShowCurrentLocationMarker(true);
                    locationButton.setBackgroundResource(R.drawable.location_pressed);
                } // true
                else {
                    mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff);
                    mapView.setShowCurrentLocationMarker(false);
                    locationButton.setBackgroundResource(R.drawable.location_unpressed);
                } // false
            }
        });

        toiletButton = (ToggleButton) findViewById(R.id.toiletToggleButton);
        toiletButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isToiletParseEnd) {
                    if (b == true) {
                        toiletButton.setBackgroundResource(R.drawable.toilet_pressed);
                        mapView.addPOIItems(toiletItems);
                    } // true
                    else {
                        toiletButton.setBackgroundResource(R.drawable.toilet_unpressed);
                        mapView.removePOIItems(toiletItems);
                    } // false
                } // isToiletParseEnd
            }
        });

        drinkButton = (ToggleButton) findViewById(R.id.drinkToggleButton);
        drinkButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isDrinkParseEnd) {
                    if (b == true) {
                        drinkButton.setBackgroundResource(R.drawable.drink_pressed);
                        mapView.addPOIItems(drinkItems);
                    } // true
                    else {
                        drinkButton.setBackgroundResource(R.drawable.drink_unpressed);
                        mapView.removePOIItems(drinkItems);
                    } // false
                } // isToiletParseEnd
            }
        });

        marketButton = (ToggleButton) findViewById(R.id.marketToggleButton);
        marketButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (isMarketParseEnd) {
                    if (b == true) {
                        marketButton.setBackgroundResource(R.drawable.market_pressed);
                        mapView.addPOIItems(marketItems);
                    } // true
                    else {
                        marketButton.setBackgroundResource(R.drawable.market_unpressed);
                        mapView.removePOIItems(marketItems);
                    } // false
                } // isToiletParseEnd
            }
        });


        ToiletParser toiletParser = new ToiletParser();
        toiletParser.execute();

        DrinkParser drinkParser = new DrinkParser();
        drinkParser.execute();

        MarketParser marketParser = new MarketParser();
        marketParser.execute();

    } // onCreate()

    public void parser(String facility) {
        Log.d("_test", "parser");
        String jsonString = null;

        try {
            //json파일을 String으로 변환한 후, 다시 json객체로 변환
            InputStream is = getAssets().open(facility + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        StringBuffer sb = null;

        try {
            sb = new StringBuffer();


            JSONObject originJSON = new JSONObject(jsonString);
            JSONArray dataArray = originJSON.getJSONArray("DATA");
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject data = dataArray.getJSONObject(i);

                String name = data.getString("GIGU");
                Double lat = data.getDouble("LAT"); //위도
                Double lng = data.getDouble("LNG"); //경도

                //sb.append("기구: " + name + " 위도: " + lat + " 경도: " + lng + "\n");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            //textView.setText(sb.toString());
        }
    }


    public void parser(String facility, String dong) {
        String jsonString = null;

        try {
            //json파일을 String으로 변환한 후, 다시 json객체로 변환
            InputStream is = getAssets().open(facility + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        StringBuffer sb = null;

        try {
            sb = new StringBuffer();

            JSONObject toiletJSON = new JSONObject(jsonString);
            JSONArray dataArray = toiletJSON.getJSONArray("DATA");

            for (int j = 0; j < 11; j++) {

                dong = dongNames[j];
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject data = dataArray.getJSONObject(i);
                    if (data.getString("HNR_NAM").equals(dong) == false) continue;
                    Double lat = data.getDouble("LAT"); //위도
                    Double lng = data.getDouble("LNG"); //경도

                    MapPOIItem _temp = new MapPOIItem();
                    _temp.setItemName(dong);
                    _temp.setTag(TOILET_MARKER);
                    _temp.setMapPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
                    _temp.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    _temp.setCustomImageResourceId(R.drawable.toilet_day_light);
                    _temp.setCustomImageAutoscale(true);
                    _temp.setCustomImageAnchor(0.5f, 1.0f);
                    toiletList.add(_temp);
                    int _test = 0;
                    Log.d("_test", Integer.toString(++_test));
                }
            }
            //Log.v("test",sb.toString());
        } catch (JSONException e) {
            //e.printStackTrace();
        } finally {
            toiletItems = new MapPOIItem[toiletList.size()];
            for (int i = 0; i < toiletList.size(); i++) {
                toiletItems[i] = toiletList.get(i);
            }
        }
    }

    // changePark() : 시간에 따라 공원 이미지를 변경하는 메소드
    private void changePark(int day, int night) {

        ImageView park = (ImageView) findViewById(R.id.park);

        long CurTime = System.currentTimeMillis();
        Date date = new Date(CurTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH");
        String formatDate = simpleDateFormat.format(date);

        int intTime = Integer.parseInt(formatDate);

        if (6 < intTime && intTime < 18) { //새벽 6시 ~ 오후 6시 사이
            park.setBackgroundResource(day);
        } else { //오후 6시 ~ 새벽 6시 사이
            park.setBackgroundResource(night);
        }
    } // changePark()

    private boolean chkGpsService() {
        Log.d("_test", "chkGpsService");
        String gps = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
        if (!(gps.matches(".*gps.*") && gps.matches(".*network.*"))) {

            // GPS OFF 일때 Dialog 표시
            AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
            gsDialog.setTitle("위치 서비스 설정");
            gsDialog.setMessage("무선 네트워크 사용, GPS 위성 사용을\n모두 체크하셔야 정확한 위치 서비스가 가능합니다.\n위치 서비스 기능을 설정하시겠습니까?");
            gsDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // GPS설정 화면으로 이동
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).create().show();
            return false;

        } else {
            return true;
        }
    }

    @Override
    public void onDaumMapOpenAPIKeyAuthenticationResult(MapView mapView, int i, String s) {

    }

    private class ToiletParser extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            String jsonString = null;
            String facility = "toilet";
            String dong = "";

            try {
                //json파일을 String으로 변환한 후, 다시 json객체로 변환
                InputStream is = getAssets().open(facility + ".json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                jsonString = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            StringBuffer sb = null;

            try {
                sb = new StringBuffer();

                JSONObject toiletJSON = new JSONObject(jsonString);
                JSONArray dataArray = toiletJSON.getJSONArray("DATA");

                for (int j = 0; j < 11; j++) {

                    dong = dongNames[j];
                    for (int i = 0; i < dataArray.length(); i++) {
                        JSONObject data = dataArray.getJSONObject(i);
                        if (data.getString("HNR_NAM").equals(dong) == false) continue;
                        Double lat = data.getDouble("LAT"); //위도
                        Double lng = data.getDouble("LNG"); //경도

                        MapPOIItem _temp = new MapPOIItem();
                        _temp.setItemName(dong);
                        _temp.setTag(TOILET_MARKER);
                        _temp.setMapPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
                        _temp.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                        _temp.setCustomImageResourceId(R.drawable.toilet_day_light);
                        _temp.setCustomImageAutoscale(true);
                        _temp.setCustomImageAnchor(0.5f, 1.0f);
                        toiletList.add(_temp);
                    }
                }
                //Log.v("test",sb.toString());
            } catch (JSONException e) {
                //e.printStackTrace();
            } finally {
                toiletItems = new MapPOIItem[toiletList.size()];
                for (int i = 0; i < toiletList.size(); i++) {
                    toiletItems[i] = toiletList.get(i);
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            isToiletParseEnd = true;
        }
    }

    private class MarketParser extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String jsonString = null;
            String facility = "store";
            String dong = "";
            try {
                //json파일을 String으로 변환한 후, 다시 json객체로 변환
                InputStream is = getAssets().open(facility + ".json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                jsonString = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            StringBuffer sb = null;

            try {
                sb = new StringBuffer();


                JSONObject originJSON = new JSONObject(jsonString);
                JSONArray dataArray = originJSON.getJSONArray("DATA");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject data = dataArray.getJSONObject(i);

                    String name = data.getString("GIGU");
                    Double lat = data.getDouble("LAT"); //위도
                    Double lng = data.getDouble("LNG"); //경도

                    MapPOIItem _temp = new MapPOIItem();
                    _temp.setItemName(dong);
                    _temp.setTag(MARKET_MARKER);
                    _temp.setMapPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
                    _temp.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    _temp.setCustomImageResourceId(R.drawable.market_day_light);
                    _temp.setCustomImageAutoscale(true);
                    _temp.setCustomImageAnchor(0.5f, 1.0f);
                    marketList.add(_temp);

                    //sb.append("기구: " + name + " 위도: " + lat + " 경도: " + lng + "\n");
                } // for
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                marketItems = new MapPOIItem[marketList.size()];
                for (int i = 0; i < marketList.size(); i++) {
                    marketItems[i] = marketList.get(i);
                } // for
            } // finally
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            isMarketParseEnd = true;
        }
    }

    private class DrinkParser extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            String jsonString = null;
            String facility = "water";
            String dong = "";
            try {
                //json파일을 String으로 변환한 후, 다시 json객체로 변환
                InputStream is = getAssets().open(facility + ".json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                jsonString = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            StringBuffer sb = null;

            try {
                sb = new StringBuffer();


                JSONObject originJSON = new JSONObject(jsonString);
                JSONArray dataArray = originJSON.getJSONArray("DATA");
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject data = dataArray.getJSONObject(i);

                    String name = data.getString("GIGU");
                    Double lat = data.getDouble("LAT"); //위도
                    Double lng = data.getDouble("LNG"); //경도

                    MapPOIItem _temp = new MapPOIItem();
                    _temp.setItemName(dong);
                    _temp.setTag(DRINK_MARKER);
                    _temp.setMapPoint(MapPoint.mapPointWithGeoCoord(lat, lng));
                    _temp.setMarkerType(MapPOIItem.MarkerType.CustomImage);
                    _temp.setCustomImageResourceId(R.drawable.drink_day_light);
                    _temp.setCustomImageAutoscale(true);
                    _temp.setCustomImageAnchor(0.5f, 1.0f);
                    drinkList.add(_temp);

                    //sb.append("기구: " + name + " 위도: " + lat + " 경도: " + lng + "\n");
                } // for
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                drinkItems = new MapPOIItem[drinkList.size()];
                for (int i = 0; i < drinkList.size(); i++) {
                    drinkItems[i] = drinkList.get(i);
                } // for
            } // finally
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            isDrinkParseEnd = true;
        }
    }


    // TODO: 2017-09-07  MapViewEventListener

    @Override
    public void onMapViewInitialized(MapView mapView) {
        mapView.setMapCenterPointAndZoomLevel(MapPoint.mapPointWithGeoCoord(getDoubleLocation[0], getDoubleLocation[1]), 2, false); // 위치 받아와서 실행
    }


    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }


    // TODO: 2017-09-07  POIItemEventListener


    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }


    // TODO: 2017-09-07  CurrentLocationEventListener

    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {
        Log.d("_test", "error occurred while onCurrentLocationUpdate");
    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {
        Log.d("_test", "cancelled onCurrentLocationUpdate by user");
    }

}
