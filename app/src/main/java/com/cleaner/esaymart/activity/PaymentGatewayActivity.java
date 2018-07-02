package com.cleaner.esaymart.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cleaner.esaymart.R;
import com.cleaner.esaymart.paymentgatway.InitialScreenActivity;
import com.cleaner.esaymart.paymentgatway.WebViewActivity;
import com.cleaner.esaymart.utils.AvenuesParams;
import com.cleaner.esaymart.utils.MyProgressDialog;
import com.cleaner.esaymart.utils.ServiceUtility;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static com.cleaner.esaymart.activity_cleaningBoy.Home_CleaningActivity.s_user_id;
import static com.cleaner.esaymart.utils.utils.isNetworkAvailable;


public class PaymentGatewayActivity extends AppCompatActivity {


    private RadioGroup radioGroup;
    String checkBox = "";
    private String order_id;
    private String key_id, key_data, key_type;
    private LinearLayout gateway_layout;
    private String cashOn_deliveryApi = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/cod.php";
    private String pro_cashOn_deliveryApi = "http://littlejoy.co.in/esaymart/public_html/admin_dryclean/users/cart_bp.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_gateway);
        getId();
        getDatafromIntent();
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
                if (null != radioButton && checkedId > -1) {
                    checkBox = radioButton.getText().toString();
                    Toast.makeText(PaymentGatewayActivity.this, radioButton.getText(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getDatafromIntent() {
        if (getIntent() != null) {
            key_id = getIntent().getStringExtra("id");
            key_data = getIntent().getStringExtra("key");
            key_type = getIntent().getStringExtra("type");
        }
    }

    private void getId() {
        radioGroup = (RadioGroup) findViewById(R.id.rd);
        gateway_layout = (LinearLayout) findViewById(R.id.gateway_layout);
    }

    public void start_payment(View view) {
        if (checkBox.equals("Cash on Delivery")) {
            if (key_type.equals("ser")) {
                cashOn_deliveryApiCall();
            } else if (key_type.equals("pro")) {
                product_cashOn_deliveryApiCall();
            }
        }
        if ((checkBox.equals("Online Payment"))) {
            paymentPage();
            // startActivity(new Intent(this, InitialScreenActivity.class));
        } else {
            Toast.makeText(this, "Please Click ", Toast.LENGTH_LONG).show();
        }
    }

    private void paymentPage() {

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull("AVTG76FB43BS37GTSB").toString().trim());
        intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(166015).toString().trim());
        intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(order_id).toString().trim());
        intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull("INR").toString().trim());
        intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull("1.00").toString().trim());
        intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull("http://esaymart.com/mobile/ccavResponseHandler.php").toString().trim());
        intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull("http://esaymart.com/mobile/ccavResponseHandler.php").toString().trim());
        intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull("http://esaymart.com/mobile/GetRSA.php").toString().trim());
        intent.putExtra("id", key_id + "");
        intent.putExtra("key", key_data + "");
        intent.putExtra("type", key_type);
        startActivity(intent);
    }

    private void product_cashOn_deliveryApiCall() {
        Boolean result = isNetworkAvailable(this);
        if (result) {
            MyProgressDialog.showPDialog(this);
            RequestQueue queue = Volley.newRequestQueue(this);
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.POST, pro_cashOn_deliveryApi,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                if (jsonObject.getString("status").equals("STATUS_SUCCESS")) {

                                    Toast.makeText(PaymentGatewayActivity.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(PaymentGatewayActivity.this, CashOnDeliveryActivity.class);
//                                    intent.putExtra("id", key_id + "");
//                                    intent.putExtra("key", mobile);
                                    startActivity(intent);
                                    MyProgressDialog.hidePDialog();

                                } else {
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
                            Toast.makeText(PaymentGatewayActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                            MyProgressDialog.hidePDialog();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("user_id", key_id);
                    params.put("total_price", key_data);
                    params.put("status", "1");
                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);

        } else {
            Snackbar.make(gateway_layout, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }

    private void cashOn_deliveryApiCall() {
        Boolean result = isNetworkAvailable(this);
        if (result) {
            MyProgressDialog.showPDialog(this);
            RequestQueue queue = Volley.newRequestQueue(this);
            String response = null;
            final String finalResponse = response;

            StringRequest postRequest = new StringRequest(Request.Method.POST, cashOn_deliveryApi,
                    new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("amit", response);
                            try {
                                Object json = new JSONTokener(response).nextValue();
                                JSONObject jsonObject = (JSONObject) json;
                                if (jsonObject.getString("status").equals("STATUS_SUCCESS")) {

                                    Toast.makeText(PaymentGatewayActivity.this, jsonObject.getString("data"), Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(PaymentGatewayActivity.this, CashOnDeliveryActivity.class);
//                                    intent.putExtra("id", key_id + "");
//                                    intent.putExtra("key", mobile);
                                    startActivity(intent);
                                    MyProgressDialog.hidePDialog();

                                } else {
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
                            Toast.makeText(PaymentGatewayActivity.this, "Network Error", Toast.LENGTH_LONG).show();
                            MyProgressDialog.hidePDialog();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("service_id", key_id);
                    params.put("phone", key_data);
                    return params;
                }
            };
            postRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(postRequest);

        } else {
            Snackbar.make(gateway_layout, "Network Not Found", Snackbar.LENGTH_LONG).show();

        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //generating new order number for every transaction
        Integer randomNum = ServiceUtility.randInt(0, 9999999);
        order_id = randomNum.toString();
    }
}
