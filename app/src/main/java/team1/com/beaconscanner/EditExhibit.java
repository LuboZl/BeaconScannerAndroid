package team1.com.beaconscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import team1.com.beaconscanner.exhibit.Exhibit;

public class EditExhibit extends AppCompatActivity {
    private ExhibitFirebase exhibitFirebase;
    private EditText titleEditText;
    private EditText aboutEditText;
    private TextView addressTextView;
    private ImageView imageView;
    private Exhibit exhibit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exhibit);

        titleEditText = (EditText) findViewById(R.id.title);
        aboutEditText = (EditText) findViewById(R.id.about);
        addressTextView = (TextView) findViewById(R.id.address);
        imageView = (ImageView) findViewById(R.id.image);

        exhibit = getIntent().getExtras().getParcelable("exhibit");
        exhibitFirebase = new ExhibitFirebase(null);

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
    }

    private void fillActivityFields() {
        titleEditText.setText(exhibit.getTitle());
        // TODO imageView.set??
        aboutEditText.setText(exhibit.getAbout());
        addressTextView.setText(exhibit.getAddress());
    }

    private void updateInFirebase() {
        if (titleEditText.getText().toString().equals("")) Toast.makeText(getBaseContext(), "Zadajte názov exponátu.", Toast.LENGTH_SHORT).show();
        else if (aboutEditText.getText().toString().equals("")) Toast.makeText(getBaseContext(), "Zadajte popis exponátu.", Toast.LENGTH_SHORT).show();
        else if (addressTextView.getText().toString().equals("")) Toast.makeText(getBaseContext(), "Zadajte adresu beaconu.", Toast.LENGTH_SHORT).show();
        else {
            exhibit.setTitle(titleEditText.getText().toString());
            exhibit.setAbout(aboutEditText.getText().toString());
            exhibit.setImagePath("TODO");
            exhibit.setAddress(addressTextView.getText().toString());

            exhibitFirebase.edit(exhibit);

            Toast.makeText(getBaseContext(), "Exponát bol upravený", Toast.LENGTH_SHORT).show();
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
