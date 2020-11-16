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
    String[] shopName;
    String[] price;
    String[] specialOffer;
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
                id= 0;
            } else {
                id= extras.getInt("product_ID");
            }
        } else {
            id= (int) savedInstanceState.getSerializable("product_ID");
        }

        listView = (ListView) findViewById(R.id.lview2);

        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        collectData();
        //CustomListView customListView = new CustomListView(this, shopName, price, specialOffer);
        //listView.setAdapter(customListView);
    }


    private void collectData() {
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
            shopName = new String[ja.length()];
            price = new String[ja.length()];
            specialOffer = new String[ja.length()];

            for (int i = 0; i <= ja.length(); i++) {
                jo = ja.getJSONObject(i);
                shopName[i] = jo.getString("name");
                price[i] = jo.getString("description");
                specialOffer[i] = jo.getString("image_url");
            }
        } catch (Exception ex) {

            ex.printStackTrace();
        }


    }
}