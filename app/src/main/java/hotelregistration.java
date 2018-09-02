package foodxpress.foodxpress;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class hotelregistration extends AppCompatActivity {
private Toolbar resttool;
private TextInputLayout txtowneremail,txtrestpassword,txtrestrptpassword,txtrestname,
        txtrestregname,txtrestoutletaddress,txtrestbusinessaddress,txtownername,txtrestcontactno,
        txtrestcity,txtreststate;
private Button btnrestreg;
private ProgressDialog restprogressDialog;
private FirebaseAuth mAuth;
private DatabaseReference db;
private int flag=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotelregistration);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //authntication firebase
        mAuth = FirebaseAuth.getInstance();
        //toolbaar restaurant
        resttool=(Toolbar)findViewById(R.id.app_baar_restaurant);

        setSupportActionBar(resttool);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //progress dialog
        restprogressDialog =new ProgressDialog(this);
        // /text view
        txtowneremail=(TextInputLayout)findViewById(R.id.txtrestowneremail);
        txtrestpassword=(TextInputLayout)findViewById(R.id.txtrestpassword);
        txtrestrptpassword=(TextInputLayout)findViewById(R.id.txtrestrptpassword);
        txtrestname=(TextInputLayout)findViewById(R.id.txtrestname);
        txtrestregname=(TextInputLayout)findViewById(R.id.txtrestregistername);
        txtrestoutletaddress=(TextInputLayout)findViewById(R.id.txtrestoutletaddress);
        txtrestbusinessaddress=(TextInputLayout)findViewById(R.id.txtrestbusinessaddress);
        txtownername=(TextInputLayout)findViewById(R.id.txtrestOwnername);
        txtrestcontactno=(TextInputLayout)findViewById(R.id.txtrestownercontactno);
        txtrestcity=(TextInputLayout)findViewById(R.id.txtrestcity);
        txtreststate=(TextInputLayout)findViewById(R.id.txtreststate);
        //button restraurant register
        btnrestreg=(Button)findViewById(R.id.btnrestregister);
        btnrestreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String restowneremail = txtowneremail.getEditText().getText().toString();
                    String restpwd=txtrestpassword.getEditText().getText().toString();
                    String restrptpwd=txtrestrptpassword.getEditText().getText().toString();
                    String restname=txtrestname.getEditText().getText().toString();
                    String restregname=txtrestregname.getEditText().getText().toString();
                    String restoutletaddress=txtrestoutletaddress.getEditText().getText().toString();
                    String restbusinessaddress=txtrestbusinessaddress.getEditText().getText().toString();
                    String restownername=txtownername.getEditText().getText().toString();
                    String restcontactno=txtrestcontactno.getEditText().getText().toString();
                    String restcity=txtrestcity.getEditText().getText().toString();
                    String reststate=txtreststate.getEditText().getText().toString();

                    if(restowneremail.isEmpty())
                    {
                        txtowneremail.setError("Field Required!");
                    }
                    else if(!Patterns.EMAIL_ADDRESS.matcher(restowneremail).matches())
                    {
                        txtowneremail.setError("Enter Valid Email");
                    }
                    else
                    {
                        txtowneremail.setError(null);
                    }
                    if(restpwd.isEmpty()||restpwd.length()>=20)
                    {
                        txtrestpassword.setError("Field Required!");
                    }
                    else
                    {
                        txtrestpassword.setError(null);
                    }
                    if(restrptpwd.isEmpty()||restrptpwd.length()>=20)
                    {
                        txtrestrptpassword.setError("Field Required!");
                    }
                    else
                    {
                        txtrestrptpassword.setError(null);
                    }
                    if(restname.isEmpty()||restname.length()>=20||restname.matches("[a-z A-Z]"))
                    {
                        txtrestname.setError("Field Required!");
                    }
                    else
                    {
                        txtrestname.setError(null);
                    }
                    if(restregname.isEmpty()||restregname.length()>=20||restregname.matches("[a-z A-Z]"))
                    {
                        txtrestname.setError("Field Required!");
                    }
                    else
                    {
                        txtrestname.setError(null);
                    }
                    if(restoutletaddress.isEmpty())
                    {
                        txtrestoutletaddress.setError("Field Required!");
                    }
                    else
                    {
                        txtrestoutletaddress.setError(null);
                    }
                    if(restbusinessaddress.isEmpty())
                    {
                        txtrestbusinessaddress.setError("Field Required!");
                    }
                    else
                    {
                        txtrestbusinessaddress.setError(null);
                    }
                    if(restownername.isEmpty()||restownername.length()>=20||restownername.matches("[a-z A-Z]"))
                    {
                        txtownername.setError("Field Required!");
                    }
                    else
                    {
                        txtownername.setError(null);
                    }
                    if(restcontactno.isEmpty()||restcontactno.length()<13)
                    {
                        txtrestcontactno.setError("Field Required!");
                    }
                    else if(!Patterns.PHONE.matcher(restcontactno).matches())
                    {
                        txtrestcontactno.setError("Please Enter Valid Mobile Number");
                    }
                    else
                    {
                        txtrestcontactno.setError(null);
                    }
                    if(restcity.isEmpty()||restcity.length()>=20||restcity.matches("[a-z A-Z]"))
                    {
                        txtrestcity.setError("Field Required!");
                    }
                    else
                    {
                        txtrestcity.setError(null);
                    }
                    if(reststate.isEmpty()||reststate.length()>=20||reststate.matches("[a-z A-Z]"))
                    {
                        txtreststate.setError("Field Required!");
                    }
                    else
                    {
                        txtreststate.setError(null);
                    }
                    if(!TextUtils.isEmpty(restowneremail)||
                            !TextUtils.isEmpty(restpwd)||
                            !TextUtils.isEmpty(restrptpwd)||
                            !TextUtils.isEmpty(restname)||
                            !TextUtils.isEmpty(restregname)||
                            !TextUtils.isEmpty(restoutletaddress)||
                            !TextUtils.isEmpty(restbusinessaddress)||
                            !TextUtils.isEmpty(restownername)||
                            !TextUtils.isEmpty(restcontactno)||
                            !TextUtils.isEmpty(restcity)||
                            !TextUtils.isEmpty(reststate)) {
                        if(!txtrestpassword.getEditText().getText().toString().equals(txtrestrptpassword.getEditText().getText().toString())) {

                            txtrestrptpassword.setError("Password does not match");
                        }
                        else {
                            restregister(restowneremail,
                                    restpwd,
                                    restname,
                                    restregname,
                                    restoutletaddress,
                                    restbusinessaddress,
                                    restownername,
                                    restcontactno,
                                    restcity,
                                    reststate,
                                    flag);
                            restprogressDialog.setTitle("Registering restaurant!!!");
                            restprogressDialog.setMessage("Please Wait for the registering...");
                            restprogressDialog.setCanceledOnTouchOutside(false);
                            restprogressDialog.show();
                        }
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(hotelregistration.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void startrestaurant() {


        Intent i=new Intent(hotelregistration.this,Login.class);
        startActivity(i);
        mAuth.signOut();
        sendEmailVerification();

    }

    private void sendEmailVerification() {
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null)
        {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(hotelregistration.this,"Check Your Email for Verification",Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    private void restregister(final String restowneremail,
                              final String restpwd,
                              final String restname,
                              final String restregname,
                              final String restoutletaddress,
                              final String restbusinessaddress,
                              final String restownername,
                              final String restcontactno,
                              final String restcity,
                              final String reststate,
                              final int flag) {
        mAuth.createUserWithEmailAndPassword(restowneremail,restpwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    try {
                        restprogressDialog.dismiss();
                        FirebaseUser currentuser=FirebaseAuth.getInstance().getCurrentUser();
                        String restuid=currentuser.getUid();
                        db= FirebaseDatabase.getInstance().getReference().child("restaurant").child(restuid);
                        HashMap<String,String> usemap=new HashMap<>();
                        usemap.put("owneremail",restowneremail);
                        usemap.put("password",restpwd);
                        usemap.put("RestaurantName",restname);
                        usemap.put("RegisteredName",restregname);
                        usemap.put("OutletAddress",restoutletaddress);
                        usemap.put("BusinessAddress",restbusinessaddress);
                        usemap.put("OwnerName",restownername);
                        usemap.put("ContactNo",restcontactno);
                        usemap.put("City",restcity);
                        usemap.put("State",reststate);
                        db.setValue(usemap);
                        restprogressDialog.hide();
                        startrestaurant();
                    }
                    catch (Exception e)
                    {
                        restprogressDialog.hide();
                        Toast.makeText(hotelregistration.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    restprogressDialog.hide();

                    Toast.makeText(getApplicationContext(),"Something Wrong Please Check your Information!",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
