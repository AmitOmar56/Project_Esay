package com.cleaner.esaymart.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cleaner.esaymart.R;
import com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity;
import com.cleaner.esaymart.customtoast.CustomToast;
import com.cleaner.esaymart.utils.MyProgressDialog;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.cleaner.esaymart.activity.HomeActivity.lresult;
import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_user_id;
import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_user_name;
import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;

public class LoginActivity extends AppCompatActivity implements
        View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    public LoginButton loginButton;
    private CallbackManager callbackManager;
    private String birthday;

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    private String personPhotoUrl;
    private SignInButton btnSignIn;
    private EditText mobileNumber, login_password;
    private LinearLayout linear_layout;
    private static Animation shakeAnimation;
    private static CheckBox show_hide_password;
    private String getmobile;
    private String getPassword;
    private String login_url = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/user_login.php";
    private String user_id;
    private String user_name;
    private String user_phone;
    private String fb_url = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/fb.php";
    private String fb_name, fb_email, fb_token;
    private String gmail_url = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/g.php";
    private String personEmail, personName, gmail_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getId();
        setListeners();
        // printHashKey(LoginActivity.this);
        facebooklogin();
        btnSignIn.setOnClickListener(this);
        gmailApi();
        if (lresult == false) {
            loginButton.setVisibility(View.VISIBLE);
            FacebookSdk.sdkInitialize(getApplicationContext());
            LoginManager.getInstance().logOut();
        }
    }

    public void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }

    private void getId() {
        loginButton = (LoginButton) findViewById(R.id.login_button);
        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        mobileNumber = (EditText) findViewById(R.id.user_login_Number);
        login_password = (EditText) findViewById(R.id.user_login_password);
        linear_layout = (LinearLayout) findViewById(R.id.linear_layout);
        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);
        show_hide_password = (CheckBox) findViewById(R.id.show_hide_password);
    }

    // Set Listeners
    private void setListeners() {
        // Set check listener over checkbox for showing and hiding password
        show_hide_password
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(CompoundButton button,
                                                 boolean isChecked) {

                        // If it is checkec then show password else hide
                        // password
                        if (isChecked) {

                            show_hide_password.setText(R.string.hide_pwd);// change
                            // checkbox
                            // text

                            login_password.setInputType(InputType.TYPE_CLASS_TEXT);
                            login_password.setTransformationMethod(HideReturnsTransformationMethod
                                    .getInstance());// show password
                        } else {
                            show_hide_password.setText(R.string.show_pwd);// change
                            // checkbox
                            // text

                            login_password.setInputType(InputType.TYPE_CLASS_TEXT
                                    | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            login_password.setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());// hide password

                        }

                    }
                });
    }

    private void facebooklogin() {
        /*************Facebook***********/

        callbackManager = CallbackManager.Factory.create();
        loginButton.setVisibility(View.VISIBLE);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.GONE);
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.d("LoginActivity", response.toString());
                                try {
                                    fb_name = object.getString("name");
                                    Log.d("facebook", object.getString("name") + object.getString("id") + "");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    fb_email = object.getString("email");
                                    Log.d("facebook", object.getString("email"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    fb_token = object.getString("id");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                facebookApiCall();
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender");
                request.setParameters(parameters);
                request.executeAsync();
                Log.d("birthday", birthday + "");

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });

        /*************Facebook***********/
    }

    private void gmailApi() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
        btnSignIn.setScopes(gso.getScopeArray());


    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            personName = acct.getDisplayName();
            Object o = acct.getPhotoUrl();
            Log.d("=->.>>>>>>>>>>", o + "");
            if (o == null) {
                personPhotoUrl = "https://pleper.com/html/assets/img/no-image-found.jpg";
            } else {
                personPhotoUrl = acct.getPhotoUrl().toString();
            }
            personEmail = acct.getEmail();
            gmail_id = acct.getId();


            Log.e(TAG, "Name: " + personName + ", email: " + personEmail
                    + ", Image: " + personPhotoUrl + gmail_id);

            Log.d("personPhotoUrl", personPhotoUrl);
//            Glide.with(getApplicationContext()).load(personPhotoUrl)
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(imgProfilePic);
            updateUI(true);
            gmailApiCall();
            //  insert();
            //  startActivity(new Intent(this, HomeActivity.class));
            updateUI(true);

        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
        signOut();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_sign_in:
                signIn();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Intent", data + "");
        callbackManager.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            MyProgressDialog.showPDialog(this);
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    MyProgressDialog.hidePDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.VISIBLE);
        } else {
            if (lresult == true) {
                btnSignIn.setVisibility(View.VISIBLE);
            } else {
                btnSignIn.setVisibility(View.VISIBLE);
            }
        }
    }

    public void loginPage(View view) {
        // Check Validation before login
        boolean isExecuteNext = true;

        // Get email id and password
        getmobile = mobileNumber.getText().toString();
        getPassword = login_password.getText().toString();

        if (getmobile.equals("")
                || getPassword.equals("") || getPassword.length() == 0) {
            linear_layout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(this, view,
                    "Enter both credentials.");

        }

        if (getmobile.isEmpty()) {
            mobileNumber.setError(getResources().getString(R.string.Phone_error));
            isExecuteNext = false;
        } else if (getmobile.length() < 10) {
            mobileNumber.setError(getResources().getString(R.string.Phone_length_validation));
            isExecuteNext = false;
        }
        if (getPassword.isEmpty()) {
            Log.d("password", getPassword + "");
            login_password.setError(getResources().getString(R.string.password_error));
            isExecuteNext = false;
        }
        if (!isExecuteNext) {
            //registerLayout.startAnimation(shakeAnimation);
            linear_layout.startAnimation(shakeAnimation);
            new CustomToast().Show_Toast(LoginActivity.this, view,
                    "All fields are required.");
        } else {
            loginApiCall();
        }

    }

    private void loginApiCall() {
        Boolean result = isNetworkAvailable(this);
        if (result) {
            MyProgressDialog.showPDialog(this);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.POST, login_url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                if (jsonObject.getString("status").equals("1") || jsonObject.getString("status").equals("STATUS_SUCCESS")) {
                                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                                    Log.d("jsonObject", jsonObject + "");
                                    Log.d("jsonArray", jsonArray + "");

                                    for (int i = 0; i < jsonArray.length(); i++) {

                                        JSONObject profile = jsonArray.getJSONObject(i);

                                        user_id = profile.getString("user_id");
                                        user_name = profile.getString("name");
                                        user_phone = profile.getString("phone");
                                        s_user_id = user_id;
                                        s_user_name = user_name;
//                                        insert();
                                        //  Toast.makeText(LoginActivity.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                                        intent.putExtra("phone", user_phone);
                                        startActivity(intent);
                                        MyProgressDialog.hidePDialog();
                                        break;
                                    }
                                } else {
                                    login_password.setError(getResources().getString(R.string.incorrect_Password));
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
                            Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                            MyProgressDialog.hidePDialog();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("phone", getmobile);
                    params.put("pass", getPassword);

                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } else {
            Snackbar.make(linear_layout, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    private void facebookApiCall() {
        Boolean result = isNetworkAvailable(this);
        if (result) {
            MyProgressDialog.showPDialog(this);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.POST, fb_url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                if (jsonObject.getString("status").equals("1") || jsonObject.getString("status").equals("STATUS_SUCCESS")) {
                                    Log.d("jsonObject", jsonObject + "");
                                    user_id = jsonObject.getString("id");
                                    s_user_id = user_id;
                                    s_user_name = user_name;
                                    insert();
                                    //  Toast.makeText(LoginActivity.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    MyProgressDialog.hidePDialog();

                                } else {
                                    login_password.setError(getResources().getString(R.string.incorrect_Password));
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
                            Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                            MyProgressDialog.hidePDialog();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("name", fb_name);
                    params.put("email", fb_email);
                    params.put("token", fb_token);

                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } else {
            Snackbar.make(linear_layout, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    private void gmailApiCall() {
        Boolean result = isNetworkAvailable(this);
        if (result) {
            MyProgressDialog.showPDialog(this);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.POST, gmail_url,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                if (jsonObject.getString("status").equals("1") || jsonObject.getString("status").equals("STATUS_SUCCESS")) {
                                    Log.d("jsonObject", jsonObject + "");
                                    user_id = jsonObject.getString("id");
                                    s_user_id = user_id;
                                    s_user_name = user_name;
                                    insert();
                                    //  Toast.makeText(LoginActivity.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    MyProgressDialog.hidePDialog();

                                } else {
                                    login_password.setError(getResources().getString(R.string.incorrect_Password));
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
                            Toast.makeText(LoginActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                            MyProgressDialog.hidePDialog();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("name", personName);
                    params.put("email", personEmail);
                    params.put("token", gmail_id);

                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);
        } else {
            Snackbar.make(linear_layout, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    // startActivity(new Intent(this, HomeActivity.class));
//}
    public void insert() {
        SharedPreferences pref = getSharedPreferences("ActivityPREF", 0);
        SharedPreferences.Editor edt = pref.edit();
        edt.putString("user_id", user_id);
        edt.putString("user_name", user_name);
        edt.putBoolean("activity_user_executed", true);
        edt.commit();
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));

    }

    public void gotoforgotPage(View view) {
        startActivity(new Intent(this, ForgotPasswordActivity.class));
    }
}
