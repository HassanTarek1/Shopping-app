package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;

/**
 * Created by jaiso on 13-02-2018.
 */

public class CustomListView2 extends ArrayAdapter<String> {

    private  ArrayList<String> shopName;
    private  ArrayList<String> price;
    private  ArrayList<String> specialOffer;
    private  ArrayList<String> distances;
    private String ids;
    ArrayList<String> extra_shop_id;
    private Button btnadd;
    private Activity context;

    Bitmap bitmap;

    public CustomListView2(Activity context, ArrayList<String> shopName,  ArrayList<String> price,  ArrayList<String> specialOffer,  ArrayList<String> distances, String ids
    , ArrayList<String> extra_shop_id) {
        super(context, R.layout.layout2, shopName);
        this.context=context;
        this.shopName = shopName;
        this.price = price;
        this.specialOffer = specialOffer;
        this.distances=distances;
        this.extra_shop_id=extra_shop_id;
        this.ids= ids;


    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.layout2,null,true);
            viewHolder=new ViewHolder(r, position);
            r.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)r.getTag();

        }

        viewHolder.tvw1.setText(shopName.get(position));
        viewHolder.tvw2.setText(price.get(position));
        if(specialOffer.get(position)!=null && !specialOffer.get(position).equals(""))
            viewHolder.tvw3.setText(specialOffer.get(position));
        else{
            if (shopName.get(position)!=null && !shopName.get(position).equals(""))
                viewHolder.tvw3.setText("No Available Offers");
        }

        viewHolder.tvw4.setText(distances.get(position));


        return r;
    }

    class ViewHolder{

        TextView tvw1;
        TextView tvw2;
        TextView tvw3;
        TextView tvw4;

        ViewHolder(View v, int position){
            tvw1=(TextView)v.findViewById(R.id.shopname);
            tvw2=(TextView)v.findViewById(R.id.price);
            tvw3=(TextView)v.findViewById(R.id.offer);
            tvw4=(TextView)v.findViewById(R.id.distance);
            btnadd = (Button) v.findViewById(R.id.btnadd);
            btnadd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addToCart(position);
                }
            });
        }

    }

    public void addToCart(int position){
        String user_id = (ids.substring(0,ids.length()-1));
        int product_id = Character.getNumericValue(ids.charAt(ids.length()-1));
        int shopID= Integer.parseInt(extra_shop_id.get(position));

        StringRequest request=new StringRequest(Request.Method.POST, "http://172.30.112.1/android_login_api/add_to_cart.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("added to cart")){
                            Toast.makeText(context,"added to cart", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(context,response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id+"");
                params.put("shop_id",shopID+"");
                params.put("product_id",product_id+"");

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);



    }




}
