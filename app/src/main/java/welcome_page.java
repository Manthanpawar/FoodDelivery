package foodxpress.foodxpress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

public class welcome_page extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {

    private FragmentManager fragmentManager;
    private FirebaseAuth mAuth;
    private DatabaseReference db;
    TextView display_name;
    TextView display_email;
    private View headerview;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isConnected(welcome_page.this))
        {buildDialog(welcome_page.this).show();
        }
        else {
        }
        setContentView(R.layout.activity_welcome_page);


        fragmentManager = getSupportFragmentManager();
        mAuth=FirebaseAuth.getInstance();
        //FirebaseAuth.getInstance().signOut();
        //mAuth.signOut();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        headerview=navigationView.getHeaderView(0);

        loadNavHeader();
        showFragment(new fragment_welcome());
    }

    private void loadNavHeader() {
        try {
            FirebaseUser currentuser = mAuth.getCurrentUser();
            if (currentuser == null) {
                sendtostart();
            }
            else {
                display_name = (TextView)headerview.findViewById(R.id.welcome_display_name);
                display_email = (TextView)headerview.findViewById(R.id.welcome_display_email);
                currentuser = FirebaseAuth.getInstance().getCurrentUser();
                String login_id = currentuser.getUid();
                db = FirebaseDatabase.getInstance().getReference().child("userregister").child(login_id);
                progressDialog =new ProgressDialog(this);
                progressDialog.setMessage("Loading..");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {


                            String firstname = dataSnapshot.child("FirstName").getValue().toString();
                            String lastname = dataSnapshot.child("LastName").getValue().toString();
                            String email = dataSnapshot.child("EmailId").getValue().toString();
                            String fullname = firstname + " " + lastname;
                            display_name.setText(fullname);
                            display_email.setText(email);
                            progressDialog.dismiss();
                        }
                        catch (Exception e)
                        {
                            FirebaseAuth.getInstance().signOut();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        } catch (Exception e) {
           // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            finish();
        } else {
            super.onBackPressed();
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        displaySelectedScreen(item.getItemId());
        return true;
    }

    private void displaySelectedScreen(int itemId) {
        switch (itemId) {
            case R.id.nav_restaurant:

                Intent welcomeintent=new Intent(this,welcome_page.class);
                startActivity(welcomeintent);
                //showFragment(new fragment_welcome());

                break;
            case R.id.nav_pastorder:
                showFragment(new fragment_pastOrder());
                break;
            case R.id.nav_settings:
                showFragment(new fragment_settings());
                break;
            case R.id.nav_about:
                showFragment(new fragment_about());
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(welcome_page.this, Login.class);
                startActivity(i);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void showFragment(Fragment fragment) {
        if (fragment == null) {
            return;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commitNow();
    }


   @Override
    protected void onStart() {
        super.onStart();


            try {
                FirebaseUser currentuser = mAuth.getCurrentUser();
                if (currentuser == null) {
                    sendtostart();
                }
            } catch (Exception e) {
               // Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

            }

    }


    private void sendtostart() {
        Intent i=new Intent(welcome_page.this,Welcome.class);
        startActivity(i);
        finish();
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

                if(!isConnected(welcome_page.this))
                {buildDialog(welcome_page.this).show();
                }
                else {

                }
            }
        });

        return builder;
    }



}
