package com.example.foodforyou;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void BuyerPage(View view) {
        startActivity(new Intent(this, BSignInActivity.class));
    }

    public void SellerPage(View view) {
        startActivity(new Intent(this, SignInActivity.class));
    }
}