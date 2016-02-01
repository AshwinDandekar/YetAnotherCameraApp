package com.example.asd29.findthoseips;

import com.google.android.gms.maps.model.LatLng;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    public boolean Ip1GreaterthanIp2;
    public static boolean largeIprange = false;
    public static boolean normalIprange = false;
    public static boolean smallIprange = false;
    public static String LAT_LONG = "mykey";
    public ArrayList<IPAddress> ipAddresses = null;
    public ArrayList<IpCityResponse> ipCityResponses = null;
    public ArrayList<LatLng> latLngs = null;
    private EditText IP1Byte1;
    private EditText IP1Byte2;
    private EditText IP1Byte3;
    private EditText IP1Byte4;
    private EditText IP2Byte1;
    private EditText IP2Byte2;
    private EditText IP2Byte3;
    private EditText IP2Byte4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        int NUMBER_OF_PROCESSORS = Runtime.getRuntime().availableProcessors();
        initializeTextBoxes();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void enableMapsClicked(View v) {

        if(IP1Byte1.getText().toString().matches("") || IP1Byte3.getText().toString().matches("") || IP1Byte2.getText().toString().matches("") || IP1Byte4.getText().toString().matches("") ||
                IP2Byte1.getText().toString().matches("")|| IP2Byte2.getText().toString().matches("") || IP2Byte3.getText().toString().matches("") || IP2Byte4.getText().toString().matches("")) {
            Toast.makeText(this, "Invalid Input! One byte will have 0-255", Toast.LENGTH_LONG).show();
            return;
        } else {
            int IP1byte1 = Integer.parseInt(IP1Byte1.getText().toString());
            int IP1byte2 = Integer.parseInt(IP1Byte2.getText().toString());
            int IP2byte1 = Integer.parseInt(IP2Byte1.getText().toString());
            int IP2byte2 = Integer.parseInt(IP2Byte2.getText().toString());
            int IP2byte3 = Integer.parseInt(IP2Byte3.getText().toString());
            int IP2byte4 = Integer.parseInt(IP2Byte4.getText().toString());
            int IP1byte4 = Integer.parseInt(IP1Byte4.getText().toString());
            int IP1byte3 = Integer.parseInt(IP1Byte3.getText().toString());

            if ((IP1byte1 < 0 || IP1byte1 > 255) || (IP1byte2 < 0 || IP1byte2 > 255) || (IP1byte3 < 0 || IP1byte3 > 255) || (IP1byte4 < 0 || IP1byte4 > 255) || (IP2byte1 < 0 || IP2byte1 > 255) || (IP2byte2 < 0 || IP2byte2 > 255) || (IP2byte3 < 0 || IP2byte3 > 255) || (IP2byte4 < 0 || IP2byte4 > 255)) {
                Toast.makeText(this, "Invalid Input! One byte will have 0-255", Toast.LENGTH_LONG).show();
                return;
            }

            if ((IP1byte1 == 192 && IP1byte2 == 168) || IP1byte1 == 10 || (IP1byte1 == 172 && (IP1byte2 == 16 || IP1byte2 == 31))) {
                Toast.makeText(this, "the From IP is in the reserved IP range", Toast.LENGTH_LONG).show();
                return;

            } else if ((IP2byte1 == 192 && IP2byte2 == 168) || IP2byte1 == 10 || (IP2byte1 == 172 && (IP2byte2 == 16 || IP2byte2 == 31))) {
                Toast.makeText(this, "the To IP is in the reserved IP range", Toast.LENGTH_LONG).show();
                return;
            }

            if(IP1byte1-IP2byte1  >0 || IP2byte1 - IP1byte1 >0 ) {
                Toast.makeText(this,"You are trying to locate over 16777216 Ips select a shorter range!",Toast.LENGTH_LONG).show();
                return;
            }
            if(IP1byte1==IP2byte1 && (IP1byte2-IP2byte2 > 0 || IP2byte2-IP1byte2>0)) {
                largeIprange = true;

            }else if (IP1byte1 == IP2byte1 && IP1byte2 == IP2byte2 && (IP1byte3 - IP2byte3 > 0 || IP2byte3 - IP1byte3 > 0)) {
                normalIprange = true;
            } else if(IP1byte1 == IP2byte1 && IP1byte2 == IP2byte2 && (IP1byte3 - IP2byte3 > 0 || IP2byte3 - IP1byte3 > 0)) {
                largeIprange = true;

            }
            else if(IP1byte1 == IP2byte1 && IP1byte2 == IP2byte2 && IP1byte3==IP2byte3) {
                smallIprange = true;
            }
            IPAddress[] rangeAddresses = getAddress();
            try {
                ipAddresses = (new genrateIPs().execute(rangeAddresses)).get(10000, TimeUnit.MILLISECONDS);
                ipCityResponses = (new APIConnection().execute(ipAddresses)).get(60000, TimeUnit.MILLISECONDS);
                latLngs = (new Acquirelatlongs().execute(ipCityResponses)).get(10000, TimeUnit.MILLISECONDS);

            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(this, MapsActivity.class);
            intent.putExtra(LAT_LONG, latLngs);
            startActivity(intent);

        }
    }

    public void initializeTextBoxes() {
        IP1Byte1 = (EditText) findViewById(R.id.IP1Byte1);
        IP1Byte2 = (EditText) findViewById(R.id.IP1Byte2);
        IP1Byte3 = (EditText) findViewById(R.id.IP1Byte3);
        IP1Byte4 = (EditText) findViewById(R.id.IP1Byte4);
        IP2Byte1 = (EditText) findViewById(R.id.IP2Byte1);
        IP2Byte2 = (EditText) findViewById(R.id.IP2Byte2);
        IP2Byte3 = (EditText) findViewById(R.id.IP2Byte3);
        IP2Byte4 = (EditText) findViewById(R.id.IP2Byte4);

        //Setting the maximum value to 255
    }

    public IPAddress[] getAddress() {
        IPAddress[] rangeAddresses = new IPAddress[2];
        rangeAddresses[0] = new IPAddress(Integer.parseInt(IP1Byte1.getText().toString()),Integer.parseInt(IP1Byte2.getText().toString()),Integer.parseInt(IP1Byte3.getText().toString()), Integer.parseInt(IP1Byte4.getText().toString()));
        rangeAddresses[1] = new IPAddress(Integer.parseInt(IP2Byte1.getText().toString()), Integer.parseInt(IP2Byte2.getText().toString()),Integer.parseInt(IP2Byte3.getText().toString()), Integer.parseInt(IP2Byte4.getText().toString()));

        Ip1GreaterthanIp2 = checkIps();


        return rangeAddresses;
    }

    public boolean checkIps () {
        int IP1byte1 = Integer.parseInt(IP1Byte1.getText().toString());
        int IP1byte2 = Integer.parseInt(IP1Byte2.getText().toString());
        int IP2byte1 = Integer.parseInt(IP2Byte1.getText().toString());
        int IP2byte2 = Integer.parseInt(IP2Byte2.getText().toString());
        int IP2byte3 = Integer.parseInt(IP2Byte3.getText().toString());
        int IP2byte4 = Integer.parseInt(IP2Byte4.getText().toString());
        int IP1byte4 = Integer.parseInt(IP1Byte4.getText().toString());
        int IP1byte3 = Integer.parseInt(IP1Byte3.getText().toString());

        if (IP1byte1 > IP2byte1) {
            return true;
        } else if ((IP1byte1 == IP2byte1 && IP1byte2 > IP2byte2)) {
            return true;
        } else if ((IP1byte1 == IP1byte1 && IP1byte2 == IP2byte2 && IP1byte3 > IP2byte3)) {
            return true;
        } else if ((IP1byte1 == IP1byte1 && IP1byte2 == IP2byte2 && IP1byte3 == IP2byte3 && IP1byte4 > IP2byte4)) {
            return true;
        } else {
            return false;
        }
    }

    public class genrateIPs extends AsyncTask<IPAddress[],Integer,ArrayList<IPAddress>> {
        @Override
        protected ArrayList<IPAddress> doInBackground(IPAddress[]... params) {
            ArrayList<IPAddress> result = new ArrayList<IPAddress>();
            if(params[0]==null) {
                result.add(new IPAddress(0,0,0,0));
                return result;
            }
            else {

                if (Ip1GreaterthanIp2) {
                    int byte1 = params[0][1].getByte1();
                    int byte2 = params[0][1].getByte2();
                    int byte3 = params[0][1].getByte3();
                    int byte4 = params[0][1].getByte4();
                    boolean firstk = true;
                    boolean firstj = true;
                    boolean firstl = true;
                    int lIncrement = 1;
                    int kIncrement = 1;
                    if (largeIprange) {
                        lIncrement = 100;
                        kIncrement = 10;
                    } else if (normalIprange) {
                        lIncrement = 50;
                    } else if (smallIprange) {
                        lIncrement = 1;
                    }

                    for (int i = byte1; i <= params[0][0].getByte1(); i++) {
                        outerloop:
                        for (int j = 0; j < 256; j++) {
                            if (firstj) {
                                firstj = false;
                                j = byte2;
                            }
                            for (int k = 0; k < 256; k += kIncrement) {
                                if (firstk) {
                                    firstk = false;
                                    k = byte3;
                                }
                                for (int l = 0; l < 256; l += lIncrement) {
                                    if (firstl) {
                                        firstl = false;
                                        l = byte4;
                                    }
                                    System.out.println(i + "." + j + "." + k + "." + l);
                                    result.add(new IPAddress(i, j, k, l));
                                    if (!firstj & !firstk & !firstl && i == params[0][0].getByte1() && j == params[0][0].getByte2() && k >= params[0][0].getByte3()) {
                                        if(smallIprange && l == params[0][0].getByte4() &&!firstl) {
                                            break outerloop;
                                        }
                                        else if(normalIprange &&!firstl && l > params[0][0].getByte4()){
                                            break outerloop;
                                        }
                                        else if(largeIprange &&!firstk && (k>params[0][0].getByte3())) {
                                            break outerloop;
                                        }
                                    }
                                }
                            }
                        }
                    }

                } else if (!Ip1GreaterthanIp2) {
                    int byte1 = params[0][0].getByte1();
                    int byte2 = params[0][0].getByte2();
                    int byte3 = params[0][0].getByte3();
                    int byte4 = params[0][0].getByte4();
                    boolean firstk = true;
                    boolean firstj = true;
                    boolean firstl = true;
                    int lIncrement = 1;
                    int kIncrement = 1;
                    if (largeIprange) {
                        lIncrement = 100;
                        kIncrement = 10;
                    } else if (normalIprange) {
                        lIncrement = 50;
                    } else if (smallIprange) {
                        lIncrement = 1;
                    }

                    for (int i = byte1; i <= params[0][1].getByte1(); i++) {
                        outerloop:
                        for (int j = 0; j < 256; j++) {
                            if (firstj) {
                                firstj = false;
                                j = byte2;
                            }
                            for (int k = 0; k < 256; k += kIncrement) {
                                if (firstk) {
                                    firstk = false;
                                    k = byte3;
                                }
                                for (int l = 0; l < 256; l += lIncrement) {
                                    if (firstl) {
                                        firstl = false;
                                        l = byte4;
                                    }
                                    System.out.println(i + "." + j + "." + k + "." + l);
                                    result.add(new IPAddress(i, j, k, l));
                                    if (!firstj & !firstk & !firstl && i == params[0][1].getByte1() && j == params[0][1].getByte2() && k >= params[0][1].getByte3()) {
                                        if(smallIprange && l == params[0][0].getByte4() &&!firstl) {
                                            break outerloop;
                                        }
                                        else if(normalIprange && l > params[0][0].getByte4() &&!firstl){
                                            break outerloop;
                                        }
                                        else if(largeIprange &&!firstk && (k>params[0][0].getByte3())) {
                                            break outerloop;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return result;
            }
        }
    }

    public static class Acquirelatlongs extends AsyncTask<ArrayList<IpCityResponse>, Void,ArrayList<LatLng>> {

    @Override
    protected ArrayList<LatLng> doInBackground(ArrayList<IpCityResponse>... params) {
        ArrayList<LatLng> latLngs = new ArrayList<LatLng>();
        if(params[0]==null) {
            latLngs.add(new LatLng(0,0));
            return latLngs;
        } else {

            for (IpCityResponse curr : params[0]) {
                LatLng newlocation = new LatLng(Double.parseDouble(curr.getLatitude()), Double.parseDouble(curr.getLongitude()));
                latLngs.add(newlocation);
            }
            return latLngs;
        }
    }

}



    public class APIConnection extends AsyncTask<ArrayList<IPAddress>, Void, ArrayList<IpCityResponse>> {
        private URL url;
        private HttpURLConnection urlConnection;

        @Override
        protected ArrayList<IpCityResponse> doInBackground(final ArrayList<IPAddress>... params) {
            ipCityResponses = new ArrayList<IpCityResponse>();

            for(IPAddress curr : params[0]) {
                        try {
                            IpCityResponse resp = new IpCityResponse();
                            url = new URL("http://api.ipinfodb.com/v3/ip-city/?key=6022030bbf801545b60ebf0c4ad795f19f4eadf1b99f19a6f55c2697cd2caabd&ip=" +curr.byte1+"."+curr.byte2+"."+curr.byte3+"."+curr.byte4+ "&format=json");
                            urlConnection = (HttpURLConnection) url.openConnection();
                            InputStreamReader in = new InputStreamReader(urlConnection.getInputStream());
                            JsonReader responseReader = new JsonReader(in);
                            responseReader.beginObject();
                            while (responseReader.hasNext()) {
                                String name = responseReader.nextName();
                                if (name.equals("statusCode")) {
                                    resp.setStatusCode(responseReader.nextString());
                                } else if (name.equals("statusMessage")) {
                                    resp.setStatusMessage(responseReader.nextString());
                                } else if (name.equals("ipAddress")) {
                                    resp.setIpAddress(responseReader.nextString());
                                } else if (name.equals("countryCode")) {
                                    resp.setCountryCode(responseReader.nextString());
                                } else if (name.equals("countryName")) {
                                    resp.setCountryName(responseReader.nextString());
                                } else if (name.equals("regionName")) {
                                    resp.setRegionName(responseReader.nextString());
                                } else if (name.equals("cityName")) {
                                    resp.setCityName(responseReader.nextString());
                                } else if (name.equals("zipCode")) {
                                    resp.setZipCode(responseReader.nextString());
                                } else if (name.equals("latitude")) {
                                    resp.setLatitude(responseReader.nextString());
                                } else if (name.equals("longitude")) {
                                    resp.setLongitude(responseReader.nextString());
                                } else if (name.equals("timeZone")) {
                                    resp.setTimeZone(responseReader.nextString());
                                }
                            }
                            ipCityResponses.add(resp);
                        } catch (Exception e) {

                        } finally {
                            urlConnection.disconnect();
                        }
                    }
            return ipCityResponses;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            largeIprange = false;
            normalIprange = false;
            smallIprange = false;
        }
        return super.onKeyDown(keyCode, event);
    }
}