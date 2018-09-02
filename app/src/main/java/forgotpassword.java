package foodxpress.foodxpress;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpassword extends AppCompatActivity {
private Toolbar forgottoolbar;
private TextInputLayout txtresetemail;
private Button btnreset;
private FirebaseAuth mAuth;
private TextView txtmsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        mAuth =FirebaseAuth.getInstance();
        forgottoolbar=(Toolbar)findViewById(R.id.app_baar_forgate_password);
        setSupportActionBar(forgottoolbar);
        getSupportActionBar().setTitle("Forgot Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        txtresetemail=(TextInputLayout)findViewById(R.id.txtresetemail);
        txtmsg=(TextView)findViewById(R.id.lblmsg);
        btnreset=(Button)findViewById(R.id.btnresetpassword);
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=txtresetemail.getEditText().getText().toString();
                if(TextUtils.isEmpty(email)||!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    txtresetemail.setError("Please Enter Valid Email");
                }
                else
                {
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    txtmsg.setText("Password reset link send on your Email, Please check your Email");
                                }
                                else
                                {
                                    txtmsg.setText("Something Wrong..");
                                }
                        }
                    });
                }

            }
        });

    }
}
