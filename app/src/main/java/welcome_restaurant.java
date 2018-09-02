package foodxpress.foodxpress;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class welcome_restaurant extends AppCompatActivity {
private Toolbar toolbaar;
private TextView restname,restemail;
private FirebaseAuth mAuth;
private DatabaseReference db;
private String rest_login_id;
private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_welcome_restaurant);
        toolbaar= (Toolbar)findViewById(R.id.app_baar_welcome_restaurant);
        setSupportActionBar(toolbaar);
        getSupportActionBar().setTitle("Restaurant");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        try {
            FirebaseUser currentuser = mAuth.getCurrentUser();
            if (currentuser == null) {
                sendtostart();
            }
            else {
                restname = (TextView)findViewById(R.id.displayrestname);
                restemail = (TextView)findViewById(R.id.displayrestemail);
                currentuser = FirebaseAuth.getInstance().getCurrentUser();
                rest_login_id = currentuser.getUid();
                db = FirebaseDatabase.getInstance().getReference().child("restaurant").child(rest_login_id);
                progressDialog =new ProgressDialog(this);
                progressDialog.setMessage("Loading..");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            String resttname = dataSnapshot.child("RestaurantName").getValue().toString();
                            String email = dataSnapshot.child("owneremail").getValue().toString();
                            restname.setText(resttname);
                            restemail.setText(email);
                            progressDialog.dismiss();
                        }catch (Exception e){
                            FirebaseAuth.getInstance().signOut();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //Toast.makeText(welcome_restaurant.this,"Something Wrong!",Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    private void sendtostart() {
        Intent i=new Intent(welcome_restaurant.this,Welcome.class);
        startActivity(i);
        finish();
    }



    public void addfood(View view) {

        Intent i=new Intent(welcome_restaurant.this,AddFood.class);
        i.putExtra("rest_id",rest_login_id);
        //Toast.makeText(getApplicationContext(),rest_login_id,Toast.LENGTH_LONG).show();
        startActivity(i);
        finish();
    }

    public void restlogout(View view) {
        Intent i=new Intent(welcome_restaurant.this,Login.class);
        startActivity(i);
        mAuth.signOut();
        finish();
    }

    public void viewmenu(View view) {
        Intent i=new Intent(welcome_restaurant.this,restViewFood.class);
        startActivity(i);
        finish();
    }

    public void ViewOrders(View view) {
        Intent i=new Intent(welcome_restaurant.this,view_order.class);
        startActivity(i);
        finish();
    }
}
