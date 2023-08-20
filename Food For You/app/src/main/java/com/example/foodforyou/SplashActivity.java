package com.example.foodforyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    ImageView c_pic;
    TextView c_title;
    Animation top, bottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        top=AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottom=AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        c_pic=findViewById(R.id.c_pic);
        c_title=findViewById(R.id.c_title);

        c_pic.setAnimation(bottom);
        c_title.setAnimation(top);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 3000);
    }
}