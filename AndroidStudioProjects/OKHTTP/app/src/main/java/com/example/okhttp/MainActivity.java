package com.example.okhttp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    TextView output ;
    TextView Oxygen ;
    TextView Temp ;
    TextView Temp1 ;
    JSONObject Json;
    Object jsonOb ;
    Thread thread1 ;
    String filed1 ;
    String filed2 ;
    String filed3 ;
    boolean threadop = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {





        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "chat";
            String channelName = "聊天訊息";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            createNotificationChannel(channelId, channelName, importance);
            channelId = "subscribe";
            channelName = "訂閱訊息";
            importance = NotificationManager.IMPORTANCE_DEFAULT;
            createNotificationChannel(channelId, channelName, importance);
        }


    }

    @Override
    protected void onStart() {


        super.onStart();
        httpget();
    }

    @Override
    protected void onResume() {


        super.onResume();
        threadop = false ;
        thread1.start();

    }

    @Override
    protected void onPause() {


        super.onPause();

        threadop = true ;
        try {
            thread1.join();
           // thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public void httpget()
    {

        output =  (TextView) findViewById(R.id.textView);
        Oxygen =  (TextView) findViewById(R.id.textView3);
        Temp   =  (TextView) findViewById(R.id.textView4);
        Temp1   = (TextView) findViewById(R.id.textView5);
        filed1 = new String();
        filed2 = new String();
        filed3 = new String();

        thread1 = new Thread(new Runnable() {
            public void run() {
                //for(;;)
                while (threadop == false)
                {

                    Log.d("Ok_thread", "123");
                    try {

                        Request request = new Request.Builder()
                                .url("https://api.thingspeak.com/channels/509678/feeds.json?api_key=HKTD6CV84JJFF6WL&results=1") //url
                                .build();
                        Call call = client.newCall(request);

                        call.enqueue(new Callback() {
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                // 連線成功
                                final String result = response.body().string();

                                try {
                                    Json = new JSONObject(result);
                                    Json = (JSONObject) Json.getJSONArray("feeds").get(0);
                                    // Json format get feeds array[0]
                                    filed1 = Json.getString("field1"); // get field string
                                    jsonOb= Json.getString("field1");
                                    Log.d("OkHttp result", jsonOb.toString());
                                    //============== get Field1 number ==================
                                    filed2 = Json.getString("field2"); // get field string
                                    jsonOb= Json.getString("field2");
                                    Log.d("OkHttp result", jsonOb.toString());
                                    //============== get Field2 number ==================
                                    filed3 = Json.getString("field3"); // get field string
                                    jsonOb= Json.getString("field3");
                                    Log.d("OkHttp result", jsonOb.toString());
                                    //============== get Field3 number ==================
                                    //string = jsonOb.toString().split(",") ;
                                    //Log.d("OkHttp result", string[1]);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // update TextView here!
                                        output.setText(result);
                                        Oxygen.setText(filed1);
                                        Temp.setText(filed2);
                                        Temp1.setText(filed3);

                                    }
                                });
                            }//success connect and responds
                            @Override
                            public void onFailure(Call call, IOException e) {
                                // 連線失敗

                            }//fail connect
                        });

                        thread1.sleep(3000);                //每隔5秒顯示一次
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });  //thread


    }//====================================================httpget()===========================================





    public void clicktest(View view) {

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this, "chat")
                .setContentTitle("收到一條聊天訊息")
                .setContentText("吃三小消夜? 去你的麥當當？")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                        //R.drawable.icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                .setAutoCancel(true)
                .build();
        manager.notify(1, notification);


     /*   output = (TextView) findViewById(R.id.textView);
        // 建立Request，設置連線資訊
        Request request = new Request.Builder()
                .url("https://api.thingspeak.com/channels/509678/feeds.json?api_key=HKTD6CV84JJFF6WL&results=1") //url
                .build();

/*
        // 建立Call
        Call call = client.newCall(request);

        //test
        Callback callback = new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                response.body().string();
            }
        };
        call.enqueue(callback);  //call.enquene(callback class) ;
*/
/*
        // 建立Call
        Call call = client.newCall(request);

        // 執行Call連線到網址
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // 連線成功
                final String result = response.body().string();

                try {
                    Json = new JSONObject(result);
                    Json = (JSONObject) Json.getJSONArray("feeds").get(0);
                    jsonOb= Json.getString("field1");

                    Log.d("OkHttp result", jsonOb.toString());
                    //string = jsonOb.toString().split(",") ;
                    //Log.d("OkHttp result", string[1]);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // update TextView here!
                        output.setText(result);
                    }
                });
            }//success connect and responds
            @Override
            public void onFailure(Call call, IOException e) {
                // 連線失敗

            }//fail connect
        });
        //output.setText("ok");*/




    }//================================================== click clicktest function ==================================================

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        NotificationManager notificationManager = (NotificationManager) getSystemService(
                NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }




}
