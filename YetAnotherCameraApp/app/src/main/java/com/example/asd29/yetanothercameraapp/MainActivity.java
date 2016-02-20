package com.example.asd29.yetanothercameraapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText userid;
    private EditText password;
    private SharedPreferences mPref;
    public static final String Mypref = "CameraAppPref";
    public static final String user = "username";
    public static final String pwd = "Password";
    public static final String setuname = "admin";
    public static final String setpwd = "notanadmin";
    private SharedPreferences.Editor mPrefEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userid = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        mPref = getSharedPreferences(Mypref, Context.MODE_PRIVATE);
       // mPrefEditor = mPref.edit();
        if(mPref.contains(user) && mPref.contains(pwd)){
            if(mPref.getString(user,null).equals(setuname) && mPref.getString(pwd,null).equals(setpwd)) {
                Intent intent = new Intent(this, CaptureFeed.class);
                startActivity(intent);
            }
        }

    }

    public void runFadeAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fadeout);
        animation.reset();
        RelativeLayout Rl = (RelativeLayout) findViewById(R.id.RelativeLayout);
        Rl.clearAnimation();
        Rl.startAnimation(animation);
    }

    public void login(View view) {
            String inputuserid = userid.getText().toString();
            String inputpassword = password.getText().toString();
            if(inputuserid.matches("") || inputpassword.matches("")) {
                Toast.makeText(this,"UserID and Password should not be blank",Toast.LENGTH_LONG).show();
            }
            if(inputuserid.equals(setuname) && inputpassword.equals(setpwd)) {
                mPrefEditor = mPref.edit();
                mPrefEditor.putString(user, inputuserid);
                mPrefEditor.putString(pwd, inputpassword);
                mPrefEditor.commit();
                runFadeAnimation();
                Intent intent = new Intent(this,CaptureFeed.class);
                startActivity(intent);

            }
        else {
                Toast.makeText(this,"Invalid Username and Password",Toast.LENGTH_LONG).show();
            }
    }

}
