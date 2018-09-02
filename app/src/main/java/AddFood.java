package foodxpress.foodxpress;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

public class AddFood extends AppCompatActivity {
private ImageButton imgitem;
private String itemname,itemdesc,itemprice,itemquantity,id;
private TextInputLayout edtitemname,edtitemdesc,edtitemprice,edtitemquantity;
private Button btnradditem;
private DatabaseReference dbFood;
private DatabaseReference dbrest;
private StorageReference mStorageReference=null;
private ProgressDialog mProgressDialog;
private Uri uri,resultUri;
private Toolbar toolbar;
private FirebaseUser mCurrentuser;
    private String restid;
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_food);
    toolbar=findViewById(R.id.app_baar_food);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("Add Menu");
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    edtitemname=(TextInputLayout)findViewById(R.id.edtfoodname);
    edtitemdesc=(TextInputLayout)findViewById(R.id.edtfooddesc);
    edtitemprice=(TextInputLayout)findViewById(R.id.edtfoodprice);
    edtitemquantity=(TextInputLayout)findViewById(R.id.edtfoodquantity);
    btnradditem=(Button)findViewById(R.id.btnadditem);
    mProgressDialog =new ProgressDialog(this);

    imgitem=(ImageButton)findViewById(R.id.imgfood);
    imgitem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent galleryintent=new Intent(Intent.ACTION_GET_CONTENT);
            galleryintent.setType("Image/*");
            startActivityForResult(galleryintent,1);
        }

    });
    mCurrentuser=FirebaseAuth.getInstance().getCurrentUser();
    restid=mCurrentuser.getUid();
    //Toast.makeText(getApplicationContext(),restid,Toast.LENGTH_LONG).show();
    dbrest=FirebaseDatabase.getInstance().getReference().child("restaurant").child(restid);
    mStorageReference= FirebaseStorage.getInstance().getReference();
    dbFood= FirebaseDatabase.getInstance().getReference("Item");
    btnradditem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            addFood();
        }
    });
}
private void addFood() {
    try {
         itemname = edtitemname.getEditText().getText().toString();
         itemdesc = edtitemdesc.getEditText().getText().toString();
         itemprice = edtitemprice.getEditText().getText().toString();
         itemquantity = edtitemquantity.getEditText().getText().toString();

        if (!TextUtils.isEmpty(itemname) || !TextUtils.isEmpty(itemdesc) || !TextUtils.isEmpty(itemprice) || TextUtils.isEmpty(itemquantity)) {

            mProgressDialog.setMessage("Loading..");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();
             final StorageReference filepath=mStorageReference.child(resultUri.getLastPathSegment());
             filepath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                 @Override
                 public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                     final Uri image=taskSnapshot.getDownloadUrl();
                     final DatabaseReference additem=dbFood.push();
                     additem.child("ItemName").setValue(itemname);
                     additem.child("ItemDescription").setValue(itemdesc);
                     additem.child("ItemPrice").setValue(itemprice);
                     additem.child("ItemQuantity").setValue(itemquantity);
                     additem.child("Image").setValue(image.toString());
                     additem.child("restid").setValue(restid);
                     mProgressDialog.dismiss();

                     Toast.makeText(getApplicationContext(),"Menu Added",Toast.LENGTH_LONG).show();
                     edtitemname.getEditText().setText(null);
                     edtitemdesc.getEditText().setText(null);
                     edtitemprice.getEditText().setText(null);
                     edtitemquantity.getEditText().setText(null);
                     imgitem.setImageURI(null);
                 }
             });

        } else {
            Toast.makeText(getApplicationContext(), "Please Check your Information You we Enter", Toast.LENGTH_LONG).show();
        }
    }
    catch (Exception e)
    {
        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
    }
}



    @Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if(requestCode==1&&resultCode==RESULT_OK)
    {
        uri=data.getData();
        CropImage.activity(uri).setAspectRatio(1,1).start(this);
        //imgitem.setImageURI(uri);
    }
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
        CropImage.ActivityResult result = CropImage.getActivityResult(data);
        if (resultCode == RESULT_OK) {
            resultUri = result.getUri();
            imgitem.setImageURI(resultUri);
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
            Exception error = result.getError();
            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,0,0,"Logout");
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case 0:
                FirebaseAuth.getInstance().signOut();
                Intent i=new Intent(AddFood.this,welcome_page.class);
                startActivity(i);
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
