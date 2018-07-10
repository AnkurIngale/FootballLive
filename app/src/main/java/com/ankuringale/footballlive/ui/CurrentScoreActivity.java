package com.ankuringale.footballlive.ui;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.ankuringale.footballlive.R;

import com.ankuringale.footballlive.adapters.EventHandler;
import com.ankuringale.footballlive.databinding.ActivityCurrentScoreBinding;
import com.ankuringale.footballlive.football.Current;
import com.ankuringale.footballlive.football.Matches;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CurrentScoreActivity extends AppCompatActivity {

    private boolean doublePressedOnce = false;
    private int c =0;
    private static  final String TAG = CurrentScoreActivity.class.getSimpleName();
    private Current current;
    private Matches matches;
    private ImageView weatherImageView;
    private EventHandler adapter;
    private ActivityCurrentScoreBinding activityCurrentScoreBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"Activity Created!");
        Log.d(TAG,"weatherImageView");
        current = new Current();
        matches = new Matches();

        getCurrentScore();
    }

    public  boolean isNetworkAvailable() {
        boolean isAvailable = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected())
        {
            isAvailable = true;
        }

        return isAvailable;
    }


    @Override
    public void onBackPressed() {

        if(doublePressedOnce){
            Log.d(TAG,"App Exit!");
            finish();
        }
        doublePressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doublePressedOnce=false;
            }
        }, 2000);
    }

    public void getCurrentScore()
    {

        activityCurrentScoreBinding  = DataBindingUtil.setContentView(this,R.layout.activity_current_score);
        String URL = "https://worldcup.sfg.io/matches";
        weatherImageView = findViewById(R.id.weatherImageview);

        if(isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(URL).build();
            Call call = client.newCall(request);


            //asynchronous thread
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    alertError();
                }


                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, jsonData);
                        if (response.isSuccessful()) {
                            matches.getJSON(jsonData);
                            current = matches.returnCurrentMatch();
                            if(c!=2)
                            matches.setPreviousMatches();
                            else c++;


                                Log.v(TAG, "LOda hai ye nhi chalega");


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(!(current.getWeather()==null)){
                                    Drawable d = getResources().getDrawable(getIconId(current.getWeather()));
                                    weatherImageView.setImageDrawable(d);
                                    }
                                    else{alertError();}
                                }
                            });


                        } else {
                            alertError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "IO Exception", e);
                    } catch (JSONException j) {
                        Log.e(TAG, "JSON Exception", j);
                    }
                }
            });

            adapter = new EventHandler(current.getEvents() , CurrentScoreActivity.this);
            activityCurrentScoreBinding.myEventItems.setAdapter(adapter);
            activityCurrentScoreBinding.myEventItems.setHasFixedSize(true);
            activityCurrentScoreBinding.myEventItems.addItemDecoration(new DividerItemDecoration(CurrentScoreActivity.this,DividerItemDecoration.VERTICAL));
            activityCurrentScoreBinding.myEventItems.setLayoutManager(new LinearLayoutManager(CurrentScoreActivity.this));
            activityCurrentScoreBinding.setCurrent(current);


        }
        else{
            Toast.makeText(this,"No Internet Connection, try again!" , Toast.LENGTH_LONG).show();
        }
    }


    private void alertError()
    {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.show(getFragmentManager() , "error_dialog");
    }


    public  void refreshOnClick(View view)
    {
        getCurrentScore();
    }

    public void nextActivity(View view)
    {

        List<Current> lista = matches.getPrevious();

        if(lista == null)
        {
            //alertError();
            return;
        }

        Log.v(TAG , getResources().getString(R.string.clara));
        Log.v(TAG,lista.get(0).getTeam1Code());
        Intent intent = new Intent(this,PreviousMatchActivity.class);
        Bundle bundle = new Bundle();
        for (int i = 0; i<lista.size(); i++)
            bundle.putSerializable("extras"+i, lista.get(i));

        intent.putExtras(bundle);
        startActivity(intent);
    }


    public int getIconId(String weather)
    {
        switch(weather)
        {
            case "Partly Cloudy Night":
            case "Partly Cloudy":
                return R.drawable.partly_cloudy;
            case "Sunny":
                return R.drawable.sunny;
            case "Rain":
                return R.drawable.rain;
            case "Clear Day":
                return R.drawable.clear_day;
            case "Clear Night":
                return R.drawable.clear_night;
            default:
                return R.drawable.clear_night;
        }
    }


}
