package info.androidhive.loginandregistration.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import info.androidhive.loginandregistration.R;

/**
 * Created by jaiso on 13-02-2018.
 */

public class CustomListView2 extends ArrayAdapter<String> {

    private String[] shopName;
    private String[] price;
    private String[] specialOffer;
    private String[] distances;
    private Activity context;
    Bitmap bitmap;

    public CustomListView2(Activity context, String[] shopName, String[] price, String[] specialOffer, String[] distances) {
        super(context, R.layout.layout2, shopName);
        this.context=context;
        this.shopName = shopName;
        this.price = price;
        this.specialOffer = specialOffer;
        this.distances=distances;
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View r=convertView;
        ViewHolder viewHolder=null;
        if(r==null){
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.layout2,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)r.getTag();

        }

        viewHolder.tvw1.setText(shopName[position]);
        viewHolder.tvw2.setText(price[position]);
        viewHolder.tvw3.setText(specialOffer[position]);
        viewHolder.tvw4.setText(distances[position]);


        return r;
    }

    class ViewHolder{

        TextView tvw1;
        TextView tvw2;
        TextView tvw3;
        TextView tvw4;

        ViewHolder(View v){
            tvw1=(TextView)v.findViewById(R.id.shopname);
            tvw2=(TextView)v.findViewById(R.id.price);
            tvw3=(TextView)v.findViewById(R.id.offer);
            tvw4=(TextView)v.findViewById(R.id.distance);
        }

    }




}
