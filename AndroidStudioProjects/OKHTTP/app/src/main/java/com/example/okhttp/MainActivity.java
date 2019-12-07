package com.example.okhttp;

import androidx.appcompat.app.AppCompatActivity;

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

        output = (TextView) findViewById(R.id.textView);


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
                                    jsonOb= Json.getString("field1");
                                    Log.d("OkHttp result", jsonOb.toString());
                                    jsonOb= Json.getString("field2");
                                    Log.d("OkHttp result", jsonOb.toString());
                                    jsonOb= Json.getString("field3");
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

                        thread1.sleep(3000);                //每隔5秒顯示一次
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });  //thread


    }//====================================================httpget()===========================================





    public void clicktest(View view) {

        output = (TextView) findViewById(R.id.textView);
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
        //output.setText("ok");
    }//================================================== click clicktest function ==================================================



}
