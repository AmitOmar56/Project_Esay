package com.cleaner.esaymart.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cleaner.esaymart.R;

import static com.cleaner.esaymart.activity.FirstActivity.s_Address;

public class HistoryDetailActivity extends AppCompatActivity {

    private TextView user_number, history_name, cart_disc, cart_quantity, cart_price, created_date, status, delivery_date, user_name, user_address;
    private ImageView historyImage;
    private String product_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getId();
        getdatafromIntent();
//        user_address.setText(s_Address);
        Glide.with(this).load(product_image).into(historyImage);

    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            product_image = getIntent().getStringExtra("image");
            user_address.setText(getIntent().getStringExtra("p_address"));
            history_name.setText(getIntent().getStringExtra("p_name"));
            user_name.setText(getIntent().getStringExtra("u_name"));
            cart_disc.setText(getIntent().getStringExtra("p_desc"));
            user_number.setText(getIntent().getStringExtra("u_phone"));
            cart_quantity.setText(getIntent().getStringExtra("p_quantity"));
            cart_price.setText("Rs. " + getIntent().getStringExtra("p_price"));
            created_date.setText(getIntent().getStringExtra("p_created"));
            status.setText(getIntent().getStringExtra("p_status"));
            delivery_date.setText(getIntent().getStringExtra("p_deliver"));

        }
    }

    private void getId() {
        user_address = (TextView) findViewById(R.id.user_address);
        historyImage = (ImageView) findViewById(R.id.historyImage);
        history_name = (TextView) findViewById(R.id.history_name);
        user_number = (TextView) findViewById(R.id.user_number);
        cart_disc = (TextView) findViewById(R.id.cart_disc);
        cart_quantity = (TextView) findViewById(R.id.cart_quantity);
        cart_price = (TextView) findViewById(R.id.cart_price);
        created_date = (TextView) findViewById(R.id.created_date);
        status = (TextView) findViewById(R.id.status);
        delivery_date = (TextView) findViewById(R.id.delivery_date);
        user_name = (TextView) findViewById(R.id.user_name);
        user_address = (TextView) findViewById(R.id.user_address);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
