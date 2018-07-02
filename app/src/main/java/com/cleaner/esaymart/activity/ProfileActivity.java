package com.cleaner.esaymart.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.cleaner.esaymart.R;
import com.cleaner.esaymart.utils.MyProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.cleaner.esaymart.activity.FirstActivity.s_Address;
import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_user_id;
import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;

public class ProfileActivity extends AppCompatActivity {

    private TextView saveAddress, user_profile_name, user_profile_mobile, user_profile_email;
    private ImageView user_profile_photo;
    private String imagePath = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/api/";
    private String apiUrl = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/profile.php";
    private String person_name;
    private String person_phone;
    private String person_email;
    private String person_address;
    private String person_image;
    private ScrollView pro_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getId();
        signupRequest();
//        saveAddress.setText(s_Address);
    }

    private void getId() {
        saveAddress = (TextView) findViewById(R.id.saveAddress);
        user_profile_name = (TextView) findViewById(R.id.user_profile_name);
        user_profile_mobile = (TextView) findViewById(R.id.user_profile_mobile);
        user_profile_email = (TextView) findViewById(R.id.user_profile_email);
        user_profile_photo = (ImageView) findViewById(R.id.user_profile_photo);
        pro_layout = (ScrollView) findViewById(R.id.pro_layout);
    }

    /**
     * Adding few albums for testing
     */

    private void signupRequest() {
        Boolean result = isNetworkAvailable(this);
        if (result) {
            MyProgressDialog.showPDialog(this);
            RequestQueue queue = Volley.newRequestQueue(this);
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.POST, apiUrl,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                //   JSONArray jsonArray = jsonObject.getJSONArray("data");
                                //    Log.d("jsonObject", jsonObject + "");
                                //   Log.d("jsonArray", jsonArray + "");

                                //     JSONObject profile = jsonArray.getJSONObject(0);
                                person_name = jsonObject.getString("name");
                                person_phone = jsonObject.getString("phone");
                                person_email = jsonObject.getString("email");
                                person_address = jsonObject.getString("address");
                                person_image = jsonObject.getString("image");
                                person_image = imagePath + person_image;


                                user_profile_name.setText(person_name);
                                user_profile_mobile.setText(person_phone);
                                user_profile_email.setText(person_email);
                                saveAddress.setText(person_address);
                                Glide.with(ProfileActivity.this).load(person_image).into(user_profile_photo);
                                s_Address = person_address;
                                MyProgressDialog.hidePDialog();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ProfileActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                            MyProgressDialog.hidePDialog();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("user_id", s_user_id);
                    params.put("address", s_Address);

                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } else {
            Snackbar.make(pro_layout, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    public void myWallet(View view) {
        startActivity(new Intent(this, WalletActivity.class));

    }

    public void allOrder(View view) {
        startActivity(new Intent(this, HistoryActivity.class));
    }

    public void editAddress(View view) {
     //   startActivity(new Intent(this, ProductBookingDetailActivity.class));
    }

    public void profileEdit(View view) {
        Intent intent = new Intent(this, ProfileEditActivity.class);
        intent.putExtra("name", person_name);
        intent.putExtra("email", person_email);
        startActivity(intent);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }

    public void logout(View view) {
        insert();
        Intent intent = new Intent(getApplicationContext(), ChoiceActivity.class);
        this.startActivity(intent);
    }

    public void insert() {
        SharedPreferences pref = getSharedPreferences("ActivityPREF", 0);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean("activity_user_executed", false);
        edt.commit();
    }
}
