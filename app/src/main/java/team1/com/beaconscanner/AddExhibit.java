package team1.com.beaconscanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import pub.devrel.easypermissions.EasyPermissions;
import team1.com.beaconscanner.exhibit.Exhibit;

public class AddExhibit extends AppCompatActivity {
    private ExhibitFirebase exhibitFirebase;
    private EditText titleEditText;
    private EditText aboutEditText;
    private TextView addressTextView;
    private ImageView imageView;

    public static final int PICTURE = 1;
    private String[] galleryPermissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    private Uri imageDownloadPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exhibit);

        titleEditText = (EditText) findViewById(R.id.title);
        aboutEditText = (EditText) findViewById(R.id.about);
        addressTextView = (TextView) findViewById(R.id.address);
        imageView = (ImageView) findViewById(R.id.image);

        exhibitFirebase = new ExhibitFirebase(null);
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);
        final Context thisContext = this;

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToFirebase();
            }
        });
        findViewById(R.id.button_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (EasyPermissions.hasPermissions(thisContext, galleryPermissions)) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    intent.setType("image/*");

                    startActivityForResult(intent, PICTURE);
                }
                else {
                    EasyPermissions.requestPermissions(thisContext, "Access for storage",101, galleryPermissions);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            String[] prjection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, prjection,null,null,null);

            cursor.moveToFirst();

            String path = cursor.getString(cursor.getColumnIndex(prjection[0]));

            cursor.close();

            Drawable d = new BitmapDrawable(BitmapFactory.decodeFile(path));

            imageView.setBackground(d);
            // Picasso.with(AddExhibit.this).load(path).fit().centerCrop().into(imageView);
            // imageView.setImageBitmap(BitmapFactory.decodeFile(path));

            mProgressDialog.setMessage("Uploading ...");
            mProgressDialog.show();

            StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();

                    imageDownloadPath = taskSnapshot.getDownloadUrl();

                    Picasso.with(AddExhibit.this).load(imageDownloadPath).fit().centerCrop().into(imageView);
                    Toast.makeText(AddExhibit.this,"upload done", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void saveToFirebase() {
        if (titleEditText.getText().toString().equals("")) Toast.makeText(getBaseContext(), "Zadajte názov exponátu.", Toast.LENGTH_SHORT).show();
        else if (aboutEditText.getText().toString().equals("")) Toast.makeText(getBaseContext(), "Zadajte popis exponátu.", Toast.LENGTH_SHORT).show();
        else if (addressTextView.getText().toString().equals("")) Toast.makeText(getBaseContext(), "Zadajte adresu beaconu.", Toast.LENGTH_SHORT).show();
        else if (imageDownloadPath != null) Toast.makeText(getBaseContext(), "Zadajte obrázok exponátu.", Toast.LENGTH_SHORT).show();
        else {
            Exhibit exhibit = new Exhibit("", titleEditText.getText().toString(), aboutEditText.getText().toString(), imageDownloadPath.toString(), addressTextView.getText().toString());

            exhibitFirebase.add(exhibit);

            Toast.makeText(getBaseContext(), "Exponát bol uložený", Toast.LENGTH_SHORT).show();
        }
    }
}
