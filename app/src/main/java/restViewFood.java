package foodxpress.foodxpress;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class restViewFood extends AppCompatActivity {
private RecyclerView menuRecyclerView;
    private RecyclerView recyclerView;
    private DatabaseReference dbfood,dbrest;
    private Query foodquery;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentuser;
    private String restkey,temprestid;
    private ProgressDialog mProgressDialog;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_view_food);
        toolbar=(Toolbar)findViewById(R.id.view_food_app_baar_layout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressDialog=new ProgressDialog(this);
        recyclerView =(RecyclerView)findViewById(R.id.menu_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCurrentuser=FirebaseAuth.getInstance().getCurrentUser();
        restkey=mCurrentuser.getUid();
        dbrest= FirebaseDatabase.getInstance().getReference().child("restaurant");
        dbfood=FirebaseDatabase.getInstance().getReference().child("Item");
        foodquery=dbfood.orderByKey();
        dbrest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    temprestid=child.getKey();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Food> firebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(foodquery,Food.class)
                .build();
        FirebaseRecyclerAdapter<Food,foodViewHolder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Food, foodViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull foodViewHolder holder, int position, @NonNull Food model) {
                if (restkey.toString().equals(temprestid.toString())) {

                    holder.setItemname(model.getItemName());
                    holder.setItemdesc(model.getItemDescription());
                    holder.setItemprice(model.getItemPrice());
                    holder.setItemQuantity(model.getItemQuantity());
                    holder.setimage(model.getImage(), getApplicationContext());
                    final String positionkey=getRef(position).getKey().toString();
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i=new Intent(restViewFood.this,EditFood.class);
                            i.putExtra("food_key",positionkey);
                            //Toast.makeText(restViewFood.this, positionkey, Toast.LENGTH_SHORT).show();
                            i.putExtra("rest_key",restkey);
                            startActivity(i);
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(),"No Menu Found",Toast.LENGTH_SHORT).show();
                }
            }

            @NonNull
            @Override
            public foodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.menu_single_layout,parent,false);
                return new restViewFood.foodViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }



    public static class foodViewHolder extends RecyclerView.ViewHolder{
        private TextView displayMenuName,displayMenuDescrption,displayMenuPrice,displayMenuQuantity;
        private ImageView imgmenu;
            View view;
        public foodViewHolder(View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setItemname(String ItemName)
        {
            displayMenuName=(TextView)itemView.findViewById(R.id.txtmenuname);
            displayMenuName.setText(ItemName);
        }
        public void setItemdesc(String ItemDescription)
        {
            displayMenuDescrption=(TextView)itemView.findViewById(R.id.txtmenudesc);
            displayMenuDescrption.setText(ItemDescription);
        }
        public void setItemprice(String ItemPrice)
        {
            displayMenuPrice=(TextView)itemView.findViewById(R.id.txtmenuprice);
            displayMenuPrice.setText(ItemPrice);
        }
        public void setimage(String image,Context ctx)
        {
            imgmenu=(ImageView)itemView.findViewById(R.id.imgmenu);
            Picasso.with(ctx).load(image).placeholder(R.drawable.avtar).into(imgmenu);

        }
        public void setItemQuantity(String ItemQuantity)
        {
            displayMenuQuantity=(TextView)itemView.findViewById(R.id.txtmenuquantity);
            displayMenuQuantity.setText(ItemQuantity);
        }


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
                Intent i=new Intent(restViewFood.this,welcome_page.class);
                startActivity(i);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}

