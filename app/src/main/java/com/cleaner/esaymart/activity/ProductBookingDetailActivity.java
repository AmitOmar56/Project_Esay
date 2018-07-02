package com.cleaner.esaymart.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.cleaner.esaymart.R;

import static com.cleaner.esaymart.activity.FirstActivity.s_Address;

public class ProductBookingDetailActivity extends AppCompatActivity {

    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_booking_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getId();
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                if (null != radioButton && checkedId > -1) {
                    Toast.makeText(ProductBookingDetailActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void add_save(View view) {
        finish();
    }

    private void getId() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

}
