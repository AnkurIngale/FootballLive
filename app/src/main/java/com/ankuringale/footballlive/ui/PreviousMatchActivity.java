package com.ankuringale.footballlive.ui;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;

import com.ankuringale.footballlive.R;
import com.ankuringale.footballlive.adapters.PreviousHandler;
import com.ankuringale.footballlive.football.Current;
import com.ankuringale.footballlive.databinding.ActivityPreviousMatchBinding;

import java.util.ArrayList;
import java.util.List;

public class PreviousMatchActivity extends AppCompatActivity {

    private PreviousHandler adapter;
    private ActivityPreviousMatchBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Bundle bundle = getIntent().getExtras();
        List<Current> currents = new ArrayList<>();

        for (String key : bundle.keySet()) {
            currents.add((Current) bundle.getSerializable(key));
        }


        binding = DataBindingUtil.setContentView(this,R.layout.activity_previous_match);
        adapter = new PreviousHandler (currents , this);
        binding.previousMatchItems.setAdapter(adapter);
        binding.previousMatchItems.setHasFixedSize(true);
        binding.previousMatchItems.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        binding.previousMatchItems.setLayoutManager(new LinearLayoutManager(this));
    }

}
