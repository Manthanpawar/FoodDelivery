package foodxpress.foodxpress;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Login extends AppCompatActivity {
    Button btnlogin;
    private ProgressDialog progressDialog;
    TextInputLayout emailid;
    TextInputLayout password;
    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private FirebaseUser currentuser;
    private Toolbar toolbar;
    private int flag=0;
    private RadioButton choiceuser,choicerestaurant;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog=new ProgressDialog(this);
        toolbar=(Toolbar)findViewById(R.id.app_login_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Sign In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();
        emailid=(TextInputLayout)findViewById(R.id.txtloginemail);
        password=(TextInputLayout)findViewById(R.id.txtloginpassword);
        choiceuser=(RadioButton)findViewById(R.id.choiceuser);
        choicerestaurant=(RadioButton)findViewById(R.id.choicerest);

        btnlogin=(Button)findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    String email = emailid.getEditText().getText().toString();
                    String pwd = password.getEditText().getText().toString();
                    //fetch flag for restaurant
                    if (choiceuser.isChecked()) {
                        flag = 0;
                    } else {
                        flag = 1;
                    }

                    //admin panel
                    if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(pwd)) {
                        if (email.equals("adminpawar70@gmail.com") && pwd.equals("pawar")) {
                            progressDialog.setTitle("Admin Loggin In");
                            progressDialog.setMessage("Please Wait while we check your credential");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            Intent i = new Intent(Login.this, admin_panel.class);
                            startActivity(i);
                            finish();
                        } else {

                            progressDialog.setTitle("Loggin In");
                            progressDialog.setMessage("Please Wait while we check your credential");
                            progressDialog.setCanceledOnTouchOutside(false);
                            progressDialog.show();
                            if (flag == 0) {

                                login(email, pwd);
                                emailid.getEditText().setText("");
                                emailid.requestFocus();
                                password.getEditText().setText("");


                            } else if (flag == 1) {

                                restlogin(email, pwd);
                                emailid.getEditText().setText("");
                                emailid.requestFocus();
                                password.getEditText().setText("");

                            } else {
                                Toast.makeText(Login.this, "You are not Registered user", Toast.LENGTH_LONG).show();
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Please fill information proper", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {
                    mAuth.signOut();
                    Intent i =new Intent(getApplicationContext(),Login.class);
                    startActivity(i);}
            }
        });
    }




    private void login(String email, String pwd) {
        try
        {


        mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                     FirebaseUser users=FirebaseAuth.getInstance().getCurrentUser();
                    boolean emailverified=users.isEmailVerified();
                    if(!emailverified)
                    {
                        Toast.makeText(Login.this,"Verify your Email First",Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                        progressDialog.dismiss();
                    }else {
                        progressDialog.dismiss();
                        Intent i = new Intent(Login.this, welcome_page.class);
                        startActivity(i);
                    }
                }
                else {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(),"Something wrong. Please Check Your Information",Toast.LENGTH_LONG).show();
                }
            }
        });}
        catch (Exception e)

        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
    private void restlogin(String email, String pwd) {
        try
        {


            mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        FirebaseUser users=FirebaseAuth.getInstance().getCurrentUser();
                        boolean emailverified=users.isEmailVerified();
                        if(!emailverified)
                        {
                            Toast.makeText(Login.this,"Verify your Email First",Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                            progressDialog.dismiss();
                        }else {
                            progressDialog.dismiss();
                            Intent i = new Intent(Login.this, welcome_restaurant.class);
                            startActivity(i);
                        }
                    }
                    else {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(),"Something wrong. Please Check Your Information",Toast.LENGTH_LONG).show();
                    }
                }
            });}
        catch (Exception e)

        {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void signup(View view) {
        Intent i=new Intent(Login.this,Register.class);
        startActivity(i);
    }

    public void forgotpassword(View view) {
        Intent forgotintent=new Intent(Login.this,forgotpassword.class);
        startActivity(forgotintent);
    }
}
