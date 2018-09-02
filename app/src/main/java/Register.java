package foodxpress.foodxpress;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
   private TextInputLayout fname , lname, contactno,email,password,repeatepassword;

    Button btnregister;
    Toolbar toolbar;
    private DatabaseReference db;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mAuth = FirebaseAuth.getInstance();
        toolbar=(Toolbar)findViewById(R.id.app_baar_register);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog =new ProgressDialog(this);
        fname = (TextInputLayout) findViewById(R.id.txtfname);
        lname = (TextInputLayout) findViewById(R.id.txtlname);
        contactno = (TextInputLayout)findViewById(R.id.txtcontactno);
        email=(TextInputLayout)findViewById(R.id.txtemail);
        password=(TextInputLayout)findViewById(R.id.txtpassword);
        repeatepassword=(TextInputLayout)findViewById(R.id.txtrepeatepassword);
        btnregister=(Button)findViewById(R.id.btnregister);
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String firstname = fname.getEditText().getText().toString();
                    String lastname = lname.getEditText().getText().toString();
                    String contact_no = contactno.getEditText().getText().toString();
                    String email_id = email.getEditText().getText().toString();
                    String pwd = password.getEditText().getText().toString();
                    String rptpassword = repeatepassword.getEditText().getText().toString();


                    if(firstname.isEmpty()||firstname.length()>15||firstname.matches("[a-z A-Z]"))
                    {
                        fname.setError("Field cannot Empty");
                        fname.requestFocus();
                        if (firstname.length()<3)
                        {
                            fname.getEditText().setError("Enter Valid Name");
                            fname.getEditText().requestFocus();
                        }

                    }
                    if(lastname.isEmpty()||lastname.length()>15||firstname.matches("[a-z A-Z]"))
                    {
                        lname.getEditText().setError("Last Name Require");
                        lname.getEditText().requestFocus();
                        if (lastname.length()<3)
                        {
                            lname.getEditText().setError("Enter Valid Name");
                            lname.getEditText().requestFocus();
                        }
                    }
                    if(contact_no.isEmpty()||contact_no.length()<13||!Patterns.PHONE.matcher(contact_no).matches())
                    {
                        contactno.getEditText().setError("Enter Valid Contact no");
                        contactno.getEditText().requestFocus();
                    }
                    if(email_id.isEmpty()||!Patterns.EMAIL_ADDRESS.matcher(email_id).matches())
                    {
                        email.getEditText().setError("Please Enter Valid Email Address");
                        email.getEditText().requestFocus();
                    }
                    if(pwd.isEmpty()||pwd.length()>20)
                    {
                        password.getEditText().setError("Password Require");
                        password.getEditText().requestFocus();
                    }
                    if(rptpassword.isEmpty()||pwd.length()>20)
                    {
                        repeatepassword.getEditText().setError("Repeat Password Require");
                        repeatepassword.getEditText().requestFocus();
                    }
                    if(!TextUtils.isEmpty(firstname)||
                            !TextUtils.isEmpty(lastname)||
                            !TextUtils.isEmpty(contact_no)||
                            TextUtils.isEmpty(email_id)||
                            TextUtils.isEmpty(pwd)||
                            !TextUtils.isEmpty(rptpassword)) {

                        if(!password.getEditText().getText().toString().equals(repeatepassword.getEditText().getText().toString())) {

                            repeatepassword.getEditText().setError("Password does not match");
                        }
                        else
                        {

                            register(firstname, lastname, contact_no, email_id, pwd);
                            progressDialog.setTitle("Registering user!!!");
                            progressDialog.setMessage("Please Wait for the registering...");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();

                        }

                    }

                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private void startmainpage() {


        Intent i=new Intent(Register.this,Login.class);
        startActivity(i);

        sendEmailVerification();
        mAuth.signOut();
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
                        Toast.makeText(Register.this,"Check Your Email for Verification",Toast.LENGTH_LONG).show();

                    }
                }
            });
        }
    }

    private void register(final String firstname, final String lastname, final String contact_no, final String email_id, final String pwd) {
        mAuth.createUserWithEmailAndPassword(email_id,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    try {
                        progressDialog.dismiss();

                        FirebaseUser currentuser = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = currentuser.getUid();
                        db = FirebaseDatabase.getInstance().getReference().child("userregister").child(uid);
                        HashMap<String, String> usemap = new HashMap<>();
                        usemap.put("FirstName", firstname);
                        usemap.put("LastName", lastname);
                        usemap.put("ContactNo", contact_no);
                        usemap.put("EmailId", email_id);
                        usemap.put("Password", pwd);
                        db.setValue(usemap);
                        startmainpage();
                    }
                    catch (Exception e)
                    {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    }

                }
                else
                {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(),"You Need To check your Information!",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
