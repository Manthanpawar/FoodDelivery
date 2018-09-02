package foodxpress.foodxpress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class restaurantpartner extends AppCompatActivity {
private Toolbar restpartner;
private Button btnrestpartner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurantpartner);
        restpartner=(Toolbar)findViewById(R.id.app_baar_partnerwithus);
        setSupportActionBar(restpartner);
        getSupportActionBar().setTitle("Partner with us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnrestpartner=(Button)findViewById(R.id.btnregisterrestaurant);
        btnrestpartner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(restaurantpartner.this,hotelregistration.class);
                startActivity(i);
            }
        });
    }
}
