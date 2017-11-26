package team1.com.beaconscanner;

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
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exhibit);

        titleEditText = (EditText) findViewById(R.id.title);
        aboutEditText = (EditText) findViewById(R.id.about);
        addressTextView = (TextView) findViewById(R.id.address);
        imageView = (ImageView) findViewById(R.id.image);

        id = getIntent().getStringExtra("id");
        exhibitFirebase = new ExhibitFirebase(null);

        // TODO: get data from firebase and fill the edit texts
        // waiting for id from previewExhibit

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

    private void updateInFirebase() {
        if (titleEditText.getText().toString().equals("")) Toast.makeText(getBaseContext(), "Zadajte názov exponátu.", Toast.LENGTH_SHORT).show();
        else if (aboutEditText.getText().toString().equals("")) Toast.makeText(getBaseContext(), "Zadajte popis exponátu.", Toast.LENGTH_SHORT).show();
        else if (addressTextView.getText().toString().equals("")) Toast.makeText(getBaseContext(), "Zadajte adresu beaconu.", Toast.LENGTH_SHORT).show();
        else {
            Exhibit exhibit = new Exhibit(id, titleEditText.getText().toString(), aboutEditText.getText().toString(), "TODO", addressTextView.getText().toString());

            exhibitFirebase.edit(exhibit);

            Toast.makeText(getBaseContext(), "Exponát bol upravený", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFromFirebase() {
        exhibitFirebase.remove(id);

        Toast.makeText(getBaseContext(), "Exponát bol zmazaný", Toast.LENGTH_SHORT).show();

        super.onBackPressed();
    }
}
