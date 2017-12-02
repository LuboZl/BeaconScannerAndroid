package team1.com.beaconscanner;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class ExhibitManager extends AppCompatActivity {
    private ExhibitFirebase exhibitFirebase;
    private EditText titleEditText;
    private EditText aboutEditText;
    private TextView addressTextView;
    private ImageView imageView;
    private Button addButton;
    private Button editButton;
    private Button removeButton;
    private Exhibit exhibit;

    public static final int PICTURE = 1;
    private boolean isEdit = false;
    private String[] galleryPermissions = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Uri imageDownloadPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibit_manage);

        titleEditText = (EditText) findViewById(R.id.title);
        aboutEditText = (EditText) findViewById(R.id.about);
        imageView = (ImageView) findViewById(R.id.image);
        addressTextView = (TextView) findViewById(R.id.address);
        addButton = (Button) findViewById(R.id.add);
        editButton = (Button) findViewById(R.id.edit);
        removeButton = (Button) findViewById(R.id.remove);

        isEdit = getIntent().getBooleanExtra("edit", false);
        exhibit = getIntent().getExtras().getParcelable("exhibit");
        exhibitFirebase = new ExhibitFirebase(null);

        hideUnusedButtons();
        if (isEdit) fillActivityFields();

        findViewById(R.id.button_beacon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO get beacons by bluetooth scanning
                final CharSequence beacons[] = new CharSequence[] {"30:20:10:00", "50:40:30:20", "70:60:50:40"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ExhibitManager.this);

                builder.setTitle(R.string.exhibit_beacon_title);
                builder.setItems(beacons, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addressTextView.setText(beacons[which]);
                    }
                });
                builder.show();
            }
        });
        findViewById(R.id.button_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (EasyPermissions.hasPermissions(ExhibitManager.this, galleryPermissions)) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");

                    startActivityForResult(intent, PICTURE);
                }
                else {
                    EasyPermissions.requestPermissions(ExhibitManager.this, getString(R.string.exhibit_permission),101, galleryPermissions);
                }
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveExhibit();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateExhibit();
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeExhibit();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE && resultCode == RESULT_OK && data != null) {
            final ProgressDialog progressDialog = new ProgressDialog(ExhibitManager.this);

            progressDialog.setTitle(R.string.exhibit_image_upload);
            progressDialog.show();

            Uri selectedImageDataUri = data.getData();

            Picasso.with(ExhibitManager.this)
                    .load(selectedImageDataUri)
                    .centerCrop()
                    .fit()
                    .into(imageView);

            StorageReference filepath = FirebaseStorage.getInstance().getReference().child("Photos").child(selectedImageDataUri.getLastPathSegment());

            filepath.putFile(selectedImageDataUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    imageDownloadPath = taskSnapshot.getDownloadUrl();
                    Toast.makeText(getBaseContext(), getString(R.string.exhibit_upload_done), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void hideUnusedButtons() {
        if (isEdit) addButton.setVisibility(View.GONE);
        else findViewById(R.id.edit_section).setVisibility(View.GONE);
    }

    private void fillActivityFields() {
        titleEditText.setText(exhibit.getTitle());
        aboutEditText.setText(exhibit.getAbout());
        addressTextView.setText(exhibit.getAddress());
        imageDownloadPath = Uri.parse(exhibit.getImagePath());
        Picasso.with(ExhibitManager.this)
                .load( Uri.parse( exhibit.getImagePath() ) )
                .centerCrop()
                .fit()
                .into(imageView);
    }

    private boolean isValid() {
        if (titleEditText.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), getString(R.string.exhibit_validation_name), Toast.LENGTH_SHORT).show();

            return false;
        }
        else if (aboutEditText.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), getString(R.string.exhibit_validation_about), Toast.LENGTH_SHORT).show();

            return false;
        }
        else if (addressTextView.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), getString(R.string.exhibit_validation_id), Toast.LENGTH_SHORT).show();

            return false;
        }
        else if (imageDownloadPath == null) {
            Toast.makeText(getBaseContext(), getString(R.string.exhibit_validation_image), Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    private void saveExhibit() {
        if (isValid()) {
            exhibit = new Exhibit("", titleEditText.getText().toString(), aboutEditText.getText().toString(), imageDownloadPath.toString(), addressTextView.getText().toString());

            exhibitFirebase.add(exhibit);

            Toast.makeText(getBaseContext(), getString(R.string.exhibit_saved), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateExhibit() {
        if (isValid()) {
            exhibit.setTitle(titleEditText.getText().toString());
            exhibit.setAbout(aboutEditText.getText().toString());
            exhibit.setImagePath(imageDownloadPath.toString());
            exhibit.setAddress(addressTextView.getText().toString());

            exhibitFirebase.edit(exhibit);

            Toast.makeText(getBaseContext(), getString(R.string.exhibit_edited), Toast.LENGTH_SHORT).show();
        }
    }

    private void removeExhibit() {
        exhibitFirebase.remove(exhibit.getId());

        Toast.makeText(getBaseContext(), getString(R.string.exhibit_removed), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
    }
}
