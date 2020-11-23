package info.androidhive.loginandregistration.activity;

import android.os.Bundle;
import android.os.StrictMode;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import info.androidhive.loginandregistration.R;

public class Shop extends AppCompatActivity {

    String urladdress = "http://172.21.16.1/android_login_api/display_shop_product.php";
    String urladdress2 = "http://172.21.16.1/android_login_api/display_shop.php";
    String[] shopID;
    String[] productID;
    String[] price;
    String[] specialOffer;

    String[] shopName;
    String[] latitude;
    String[] longitude;

    ListView listView;
    BufferedInputStream is;
    String line = null;
    String result = null;
    int id;

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

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData1();
        CustomListView2 customListView = new CustomListView2(this, shopName, price, specialOffer);
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
            int k=0;
            for (int i = 0; i <= ja.length(); i++) {
                jo = ja.getJSONObject(i);
                String idtmp = jo.getString("id");
                for (int j = 0; j <shopID.length ; j++) {
                    if(idtmp.equals(shopID[j])){
                        shopName[k] = jo.getString("name");
                        latitude[k] = jo.getString("latitude");
                        longitude[k] = jo.getString("longitude");
                        k++;
                    }
                }
            }


        } catch (Exception ex) {

            ex.printStackTrace();
        }


    }
}