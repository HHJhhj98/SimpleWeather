package com.huhuijia.simpleweather.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import com.huhuijia.simpleweather.MainActivity;
import com.huhuijia.simpleweather.R;



public class SplashActivity extends AppCompatActivity {

    private TextView mtv_splash;
    private AnimationSet set;//组合动画
    private int animTime = 2000;//时间

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class),ActivityOptionsCompat.makeSceneTransitionAnimation(SplashActivity.this).toBundle());
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initview();
        getWindow().setExitTransition(new Fade().setDuration(500));
    }



    private void initview() {
        mtv_splash = (TextView) findViewById(R.id.tv_splash);
        set = new AnimationSet(true);//是否共用一个动画补间
        set.setDuration(animTime);//动画执行之后保持状态
        set.setFillAfter(true);

        //缩放
        ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1);
        scale.setDuration(animTime);
        set.addAnimation(scale);

        //平移
        TranslateAnimation translate = new TranslateAnimation(0, 0, 0, -200);
        translate.setDuration(animTime);
        set.addAnimation(translate);

        //执行动画
        mtv_splash.startAnimation(set);

        //动画监听
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                handler.sendEmptyMessageDelayed(1001, 0);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
