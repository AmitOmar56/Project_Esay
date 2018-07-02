package com.cleaner.esaymart.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.cleaner.esaymart.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ServiceLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String key_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_location);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getdatafromIntent();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            key_id = getIntent().getStringExtra("id");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.clener_icon);
        // Add a marker in Sydney and move the camera
        LatLng My_location = new LatLng(28.4109712, 77.0408874);
        mMap.addMarker(new MarkerOptions().position(My_location).title("My Location").icon(icon));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(My_location, 10));

        LatLng Dwarka = new LatLng(28.592140, 77.046048);
        mMap.addMarker(new MarkerOptions().position(Dwarka).title("Dwarka").icon(icon));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(My_location, 10));

        LatLng Gurgaon = new LatLng(28.459497, 77.026638);
        mMap.addMarker(new MarkerOptions().position(Gurgaon).title("Gurgaon").icon(icon));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(My_location, 10));

    }

    public void bookNow(View view) {

        /*************Code For Dialog Box Start************/

        final BottomSheetDialog dialog = new BottomSheetDialog(ServiceLocationActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.service_option_dialog, null);

        Button dialog_fixed = (Button) mView.findViewById(R.id.dialog_fixed);
        Button dialog_immediate = (Button) mView.findViewById(R.id.dialog_immediate);

        dialog_fixed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(ServiceLocationActivity.this, FixedServiceActivity.class);
                intent.putExtra("id", key_id + "");
                startActivity(intent);
                Toast.makeText(ServiceLocationActivity.this, "Clicked_fixed", Toast.LENGTH_LONG).show();
            }
        });
        dialog_immediate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ServiceLocationActivity.this, "Clicked_immediate", Toast.LENGTH_LONG).show();
            }
        });
        dialog.setContentView(mView);
        dialog.show();

        /*************Code For Dialog Box End*************/
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
