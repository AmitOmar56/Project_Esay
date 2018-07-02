package com.cleaner.esaymart.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.cleaner.esaymart.R;

public class ReferAndEarnActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void refer(View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, "Easy Mart");
        String sAux = "\nabc1234\n\n";
        sAux = sAux + "https://play.google.com/store/apps/details?id=com.lifeplaytrip&hl=en \n\n";
        i.putExtra(Intent.EXTRA_TEXT, sAux);
        startActivity(Intent.createChooser(i, "choose one"));
    }

    public void referacar(View view) {
        startActivity(new Intent(this, ReferACar.class));
    }

    public void referaperson(View view) {
        startActivity(new Intent(this, ReferAndEarn.class));

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
