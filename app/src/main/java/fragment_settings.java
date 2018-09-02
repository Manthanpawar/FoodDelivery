package foodxpress.foodxpress;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_settings extends Fragment {
private TextInputLayout edtfname,edtlname,edtemail,edtcontactno;
private Button userupdate;
private String fname,lname,email,contactno;
private String disfname,dislname,disemail,discontactno;
private FirebaseAuth mAuth;
private FirebaseUser mcurrentuser;
private DatabaseReference dbuser;

    public fragment_settings() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Update");

        edtfname = (TextInputLayout) view.findViewById(R.id.edtfname);
        edtlname = (TextInputLayout) view.findViewById(R.id.edtlname);
        edtemail = (TextInputLayout) view.findViewById(R.id.edtemail);
        edtcontactno = (TextInputLayout) view.findViewById(R.id.edtcontactno);
        userupdate=(Button)view.findViewById(R.id.btnuserupdate);
        mAuth=FirebaseAuth.getInstance();
        mcurrentuser=mAuth.getCurrentUser();


        dbuser=FirebaseDatabase.getInstance().getReference().child("userregister").child(mcurrentuser.getUid());
        dbuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                disfname=dataSnapshot.child("FirstName").getValue().toString();
                dislname=dataSnapshot.child("LastName").getValue().toString();
                disemail=dataSnapshot.child("EmailId").getValue().toString();
                discontactno=dataSnapshot.child("ContactNo").getValue().toString();

                edtfname.getEditText().setText(disfname);
                edtlname.getEditText().setText(dislname);
                edtemail.getEditText().setText(disemail);
                edtcontactno.getEditText().setText(discontactno);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
        userupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fname = edtfname.getEditText().getText().toString();
                    lname = edtlname.getEditText().getText().toString();
                    email = edtemail.getEditText().getText().toString();
                    contactno=edtcontactno.getEditText().getText().toString();
                    if (TextUtils.isEmpty(fname)||edtfname.getEditText().getText().length()>=3) {
                        edtfname.getEditText().setError("Field should not be Empty");
                    }
                    if (TextUtils.isEmpty(lname)|| edtlname.getEditText().getText().length()>=3) {
                        edtlname.getEditText().setError("Field should not be Empty");
                    }
                    if (TextUtils.isEmpty(contactno)||!Patterns.PHONE.matcher(contactno).matches()) {
                        edtcontactno.getEditText().setError("Field should not be Empty");
                    }
                    if (!TextUtils.isEmpty(fname) || !TextUtils.isEmpty(lname) || !TextUtils.isEmpty(contactno)) {
                        final DatabaseReference updateuser = FirebaseDatabase.getInstance().getReference("userregister").child(mcurrentuser.getUid());
                        updateuser.child("FirstName").setValue(fname);
                        updateuser.child("LastName").setValue(lname);
                        updateuser.child("ContactNo").setValue(contactno);
                        Toast.makeText(getActivity(), "Successfully Updated...", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {

                }

            }
        });

    }


}
