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

    public static final int PICTURE= 1;
    private String[] galleryPermissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    private Uri selectedImageDataUri;
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
            selectedImageDataUri = data.getData();
            Picasso.with(AddExhibit.this)
                    .load(selectedImageDataUri)
//                    .placeholder(R.drawable.ic_action_name)
//                    .error(R.drawable.user_placeholder_error)
//                    .resize(800,800)
                    .centerCrop()
                    .fit()
                    .into(imageView);

            StorageReference filepath = mStorage.child("Photos").child(selectedImageDataUri.getLastPathSegment());

            filepath.putFile(selectedImageDataUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();
                    imageDownloadPath = taskSnapshot.getDownloadUrl();
//                    Picasso.with(AddExhibit.this).load(imageDownloadPath).centerCrop().fit().into(imageView);
                    Toast.makeText(AddExhibit.this, getString(R.string.addExhibit_upload_done), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void saveToFirebase() {
        if (titleEditText.getText().toString().equals("")) Toast.makeText(getBaseContext(), getString(R.string.addExhibit_validation_name), Toast.LENGTH_SHORT).show();
        else if (aboutEditText.getText().toString().equals("")) Toast.makeText(getBaseContext(), getString(R.string.addExhibit_validation_about), Toast.LENGTH_SHORT).show();
        else if (addressTextView.getText().toString().equals("")) Toast.makeText(getBaseContext(), getString(R.string.addExhibit_validation_id), Toast.LENGTH_SHORT).show();
        else if (selectedImageDataUri == null) Toast.makeText(getBaseContext(), getString(R.string.addExhibit_validation_image), Toast.LENGTH_SHORT).show();
        else {

            Exhibit exhibit = new Exhibit("", titleEditText.getText().toString(), aboutEditText.getText().toString(), imageDownloadPath.toString(), addressTextView.getText().toString());

            exhibitFirebase.add(exhibit);

            Toast.makeText(getBaseContext(), getString(R.string.addExhibit_saved), Toast.LENGTH_SHORT).show();
        }
    }
}