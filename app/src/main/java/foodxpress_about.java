package foodxpress.foodxpress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

public class foodxpress_about extends AppCompatActivity {
private Toolbar mtoolbar;
private WebView webabout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodxpress_about);
        mtoolbar=(Toolbar)findViewById(R.id.about_app_baar_layout);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("Foodxpress About");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webabout=(WebView)findViewById(R.id.webabout);
        webabout.loadUrl("file:///assets/About.html");
    }
}
