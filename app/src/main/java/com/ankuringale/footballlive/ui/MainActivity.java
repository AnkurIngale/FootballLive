package com.ankuringale.footballlive.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ankuringale.footballlive.R;





public class MainActivity extends AppCompatActivity {


    private boolean doublePressedOnce = false;
    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView text;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        text = findViewById(R.id.message);
        text.setBackgroundColor(getResources().getColor(R.color.appBackground));
        pesta();

    }

    private void pesta() {
        text.setBackgroundColor(getResources().getColor(R.color.black));
        if(isNetworkAvailable()) {

            text.setTextColor(getResources().getColor(R.color.appBackground));
            text.setText("Connecting To The Server. . .");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this , CurrentScoreActivity.class);
                    startActivity(intent);
                    finish();
                }
            } , 2000);

        }

        else{

            text.setTextColor(getResources().getColor(R.color.red));
            text.setText("No Internet Connection\nClick Here Once Set");
        }
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



    public void onClick(View view)
    {
        pesta();
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



}
