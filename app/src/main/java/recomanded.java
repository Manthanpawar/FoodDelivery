package foodxpress.foodxpress;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class recomanded extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference dbfood,dbrest;
    private Query foodquery;
    private FirebaseAuth mAuth;
    private FirebaseUser currentuser;
    private String restkey;
    private ProgressDialog mProgressDialog;

    public recomanded() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recomanded, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressDialog=new ProgressDialog(getContext());

        dbrest=FirebaseDatabase.getInstance().getReference().child("restaurant");
        dbrest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child:dataSnapshot.getChildren())
                {
                    restkey=child.getKey();

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                    //Toast.makeText(getActivity(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        dbfood=FirebaseDatabase.getInstance().getReference().child("Item");
        foodquery=dbfood.orderByKey();
        recyclerView=(RecyclerView)view.findViewById(R.id.food_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Food> firebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(foodquery,Food.class)
                .build();
        FirebaseRecyclerAdapter<Food,userViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Food, userViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(userViewHolder holder, int position, Food model) {
                holder.setItemname(model.getItemName());
                holder.setItemdesc(model.getItemDescription());
                holder.setItemprice(model.getItemPrice());
                holder.setimage(model.getImage(),getContext());
                final String positionkey=getRef(position).getKey().toString();
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mProgressDialog.setMessage("Loading...");
                        mProgressDialog.setCanceledOnTouchOutside(false);
                        mProgressDialog.show();
                        Intent food_order=new Intent(getContext(),Food_order.class);
                        food_order.putExtra("food_select_key",positionkey);
                        //Toast.makeText(getContext(), positionkey, Toast.LENGTH_SHORT).show();
                        food_order.putExtra("rest_key",restkey);
                        startActivity(food_order);
                        mProgressDialog.dismiss();
                    }
                });
            }

            @Override
            public userViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_single_layout,parent,false);
                return new userViewHolder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class userViewHolder extends RecyclerView.ViewHolder{
        private TextView displayitemname,displayitemdesc,displayitemprice,displayquantity;
        private ImageView displayfoodimage;
        View view;


        public userViewHolder(View itemView) {
            super(itemView);
            view=itemView;
        }
        public void setItemname(String ItemName)
        {
            displayitemname=(TextView)itemView.findViewById(R.id.itemname);
            displayitemname.setText(ItemName);
        }
        public void setItemdesc(String ItemDescription)
        {
            displayitemdesc=(TextView)itemView.findViewById(R.id.itemdesc);
            displayitemdesc.setText(ItemDescription);
        }
        public void setItemprice(String ItemPrice)
        {
            displayitemprice=(TextView)itemView.findViewById(R.id.itemprice);
            displayitemprice.setText(ItemPrice);
        }
        public void setimage(String image,Context ctx)
        {
            displayfoodimage=(ImageView)itemView.findViewById(R.id.imgfooditem);
            Picasso.with(ctx).load(image).placeholder(R.drawable.avtar).into(displayfoodimage);

        }
        public void setItemQuantity(String ItemQuantity)
        {
            displayquantity=(TextView)itemView.findViewById(R.id.txtquantity);
            displayquantity.setText(ItemQuantity);
        }


    }
}
