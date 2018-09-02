package foodxpress.foodxpress;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

public class view_order extends AppCompatActivity {
private Toolbar mtoolbaar;
private RecyclerView recyclerView;
private FirebaseAuth mAuth;
private FirebaseUser currentuser;
private DatabaseReference dbitem,dbrest;
private String temp_food_id,temp_rest_id;
private Query itemquery;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);
        mtoolbaar=(Toolbar)findViewById(R.id.view_order_app_baar);
        setSupportActionBar(mtoolbaar);
        getSupportActionBar().setTitle("View Order");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView=(RecyclerView)findViewById(R.id.view_order_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAuth=FirebaseAuth.getInstance();
        dbitem= FirebaseDatabase.getInstance().getReference().child("temp_order");
        itemquery=dbitem.orderByKey();
        dbrest= FirebaseDatabase.getInstance().getReference().child("restaurant");
        dbrest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot child:dataSnapshot.getChildren())
                {
                    temp_rest_id=child.getKey();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Food> firebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(itemquery,Food.class)
                .build();
        FirebaseRecyclerAdapter<Food,restordersviewholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter
                <Food, restordersviewholder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull restordersviewholder holder, int position, @NonNull Food model) {
                holder.setItemname(model.getItemName());
                holder.setItemdesc(model.getItemDescription());
                holder.setItemprice(model.getItemPrice());
                holder.setItemQuantity(model.getItemQuantity());
                holder.setimage(model.getImage(), getApplicationContext());
            }

            @NonNull
            @Override
            public restordersviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        //firebaseRecyclerAdapter.startListening();
    }
    public static class restordersviewholder extends RecyclerView.ViewHolder
    {
        private TextView displayMenuName,displayMenuDescrption,displayMenuPrice,displayMenuQuantity;
        private ImageView imgmenu;

        public restordersviewholder(View itemView) {

            super(itemView);

        }
        public void setItemname(String ItemName)
        {
            displayMenuName=(TextView)itemView.findViewById(R.id.orderitemname);
            displayMenuName.setText(ItemName);
        }
        public void setItemdesc(String ItemDescription)
        {
            displayMenuDescrption=(TextView)itemView.findViewById(R.id.orderitemdesc);
            displayMenuDescrption.setText(ItemDescription);
        }
        public void setItemprice(String ItemPrice)
        {
            displayMenuPrice=(TextView)itemView.findViewById(R.id.orderitemprice);
            displayMenuPrice.setText(ItemPrice);
        }
        public void setimage(String image,Context ctx)
        {
            imgmenu=(ImageView)itemView.findViewById(R.id.orderimgfooditem);
            Picasso.with(ctx).load(image).placeholder(R.drawable.avtar).into(imgmenu);

        }
        public void setItemQuantity(String ItemQuantity)
        {
            displayMenuQuantity=(TextView)itemView.findViewById(R.id.ordertxtdisplayquantity);
            displayMenuQuantity.setText(ItemQuantity);
        }

    }

}
