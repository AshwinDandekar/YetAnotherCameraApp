package com.example.asd29.yetanothercameraapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CaptureFeed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void runFadeAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        animation.reset();
        RelativeLayout Rl = (RelativeLayout) findViewById(R.id.RelativeLayout2);
        Rl.clearAnimation();
        Rl.startAnimation(animation);
    }

    @Override
    public void onResume() {
        super.onResume();
        runFadeAnimation();
    }
    public void clickCapture(View v) {
        Intent intent = new Intent(this,CaptureScreen.class);
        startActivity(intent);
    }

    public void clickFeed(View v) {
        Intent intent = new Intent(this,FeedScreen.class);
        startActivity(intent);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            this.finishAffinity();

        }
        return super.onKeyDown(keyCode, event);
    }
}
