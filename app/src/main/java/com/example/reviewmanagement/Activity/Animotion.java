package com.example.reviewmanagement.Activity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.example.reviewmanagement.R;

public class Animotion extends AppCompatActivity {

    TextView appname;
    LottieAnimationView lottie;
    private Button manageCoursesButton;

    // 用于跟踪动画数量
    private int animationCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.animotion);

        appname = findViewById(R.id.appname);
        lottie = findViewById(R.id.lottie);
        manageCoursesButton = findViewById(R.id.button_manage_courses);

        // 隐藏ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        // 启动动画并设置监听器
        startAnimation(appname, -1400, 2000, 0); // 将持续时间减少为1500毫秒
        startAnimation(lottie, 2000, 2000, 200); // 将持续时间减少为1500毫秒，延迟时间减少为200毫秒

        // 当所有动画结束时显示按钮并设置监听器
        manageCoursesButton.setVisibility(View.INVISIBLE);
        manageCoursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到课程管理活动
                Intent intent = new Intent(Animotion.this, ManageCoursesActivity.class);
                startActivity(intent);
                finish(); // 结束当前活动
            }
        });
    }

    // 启动动画并增加计数器
    private void startAnimation(View view, float translationY, long duration, long startDelay) {
        view.animate()
                .translationY(translationY)
                .setDuration(duration)
                .setStartDelay(startDelay)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        // 动画结束时增加计数器
                        animationCount++;
                        // 如果所有动画都结束了，则显示按钮
                        if (animationCount == 2) {
                            manageCoursesButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}

