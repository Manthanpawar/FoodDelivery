package foodxpress.foodxpress;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.security.cert.CertPathBuilderSpi;

public class Food_order extends AppCompatActivity {
private Toolbar foodtoolbaar;
private TextView itemname,itemdesc,itemprice,total,key;
private TextInputLayout edtquantity,edtorderaddress;
private Button btnorder,btncart;
private ImageView imgfood;
private FirebaseAuth mAuth;
private DatabaseReference dbfood,dbuser;
private FirebaseUser mcurrentUser;
String food_key=null;
String rest_key=null;
private String ItemName,ItemDesc,ItemPrice,ItemQuantity,Total,Image,ans=null,address;
private ProgressDialog mProgressDialog;
private DatabaseReference orderFood,temporderFood;
private String quantityitem,itemkey;
private FirebaseUser currentuser;
private String quantity=null;
private RecyclerView cartRecyclerView;
    private int val2,q;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_order);
        foodtoolbaar=(Toolbar)findViewById(R.id.app_baar_foodorder);
        mProgressDialog=new ProgressDialog(this);
        setSupportActionBar(foodtoolbaar);
        getSupportActionBar().setTitle("Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        food_key=getIntent().getExtras().getString("food_select_key");
        rest_key=getIntent().getExtras().getString("rest_key");
        dbfood=FirebaseDatabase.getInstance().getReference().child("Item");
        dbfood.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()) {
                    try
                    {
                        itemkey=snapshot.getKey().toString();
                        quantityitem=snapshot.child("ItemQuantity").getValue().toString();
                    }catch (Exception e)
                    {}

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //Toast.makeText(this, food_key,Toast.LENGTH_LONG).show();
        itemname=(TextView)findViewById(R.id.txtitemname);
        itemdesc=(TextView)findViewById(R.id.txtitemdesc);
        itemprice=(TextView)findViewById(R.id.txtitemprice);
        total=(TextView)findViewById(R.id.txttotal);
        edtorderaddress=(TextInputLayout)findViewById(R.id.edtorderaddress);
        btnorder=(Button)findViewById(R.id.btntempOrder);

        btnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth=FirebaseAuth.getInstance();
                mcurrentUser=mAuth.getCurrentUser();
                dbuser= FirebaseDatabase.getInstance().getReference().child("userregister").child(mcurrentUser.getUid());
                address=edtorderaddress.getEditText().getText().toString();
                final DatabaseReference temporder=temporderFood.push();
                if (TextUtils.isEmpty(quantity))
                {
                    edtquantity.getEditText().setError("Enter Quantity");
                }
                if(TextUtils.isEmpty(address))
                {
                    edtorderaddress.getEditText().setError("Enter Address");
                }
                temporderFood.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(!TextUtils.isEmpty(quantity)||
                                !TextUtils.isEmpty(address)) {
                            mProgressDialog.setMessage("Please Wait...");
                            mProgressDialog.setCanceledOnTouchOutside(false);
                            mProgressDialog.show();

                            temporder.child("user").setValue(mcurrentUser.getUid());
                            temporder.child("Restaurant").setValue(rest_key);
                            temporder.child("FoodId").setValue(food_key);
                            temporder.child("ItemName").setValue(ItemName);
                            temporder.child("ItemDescription").setValue(ItemDesc);
                            temporder.child("ItemQuantity").setValue(quantity);
                            temporder.child("ItemPrice").setValue(ItemPrice);
                            temporder.child("CustomerAddress").setValue(address);
                            temporder.child("Total").setValue(ans);
                            temporder.child("VerifyOrder").setValue("");
                            temporder.child("Image").setValue(Image.toString());
                            Toast.makeText(getApplicationContext(), "Wait for 30 min. to delivery of Product", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(Food_order.this, welcome_page.class);
                            startActivity(i);
                            updatequantity();
                            mProgressDialog.dismiss();
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please Fill Information",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });


        key=(TextView)findViewById(R.id.key);

        currentuser=FirebaseAuth.getInstance().getCurrentUser();
        temporderFood=FirebaseDatabase.getInstance().getReference().child("temp_order");
        edtquantity=(TextInputLayout)findViewById(R.id.edtquantity);
        edtquantity.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                try{

                Total=itemprice.getText().toString();
                int val1=Integer.parseInt(Total);
                quantity=edtquantity.getEditText().getText().toString();
                val2= Integer.parseInt(quantity);
                int finaltotal=val1*val2;
                ans=String.valueOf(finaltotal);
                if(TextUtils.isEmpty(quantity)){}
                else {total.setText(ans);}}
                catch (Exception e){}
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        imgfood=(ImageView)findViewById(R.id.orderimg);
        dbfood.child(food_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ItemName=(String)dataSnapshot.child("ItemName").getValue();
                ItemDesc=(String)dataSnapshot.child("ItemDescription").getValue();
                ItemPrice=(String) dataSnapshot.child("ItemPrice").getValue();
                ItemQuantity=(String)dataSnapshot.child("ItemQuantity").getValue();
                Image=(String)dataSnapshot.child("Image").getValue();
                itemname.setText(ItemName);
                itemdesc.setText(ItemDesc);
                itemprice.setText(ItemPrice);
                key.setText(rest_key);
                key.setVisibility(View.INVISIBLE);
                Picasso.with(Food_order.this).load(Image).into(imgfood);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    private void updatequantity() {
        int quntty=Integer.parseInt(edtquantity.getEditText().getText().toString());
        int iquntty=Integer.parseInt(ItemQuantity);
        int update=quntty-iquntty;
        String updatedquantity=String.valueOf(update);
       final DatabaseReference itemupdateref=FirebaseDatabase.getInstance().getReference("Item").child(food_key);
       itemupdateref.child("ItemQuantity").setValue(updatedquantity);

    }

}
