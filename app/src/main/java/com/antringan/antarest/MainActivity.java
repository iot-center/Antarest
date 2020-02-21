package com.antringan.antarest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import id.co.telkom.iot.AntaresHTTPAPI;
import id.co.telkom.iot.AntaresResponse;

public class MainActivity extends AppCompatActivity implements AntaresHTTPAPI.OnResponseListener {

    private TextView txtData;
    private AntaresHTTPAPI antaresAPIHTTP;
    private String dataDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
        Button btnOff = (Button) findViewById(R.id.btnOff);
        Button btnOn = (Button) findViewById(R.id.btnOn);
        txtData = (TextView) findViewById(R.id.txtData);

        antaresAPIHTTP = new AntaresHTTPAPI();
        antaresAPIHTTP.addListener(this);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP.getLatestDataofDevice("c01538e56fc59f94:eff9cd5d2fee545c","TestAntarest","Monitoring");
            }
        });

        btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP.storeDataofDevice(1,"c01538e56fc59f94:eff9cd5d2fee545c", "TestAntarest", "Monitoring", "{\\\"status\\\":\\\"1\\\"}");

            }
        });

        btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                antaresAPIHTTP.storeDataofDevice(1,"c01538e56fc59f94:eff9cd5d2fee545c", "TestAntarest", "Monitoring", "{\\\"status\\\":\\\"0\\\"}");
            }
        });
    }

    @Override
    public void onResponse(AntaresResponse antaresResponse) {
        String TAG = "ANTARES-API";
        Log.d(TAG,Integer.toString(antaresResponse.getRequestCode()));
        if(antaresResponse.getRequestCode()==0){
            try {
                JSONObject body = new JSONObject(antaresResponse.getBody());
                dataDevice = body.getJSONObject("m2m:cin").getString("con");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txtData.setText(dataDevice);
                    }
                });
                Log.d(TAG,dataDevice);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
