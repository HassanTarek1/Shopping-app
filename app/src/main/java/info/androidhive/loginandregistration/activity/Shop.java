package info.androidhive.loginandregistration.activity;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;

import info.androidhive.loginandregistration.R;

public class Shop extends AppCompatActivity implements LocationListener {

    String urladdress = "http://172.21.16.1/android_login_api/display_shop_product.php";
    String urladdress2 = "http://172.21.16.1/android_login_api/display_shop.php";
    String[] shopID;
    String[] productID;
    String[] price;
    String[] specialOffer;

    String[] shopName;
    String[] latitude;
    String[] longitude;
    String[] distances;

    ListView listView;
    BufferedInputStream is;
    String line = null;
    String result = null;
    int id;


    protected LocationManager locationManager;
    protected LocationListener locationListener;
    public Location currentLocation;
    protected Context context;
    String lat;
    String provider;
    protected boolean gps_enabled,network_enabled;


    LatLng currentLatLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                id = 0;
            } else {
                id = extras.getInt("product_ID")+1;
            }
        } else {
            id = (int) savedInstanceState.getSerializable("product_ID")+1;
        }

        listView = (ListView) findViewById(R.id.lview2);

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        currentLocation=getLastKnownLocation();

        collectData1();

        CustomListView2 customListView = new CustomListView2(this, shopName, price, specialOffer, distances);
        listView.setAdapter(customListView);
    }


    private void collectData1() {
//Connection
        try {

            URL url = new URL(urladdress);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is = new BufferedInputStream(con.getInputStream());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //content
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();

        } catch (Exception ex) {
            ex.printStackTrace();

        }

//JSON
        try {
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;
            productID = new String[ja.length()];
            shopID = new String[ja.length()];
            price = new String[ja.length()];
            specialOffer = new String[ja.length()];
            int k=0;
            for (int i = 0; i <= ja.length(); i++) {
                jo = ja.getJSONObject(i);
                productID[i] = jo.getString("product_id");
                int idtmp = Integer.parseInt(productID[i]);
                if (idtmp==id){
                    shopID[k] = jo.getString("shop_id");
                    price[k] = jo.getString("price");
                    specialOffer[k] = jo.getString("available_special_offers");
                    k++;
                }

            }



        } catch (Exception ex) {

            ex.printStackTrace();
        }
        collectData2(shopID);


    }

    private void collectData2(String[] shopID) {
//Connection
        try {

            URL url = new URL(urladdress2);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            is = new BufferedInputStream(con.getInputStream());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //content
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();

        } catch (Exception ex) {
            ex.printStackTrace();

        }

//JSON
        try {
            JSONArray ja = new JSONArray(result);
            JSONObject jo = null;
            shopName = new String[ja.length()];
            latitude = new String[ja.length()];
            longitude = new String[ja.length()];
            distances = new String[ja.length()];
//            currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            int k=0;
//            fetchLocation();
            for (int i = 0; i <= ja.length(); i++) {
                jo = ja.getJSONObject(i);
                String idtmp = jo.getString("id");
                for (int j = 0; j <shopID.length ; j++) {
                    if(idtmp.equals(shopID[j])){
                        shopName[k] = jo.getString("name");
                        latitude[k] = jo.getString("latitude");
                        longitude[k] = jo.getString("longitude");
                        distances[k] = ""+ calculationByDistance(this.currentLocation.getLongitude(),this.currentLocation.getLatitude()
                                , Double.parseDouble(longitude[k]), Double.parseDouble(latitude[k]));
                        k++;
                    }
                }
            }


        } catch (Exception ex) {

            ex.printStackTrace();
        }


    }


    public double calculationByDistance(double lon1, double lat1, double lon2, double lat2) {
        int Radius = 6371;// radius of earth in Km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }
    private Location getLastKnownLocation() {
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}