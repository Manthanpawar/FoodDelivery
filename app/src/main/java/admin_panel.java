package foodxpress.foodxpress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class admin_panel extends AppCompatActivity {
private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);
        toolbar=(Toolbar)findViewById(R.id.app_baar_admin);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Welcome Admin");

    }

    public void viewusers(View view) {
    }

    public void viewBranch(View view) {
    }
}
