package info.androidhive.loginandregistration;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.HashMap;

import info.androidhive.loginandregistration.activity.CartActivity;
import info.androidhive.loginandregistration.activity.List_Products;
import info.androidhive.loginandregistration.activity.LoginActivity;
import info.androidhive.loginandregistration.activity.MapActivity;
import info.androidhive.loginandregistration.helper.SQLiteHandler;
import info.androidhive.loginandregistration.helper.SessionManager;

public class MainActivity extends Activity {

    private TextView txtName;
    private TextView txtEmail;
    private Button btnLogout;
    private Button btnmap;
    private Button btnproducts;
    private Button btnCart;

    private String user_id;

    private SQLiteHandler db;
    private SessionManager session;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = (TextView) findViewById(R.id.name);
        txtEmail = (TextView) findViewById(R.id.email);
        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnmap = (Button) findViewById(R.id.btnmap);
        btnproducts= (Button) findViewById(R.id.btnproducts);
        btnCart= (Button) findViewById(R.id.btncart);

        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            logoutUser();
        }

        // Fetching user details from sqlite
        HashMap<String, String> user = db.getUserDetails();

        String name = user.get("name");
        String email = user.get("email");
        user_id = user.get("unique_id");

        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);

        // Logout button click event
        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        // map button click event
        btnmap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                map();
            }
        });

        // products button click event
        btnproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProducts();
            }
        });

        btnCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToCart();
            }
        });

    }

    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences Clears the user data from sqlite users table
     * */
    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void map(){
        Intent intent = new Intent(MainActivity.this, MapActivity.class);
        startActivity(intent);
    }

    private void showProducts(){
        Intent intent = new Intent(MainActivity.this, List_Products.class);
        intent.putExtra("user_id",user_id);
        startActivity(intent);
    }
    private void goToCart(){
        Intent intent = new Intent(MainActivity.this, CartActivity.class);
        intent.putExtra("user_id",user_id);
        startActivity(intent);
    }
}