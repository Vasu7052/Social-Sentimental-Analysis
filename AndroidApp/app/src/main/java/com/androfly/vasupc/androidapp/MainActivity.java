package com.androfly.vasupc.androidapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {

    Button btnStart;
    EditText etHashTag ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Intent i = new Intent(MainActivity.this , DisplayActivity.class);
        startActivity(i);
        finish();*/

        btnStart = findViewById(R.id.btnStart);
        etHashTag = findViewById(R.id.etHashTag);

        btnStart.setEnabled(false);

        etHashTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp = etHashTag.getText().toString();
                if (temp.length() > 0){
                    btnStart.setEnabled(true);
                    btnStart.setText("Proceed!");
                }else{
                    btnStart.setEnabled(false);
                    btnStart.setText("Get Started");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String query = etHashTag.getText().toString().trim().replace("#" , "");
                if (!query.isEmpty()){
                    if(isNetworkConnected()){
                        final SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Please Wait..");
                        pDialog.setContentText("Getting Tweets of Hashtag!");
                        pDialog.setCancelable(false);
                        pDialog.show();
                        GetDataRequest gdr = new GetDataRequest(MainActivity.this, query, new AsyncResponseString() {
                            @Override
                            public void processFinish(String output) {
                                pDialog.dismissWithAnimation();
                                if (isJSONValid(output)){
                                    //Toast.makeText(MainActivity.this, "Valid", Toast.LENGTH_SHORT).show();
                                    etHashTag.setText("");
                                    Intent i = new Intent(MainActivity.this , DisplayActivity.class);
                                    i.putExtra("Response" , output);
                                    i.putExtra("Hashtag1" , query);
                                    startActivity(i);
                                }else{
                                    Toast.makeText(MainActivity.this, "Please Try Again Later!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        gdr.execute();
                    }else{
                        Toast.makeText(MainActivity.this, "No Internet Available!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Please Enter a Keyword!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

}
