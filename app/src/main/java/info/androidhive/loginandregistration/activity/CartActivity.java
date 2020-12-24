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
import java.util.ArrayList;

import info.androidhive.loginandregistration.R;



public class CartActivity extends AppCompatActivity {
    String displayCartURL = "http://172.30.112.1/android_login_api/display_cart.php";
    String displayShopProductURL = "http://172.30.112.1/android_login_api/display_shop_product.php";
    String displayShopURL = "http://172.30.112.1/android_login_api/display_shop.php";
    String displayProductURL = "http://172.30.112.1/android_login_api/display_product.php";

    ArrayList<String> productName;
    ArrayList<String> shopName;
    ArrayList<String> price;
    ArrayList<String> imagepath;
    ArrayList<String> user_id;
    ArrayList<String> shop_id;
    ArrayList<String> shop_product_id;
    ArrayList<String> product_id;
    ArrayList<String> allProducts;
    ArrayList<String> allShops;
    ArrayList<String> allimg;
    ArrayList<String> allPrices;
    ArrayList<String> cart_id;
    ListView listViewCart;
    BufferedInputStream is;
    String line = null;
    String result = null;
    String currentUsr;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        StrictMode.setThreadPolicy((new StrictMode.ThreadPolicy.Builder().permitNetwork().build()));
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

                currentUsr="";
            } else {
                ;
                currentUsr=extras.getString("user_id");
            }
        } else {

            currentUsr= (String) savedInstanceState.getSerializable("user_id");
        }
        collectData();
        listViewCart = (ListView) findViewById(R.id.lviewCart);
        CustomeCartView customCartView = new CustomeCartView(this, productName, shopName, price, imagepath, cart_id);
        listViewCart.setAdapter(customCartView);
    }

    private void collectData() {
//Connection
        try {

            URL url = new URL(displayCartURL);
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

            cart_id = new ArrayList<String>();
            user_id = new ArrayList<String>();
            shop_id = new ArrayList<String>();
            product_id = new ArrayList<String>();

            for (int i = 0; i <= ja.length(); i++) {
                jo = ja.getJSONObject(i);
                cart_id.add(jo.getString("id"));
                if(jo.getString("user_id").equalsIgnoreCase(currentUsr)) {
                    String sID=jo.getString("shop_id");
                    String pID = jo.getString("product_id");
                    shop_id.add(sID);
                    product_id.add(pID);
                }
            }
        } catch (Exception ex) {

            ex.printStackTrace();
        }

        collectData2();
    }

    private void collectData2(){
        try {
            URL url = new URL(displayProductURL);
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
            productName = new ArrayList<String>();
            imagepath = new ArrayList<String>();
            allProducts = new ArrayList<String>();
            allimg = new ArrayList<String>();
            for (int i = 0; i <= ja.length(); i++) {
                jo = ja.getJSONObject(i);
                allProducts.add(jo.getString("name"));
                allimg.add(jo.getString("image_url"));
            }
            for (int i = 0; i <product_id.size() ; i++) {
                int index = Integer.parseInt(product_id.get(i));
                productName.add(allProducts.get(index-1));
                imagepath.add(allimg.get(index-1));
            }
        } catch (Exception ex) {
            for (int i = 0; i <product_id.size() ; i++) {
                int index = Integer.parseInt(product_id.get(i));
                productName.add(allProducts.get(index-1));
                imagepath.add(allimg.get(index-1));
            }
            ex.printStackTrace();
        }
        collectData3();
    }

    private void collectData3(){
        try {
            URL url = new URL(displayShopURL);
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
            shopName = new ArrayList<String>();
            allShops = new ArrayList<String>();

            for (int i = 0; i <= ja.length(); i++) {
                jo = ja.getJSONObject(i);
                allShops.add(jo.getString("name"));
            }
            for (int i = 0; i <shop_id.size() ; i++) {
                int index = Integer.parseInt(shop_id.get(i));
                shopName.add(allShops.get(index-1));
            }
        } catch (Exception ex) {
            for (int i = 0; i <shop_id.size() ; i++) {
                int index = Integer.parseInt(shop_id.get(i));
                shopName.add(allShops.get(index-1));
            }
            ex.printStackTrace();
        }
        collectData4();

    }
    private void collectData4(){
        try {
            URL url = new URL(displayShopProductURL);
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
            price = new ArrayList<String>();
            allPrices = new ArrayList<String>();
            shop_product_id = new ArrayList<String>();
            for (int i = 0; i <= ja.length(); i++) {
                jo = ja.getJSONObject(i);
                allPrices.add(jo.getString("price"));
                shop_product_id.add(jo.getString("id"));
            }
            for (int i = 0; i <product_id.size() ; i++) {
                String tmp = product_id.get(i)+shop_id.get(i);
                for (int j = 0; j <shop_product_id.size() ; j++) {
                    if (tmp.equalsIgnoreCase(shop_product_id.get(j))){
                        price.add(allPrices.get(j));
                    }
                }
            }

        } catch (Exception ex) {
            for (int i = 0; i <product_id.size() ; i++) {
                String tmp = product_id.get(i)+shop_id.get(i);
                for (int j = 0; j <shop_product_id.size() ; j++) {
                    if (tmp.equalsIgnoreCase(shop_product_id.get(j))){
                        price.add(allPrices.get(j));
                    }
                }
            }

            ex.printStackTrace();
        }


    }
}