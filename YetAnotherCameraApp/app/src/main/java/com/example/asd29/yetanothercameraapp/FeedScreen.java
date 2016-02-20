package com.example.asd29.yetanothercameraapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;

public class FeedScreen extends AppCompatActivity {

    private final File mFileDir = new File(Environment.getExternalStorageDirectory()+"/DCIM/FakeCamera/");
    private ImageView mImageView;
    private Bitmap mBitmap;
    private File[] mlistfile;
    private int count = 0;
    private File mimg;
    private GestureDetector mgdt;
    private final int distance = 100;
    private float downX,downY,upX,upY,deltaX,deltaY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_screen);
        mgdt = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(e1.getY() - e2.getY() > distance) {
                    upswipe();
                    return false; // Bottom to top
                }  else if (e2.getY() - e1.getY() > distance) {
                    downswipe();
                    return false; // Top to bottom
                }
                return false;
            }
        });
        mlistfile = mFileDir.listFiles();
        mImageView = (ImageView)findViewById(R.id.imageView);
        if(mlistfile == null) {
           mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.placeholder);
        } else {
            mimg = mlistfile[0];
            mBitmap = BitmapFactory.decodeFile(mimg.getAbsolutePath());
        }
        mImageView.setImageBitmap(mBitmap);
        mImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override

            public boolean onTouch(View v, MotionEvent event) {
                mgdt.onTouchEvent(event);
                return true;
            }
        });
    }


    public void upswipe() {
        if(mlistfile==null) {
            Toast.makeText(this,"Please go back and capture images to view them",Toast.LENGTH_SHORT).show();
        }
        else {
            count--;
            if (count < 0) {
                count = mlistfile.length - 1;
            }
            mimg = mlistfile[count];
            if (mimg.exists()) {
                mBitmap = BitmapFactory.decodeFile(mimg.getAbsolutePath());
                mImageView.setImageBitmap(mBitmap);
            }
        }
    }


    public void downswipe() {
        if(mlistfile==null) {
            Toast.makeText(this,"Please go back and capture images to view them",Toast.LENGTH_SHORT).show();
        } else {
            count++;
            if (count == mlistfile.length) {
                count = 0;
            }
            mimg = mlistfile[count];
            if (mimg.exists()) {
                mBitmap = BitmapFactory.decodeFile(mimg.getAbsolutePath());
                mImageView.setImageBitmap(mBitmap);
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mlistfile = mFileDir.listFiles();
    }
}
