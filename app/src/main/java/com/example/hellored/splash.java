package com.example.hellored;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class splash extends AppCompatActivity {
    ImageView logo;
    TextView name, own1, own2;
    Animation topAnim, bottomAnim;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        // Initialize UI components
        logo = findViewById(R.id.logoimg);
        name = findViewById(R.id.logonameimg);
        own1 = findViewById(R.id.ownone);
        own2 = findViewById(R.id.owntwo);

        // Load animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        // Apply animations with null check
        if (topAnim != null && bottomAnim != null) {
            logo.setAnimation(topAnim);
            name.setAnimation(bottomAnim);
            own1.setAnimation(bottomAnim);
            own2.setAnimation(bottomAnim);
        }

        // Delayed transition to MainActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(splash.this, MainActivity.class);
            startActivity(intent);
            finish(); // Remove splash from back stack
        }, 2000); // 2-second delay
    }
}
