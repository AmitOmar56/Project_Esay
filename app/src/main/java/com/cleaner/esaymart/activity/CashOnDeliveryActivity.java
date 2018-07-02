package com.cleaner.esaymart.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cleaner.esaymart.R;

public class CashOnDeliveryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_on_delivery);
    }

    @Override
    public void onBackPressed() {

    }

    public void home(View view) {
        startActivity(new Intent(this, HomeActivity.class));
    }
}
