package team1.com.beaconscanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.content.Intent;
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

public class EditExhibit extends AppCompatActivity {
    private ExhibitFirebase exhibitFirebase;
    private EditText titleEditText;
    private EditText aboutEditText;
    private TextView addressTextView;
    private ImageView imageView;
    private Exhibit exhibit;

    public static final int PICTURE = 1;
    private String[] galleryPermissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private StorageReference mStorage;
    private ProgressDialog mProgressDialog;
    private Uri imageDownloadPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exhibit);

        titleEditText = (EditText) findViewById(R.id.title);
        aboutEditText = (EditText) findViewById(R.id.about);
        addressTextView = (TextView) findViewById(R.id.address);
        imageView = (ImageView) findViewById(R.id.image);

        exhibitFirebase = new ExhibitFirebase(null);
        mStorage = FirebaseStorage.getInstance().getReference();
        mProgressDialog = new ProgressDialog(this);
        final Context thisContext = this;

        exhibit = getIntent().getExtras().getParcelable("exhibit");

        fillActivityFields();

        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInFirebase();
            }
        });
        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFromFirebase();
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

            mProgressDialog.setMessage(getString(R.string.editExhibit_uploading));
            mProgressDialog.show();

            StorageReference filepath = mStorage.child("Photos").child(uri.getLastPathSegment());

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgressDialog.dismiss();

                    imageDownloadPath = taskSnapshot.getDownloadUrl();

                    Picasso.with(EditExhibit.this).load(imageDownloadPath).centerCrop().fit().into(imageView);
                    Toast.makeText(EditExhibit.this,getString(R.string.editExhibit_upload_done), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void fillActivityFields() {
        titleEditText.setText(exhibit.getTitle());
        Picasso.with(EditExhibit.this)
                .load( Uri.parse( exhibit.getImagePath() ) )
//                .placeholder(R.drawable.ic_action_name)
//                .error(R.drawable.user_placeholder_error)
//                .resize(800,800)
                .centerCrop()
                .fit()
                .into(imageView)
        ;
        aboutEditText.setText(exhibit.getAbout());
        addressTextView.setText(exhibit.getAddress());
    }

    private void updateInFirebase() {
        if (titleEditText.getText().toString().equals("")) Toast.makeText(getBaseContext(), getString(R.string.editExhibit_validation_name), Toast.LENGTH_SHORT).show();
        else if (aboutEditText.getText().toString().equals("")) Toast.makeText(getBaseContext(), getString(R.string.editExhibit_validation_about), Toast.LENGTH_SHORT).show();
        else if (addressTextView.getText().toString().equals("")) Toast.makeText(getBaseContext(), getString(R.string.editExhibit_validation_id), Toast.LENGTH_SHORT).show();
        else if (imageDownloadPath == null) Toast.makeText(getBaseContext(), getString(R.string.editExhibit_validation_image), Toast.LENGTH_SHORT).show();
        else {
            exhibit.setTitle(titleEditText.getText().toString());
            exhibit.setAbout(aboutEditText.getText().toString());
            exhibit.setImagePath(imageDownloadPath.toString());
            exhibit.setAddress(addressTextView.getText().toString());

            exhibitFirebase.edit(exhibit);

            Toast.makeText(getBaseContext(), getString(R.string.editExhibit_saved), Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFromFirebase() {
        exhibitFirebase.remove(exhibit.getId());

        Toast.makeText(getBaseContext(), "Exponát bol zmazaný", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }
}
