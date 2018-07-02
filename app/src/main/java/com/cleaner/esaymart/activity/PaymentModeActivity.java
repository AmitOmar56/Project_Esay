package com.cleaner.esaymart.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.cleaner.esaymart.R;

public class PaymentModeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_mode);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void goToPaymentGateway(View view) {
        startActivity(new Intent(this, PaymentGatewayActivity.class));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
