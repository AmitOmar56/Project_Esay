package com.cleaner.esaymart.activity_cleaningBoy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.cleaner.esaymart.R;

public class Login_CleaningBoyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__cleaning_boy);
    }

    public void goToHomePage(View view) {
        startActivity(new Intent(this, Home_CleaningActivity.class));
    }

    public void gotoregisterPage(View view) {
        startActivity(new Intent(this, Register_Cleaning_BoyActivity.class));
    }
}
