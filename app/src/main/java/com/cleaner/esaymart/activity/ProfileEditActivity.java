package com.cleaner.esaymart.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.cleaner.esaymart.customtoast.CustomToast;
import com.cleaner.esaymart.utils.MyProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_user_id;
import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;
import static com.cleaner.esaymart.utils.utils.isValidEmail;

public class ProfileEditActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST = 1888;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;

    private String imageSource = "";
    private View view;
    private Bitmap bitmap;
    private ImageView imgView;
    private TextView user_email, user_name;
    private String name, email;
    private ScrollView profile_scroll;
    private String update_url = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/update.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getId();
        getdatafromIntent();
    }

    private void getId() {
        imgView = (ImageView) findViewById(R.id.user_profile_photo);
        user_email = (TextView) findViewById(R.id.user_email);
        user_name = (TextView) findViewById(R.id.user_name);
        profile_scroll = (ScrollView) findViewById(R.id.profile_scroll);
    }

    private void getdatafromIntent() {
        if (getIntent() != null) {
            user_name.setText(getIntent().getStringExtra("name"));
            user_email.setText(getIntent().getStringExtra("email"));
        }
    }

    public void loadImagefromGallery(View view) {
        // Create intent to Open Image applications like Gallery, Google Photos
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the Intent

        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);

    }

    public void loadImagefromCamera(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {

            // When an Image is picked

            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK && null != data) {

                // Get the Image from data


                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor

                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                // Move to first row

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                imgDecodableString = cursor.getString(columnIndex);

                cursor.close();

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);

                BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                imgView.setBackgroundDrawable(ob);
                //  Glide.with(this).load("").into(imgView);
                imgView.setImageBitmap(bitmap);
                Toast.makeText(this, "picked" + bitmap + "", Toast.LENGTH_LONG).show();


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                Log.d("imageString", imageString);

            }

            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageSource = String.valueOf(photo);

                BitmapDrawable ob = new BitmapDrawable(getResources(), photo);
                imgView.setBackgroundDrawable(ob);
                Glide.with(this).load("").into(imgView);
            } else {

                Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();

            }

        } catch (Exception e) {

            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();

        }

    }

    public void changePassword(View view) {
        startActivity(new Intent(this, ChangePasswordActivity.class));
    }

    public void logout(View view) {
        insert();
        Intent intent = new Intent(getApplicationContext(), ChoiceActivity.class);
        this.startActivity(intent);
    }

    private void updateApiCall() {
        Boolean result = isNetworkAvailable(this);
        if (result) {
            MyProgressDialog.showPDialog(this);
        RequestQueue queue = Volley.newRequestQueue(this);
        String response = null;
        final String finalResponse = response;

        StringRequest postRequest = new StringRequest(Request.Method.POST, update_url,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("amit", response);
                        try {
                            Object json = new JSONTokener(response).nextValue();
                            JSONObject jsonObject = (JSONObject) json;
                            if (jsonObject.getString("status").equals("1") || jsonObject.getString("status").equals("STATUS_SUCCESS")) {
                                Snackbar.make(profile_scroll, jsonObject.getString("data"), Snackbar.LENGTH_LONG).show();

                                Toast.makeText(ProfileEditActivity.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                MyProgressDialog.hidePDialog();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProfileEditActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                        MyProgressDialog.hidePDialog();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", s_user_id);
                params.put("name", name);
                params.put("email", email);

                return params;
            }
        };
        postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(postRequest);
        } else {
            Snackbar.make(profile_scroll, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    public void update(View view) {

        boolean isExecuteNext = true;
        name = user_name.getText().toString();
        email = user_email.getText().toString();


        if (!isValidEmail(email)) {
            user_email.setError(getResources().getString(R.string.enter_valid_email));
            isExecuteNext = false;
        }

        if (!isExecuteNext) {
            //registerLayout.startAnimation(shakeAnimation);
//            new CustomToast().Show_Toast(this, view,
//                    "All fields are required.");
            return;
        } else {
            MyProgressDialog.showPDialog(this);

            Log.d("data->>>>>>>>>>>>>>", name + email);
            updateApiCall();
        }

    }

    public void insert() {
        SharedPreferences pref = getSharedPreferences("ActivityPREF", 0);
        SharedPreferences.Editor edt = pref.edit();
        edt.putBoolean("activity_user_executed", false);
        edt.commit();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return true;
    }
}
