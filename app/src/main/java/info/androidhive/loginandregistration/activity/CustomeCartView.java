package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.loginandregistration.R;

public class CustomeCartView extends ArrayAdapter<String> {

    private ArrayList<String> productName;
    private ArrayList<String> shopName;
    private ArrayList<String> price;
    private ArrayList<String> cart_id;

    private ArrayList<String> imagepath;
    private Activity context;
    private Button btnDelete;
    Bitmap bitmap;


    public CustomeCartView( Activity context, ArrayList<String> productName, ArrayList<String> shopName,ArrayList<String> price, ArrayList<String> imagepath, ArrayList<String> cart_id ) {
        super(context, R.layout.cartlayout,productName);
        this.context=context;
        this.productName=productName;
        this.shopName=shopName;
        this.price=price;
        this.imagepath=imagepath;
        this.cart_id=cart_id;
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.cartlayout,null,true);
            viewHolder=new ViewHolder(r, position);
            r.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)r.getTag();

        }

        if(!productName.isEmpty())
            viewHolder.tvw1.setText(productName.get(position));
        if (!shopName.isEmpty())
            viewHolder.tvw2.setText(shopName.get(position));
        if (!price.isEmpty())
            viewHolder.tvw3.setText(price.get(position));
        if (!imagepath.isEmpty())
            new GetImageFromURL(viewHolder.ivw).execute(imagepath.get(position));

        return r;
    }

    class ViewHolder{

        TextView tvw1;
        TextView tvw2;
        TextView tvw3;
        ImageView ivw;


        ViewHolder(View v, int position){
            tvw1=(TextView)v.findViewById(R.id.productnameCart);
            tvw2=(TextView)v.findViewById(R.id.shopnameCart);
            tvw3=(TextView)v.findViewById(R.id.priceCart);
            ivw=(ImageView)v.findViewById(R.id.imageViewCart);
            btnDelete = (Button)v.findViewById(R.id.btndelete);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteItem(position);

                }
            });
        }

    }

    public class GetImageFromURL extends AsyncTask<String, Void, Bitmap>
    {

        ImageView imgView;
        public GetImageFromURL(ImageView imgv)
        {
            this.imgView=imgv;
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            String urldisplay=url[0];
            bitmap=null;

            try{

                InputStream ist=new java.net.URL(urldisplay).openStream();
                bitmap= BitmapFactory.decodeStream(ist);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap){

            super.onPostExecute(bitmap);
            imgView.setImageBitmap(bitmap);
        }
    }
    public void deleteItem(int position){
        StringRequest request=new StringRequest(Request.Method.POST, "http://172.30.112.1/android_login_api/delete_from_cart.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equalsIgnoreCase("added to cart")){
                            Toast.makeText(context,"deleted from cart", Toast.LENGTH_SHORT).show();
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
                String index=cart_id.get(position);
                params.put("id",index);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(request);
        this.notifyDataSetChanged();


    }


}
