package team1.com.beaconscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import team1.com.beaconscanner.exhibit.Exhibit;

public class AddExhibit extends AppCompatActivity {
    private ExhibitFirebase exhibitFirebase;
    private EditText titleEditText;
    private EditText aboutEditText;
    private TextView addressTextView;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exhibit);

        titleEditText = (EditText) findViewById(R.id.title);
        aboutEditText = (EditText) findViewById(R.id.about);
        addressTextView = (TextView) findViewById(R.id.address);
        imageView = (ImageView) findViewById(R.id.image);

        exhibitFirebase = new ExhibitFirebase(null);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToFirebase();
            }
        });
    }

    private void saveToFirebase() {
        if (titleEditText.getText().toString().equals("")) Toast.makeText(getBaseContext(), "Zadajte názov exponátu.", Toast.LENGTH_SHORT).show();
        else if (aboutEditText.getText().toString().equals("")) Toast.makeText(getBaseContext(), "Zadajte popis exponátu.", Toast.LENGTH_SHORT).show();
        else if (addressTextView.getText().toString().equals("")) Toast.makeText(getBaseContext(), "Zadajte adresu beaconu.", Toast.LENGTH_SHORT).show();
        else {
            Exhibit exhibit = new Exhibit("", titleEditText.getText().toString(), aboutEditText.getText().toString(), "TODO", addressTextView.getText().toString());

            exhibitFirebase.add(exhibit);

            Toast.makeText(getBaseContext(), "Exponát bol uložený", Toast.LENGTH_SHORT).show();
        }
    }
}
