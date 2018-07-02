package com.cleaner.esaymart.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cleaner.esaymart.R;
import com.cleaner.esaymart.activity_cleaningBoy.Login_CleaningBoyActivity;
import com.cleaner.esaymart.activity_cleaningBoy.MainActivity;

import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_emp_id;

public class ChoiceActivity extends AppCompatActivity {
    private ImageView car_user, cleaning_boy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        launchScreen();
        getId();
//        Bitmap myBitmap = QRCode.from("www.example.org").bitmap();
//        ImageView myImage = (ImageView) findViewById(R.id.imageView);
//        myImage.setImageBitmap(myBitmap);
        Glide.with(this).load("https://thumbs.dreamstime.com/z/cartoon-boy-red-car-illustration-33235789.jpg").into(car_user);
        Glide.with(this).load("http://hddfhm.com/images/car-cleaning-clipart-19.jpg").into(cleaning_boy);
    }

    private void getId() {
        car_user = (ImageView) findViewById(R.id.car_user);
        cleaning_boy = (ImageView) findViewById(R.id.cleaning_boy);
    }

    private void launchScreen() {

    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setNegativeButton("No", null).show();
    }

    public void user_login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void cleaning_boy_login(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
