package com.example.asd29.yetanothercameraapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.hardware.Camera;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class CaptureScreen extends AppCompatActivity implements SurfaceHolder.Callback{

     Camera mCamera;
    private SurfaceView mSurface;
    private SurfaceHolder mSurfaceHolder;
    private SurfaceHolder.Callback mSurfaceHolderCallback;
    private Camera.PictureCallback jpegImg;
    private final String folder = "FakeCamera/";
    private final File mFileDir = new File(Environment.getExternalStorageDirectory()+"/DCIM/",folder);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_screen);
        mSurface = (SurfaceView) findViewById(R.id.textureView);
        mSurfaceHolder = mSurface.getHolder();
        mSurfaceHolder.addCallback(this);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        if(!mFileDir.exists()) {
            mFileDir.mkdirs();
        }

        jpegImg = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream file_op = null;
                try {
                    File img = new File(mFileDir+"/",String.valueOf(Calendar.getInstance().getTimeInMillis())+".jpg");
                    file_op = new FileOutputStream(img);
                    Bitmap curr = BitmapFactory.decodeByteArray(data,0,data.length);
                    Bitmap resized = Bitmap.createScaledBitmap(curr, 1080, 1920, true);
                    resized.compress(Bitmap.CompressFormat.JPEG, 100, file_op);
                   //file_op.write(data);
                    Toast.makeText(getApplicationContext(),"Picture Saved with size 1920*1080",Toast.LENGTH_LONG).show();
                    file_op.close();
                }
                catch (IOException ioe) {
                    System.out.print(ioe.toString());
                }
                startpreview();
                callIntent();
            }
        };
    }

    public void callIntent() {
        Intent intent = new Intent(this,FeedScreen.class);
        startActivity(intent);
    }

    @Override
    public void onStop() {
        super.onStop();
        //mCamera.stopPreview();
        //mCamera.release();
    }
    @Override
    public void onPause() {
        super.onPause();
 //       mCamera.stopPreview();
 //       mCamera.release();
    }

    @Override
    public void onResume() {
        super.onResume();
        startpreview();
    }

    public void takePic(View v) {
            mCamera.takePicture(null, null, null, jpegImg);

    }

    public void startpreview() {
        try {

            Camera.Parameters param;
            param = mCamera.getParameters();
            param.setRotation(270);
            Camera.Size s = param.getPictureSize();
            Toast.makeText(getApplicationContext(),"Current resolution "+s.height+"*"+s.width,Toast.LENGTH_LONG).show();
            mCamera.setParameters(param);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.setDisplayOrientation(270);
            mCamera.startPreview();
        }
        catch(Exception e) {

            System.out.print(e.toString());
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try{
            mCamera = Camera.open();
            startpreview();
        }
        catch(Exception e) {

        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }
}
