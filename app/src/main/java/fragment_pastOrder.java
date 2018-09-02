package foodxpress.foodxpress;



import android.content.Context;

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
public class fragment_pastOrder extends Fragment {
    private RecyclerView recyclerView;
    private DatabaseReference dbitem;
    private FirebaseAuth mAuth;
    private FirebaseUser currentuser;
    private Query orderquery;
    private String restkey;
    private String current;
    private String temp_userid;

    public fragment_pastOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_past_order, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Past Orders");
        mAuth=FirebaseAuth.getInstance();
        currentuser=mAuth.getCurrentUser();
        current=currentuser.getUid();
        dbitem = FirebaseDatabase.getInstance().getReference().child("temp_order");
        orderquery=dbitem.orderByChild(current);
        dbitem.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()) {
                        temp_userid = dataSnapshot1.child("user").getValue().toString();
                    }

                }
                catch (Exception e){}
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerorder);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onStart() {
        super.onStart();
        final String currentuserid=mAuth.getUid();
        FirebaseRecyclerOptions<Food> firebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(orderquery,Food.class)
                .build();
        FirebaseRecyclerAdapter<Food,recomanded.userViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Food, recomanded.userViewHolder>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(recomanded.userViewHolder holder, int position, Food model) {
                //if(temp_userid.toString().equals(current.toString())) {
                    holder.setItemname(model.getItemName());
                    holder.setItemdesc(model.getItemDescription());
                    holder.setItemprice(model.getItemPrice());
                    holder.setItemQuantity(model.getItemQuantity());
                    holder.setimage(model.getImage(), getContext());
                /*}
                else
                {
                    Toast.makeText(getContext(),"Sorry No Order Found!!",Toast.LENGTH_SHORT).show();
                }*/
            }
            @Override
            public recomanded.userViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_single_layout,parent,false);
                return new recomanded.userViewHolder(view);
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
    }
}
