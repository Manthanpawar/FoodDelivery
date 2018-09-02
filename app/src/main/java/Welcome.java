package foodxpress.foodxpress;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Welcome extends AppCompatActivity implements View.OnClickListener{
Context context;

    Button btnalreadylogin;
    Button btnhomeregister;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         if(!isConnected(Welcome.this))
        {buildDialog(Welcome.this).show();
        }
        else {

        }
        setContentView(R.layout.activity_welcome);
        btnalreadylogin=(Button)findViewById(R.id.btnalreadylogin);
        btnhomeregister=(Button)findViewById(R.id.btnhomeregister);
        btnalreadylogin.setOnClickListener(this);
        btnhomeregister.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        if (v == btnalreadylogin) {
            Intent i = new Intent(Welcome.this, Login.class);
            startActivity(i);
            finish();

        } else if (v == btnhomeregister) {
            Intent i = new Intent(Welcome.this, Register.class);
            startActivity(i);
            finish();
        }
    }
    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else return false;
        } else
            return false;
    }


    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(!isConnected(Welcome.this))
                {buildDialog(Welcome.this).show();
                }
                else {
                }
            }
        });

        return builder;
    }



}
