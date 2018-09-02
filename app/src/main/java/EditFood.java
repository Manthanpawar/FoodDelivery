package foodxpress.foodxpress;


import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


public class EditFood extends AppCompatActivity {
private Toolbar toolbar;
private String food_key,rest_key,temp_rest_key,temp_food_key,getrestid;
private String getfoodprice,getfoodquantity;
private String displayItemName,displayItemDesc,displayItemimg;
private ImageView displayfoodimg;
private Button btnupdateitem;
private DatabaseReference dbfood,dbrest;
private TextView txtviewname,txtviewdesc;
private TextInputLayout edtprice,edtquantity;
private FirebaseAuth mAuth;
private FirebaseUser currentuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_food);
        toolbar=(Toolbar)findViewById(R.id.edit_food_app_tool_baar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Food");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth=FirebaseAuth.getInstance();
        currentuser=mAuth.getCurrentUser();
        getrestid=currentuser.getUid();
        food_key=getIntent().getExtras().getString("food_key");
        //Toast.makeText(this, food_key, Toast.LENGTH_SHORT).show();
        rest_key=getIntent().getExtras().getString("rest_key");
        dbfood= FirebaseDatabase.getInstance().getReference().child("Item");

        dbrest=FirebaseDatabase.getInstance().getReference().child("restaurant");
        dbrest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot restchild:dataSnapshot.getChildren()) {
                    temp_food_key=restchild.getKey();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        txtviewname=(TextView)findViewById(R.id.txtviewitemname);
        txtviewdesc=(TextView)findViewById(R.id.txtviewitemdescrtiption);
        edtprice=(TextInputLayout)findViewById(R.id.edtviewprice);
        edtquantity=(TextInputLayout)findViewById(R.id.edtviewquantity);
        displayfoodimg=(ImageView) findViewById(R.id.imgviewitem);
        btnupdateitem=(Button)findViewById(R.id.btnupdateitem);
        btnupdateitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getfoodprice=edtprice.getEditText().getText().toString();
                getfoodquantity=edtquantity.getEditText().getText().toString();
                updatefood(getfoodprice,getfoodquantity);
                Toast.makeText(getApplicationContext(),"Succesfully Update",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(EditFood.this,restViewFood.class);
                startActivity(i);
                edtprice.getEditText().setText(null);
                edtquantity.getEditText().setText(null);

            }
        });
        dbfood.child(food_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                displayItemName=dataSnapshot.child("ItemName").getValue().toString();
                displayItemDesc=dataSnapshot.child("ItemDescription").getValue().toString();
                displayItemimg=(String)dataSnapshot.child("Image").getValue().toString();
                Picasso.with(EditFood.this).load(displayItemimg).into(displayfoodimg);
                txtviewname.setText(displayItemName);
                txtviewdesc.setText(displayItemDesc);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                }
        });
    }

    private void updatefood(String foodprice,String quantity) {
       final DatabaseReference dbitemref=FirebaseDatabase.getInstance().getReference("Item").child(food_key);
        dbitemref.child("ItemPrice").setValue(foodprice);
        dbitemref.child("ItemQuantity").setValue(quantity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"Logout");
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case 0:
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(EditFood.this,welcome_page.class);
                startActivity(i);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
