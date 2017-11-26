package team1.com.beaconscanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AddExhibit extends AppCompatActivity {
    private EditText titleEditText;
    private ImageView imageView;
    private EditText aboutEditText;
    private TextView idTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exhibit);

        titleEditText = (EditText) findViewById(R.id.et_title);
        imageView = (ImageView) findViewById(R.id.et_image);
        aboutEditText = (EditText) findViewById(R.id.et_about);
        idTextView = (TextView) findViewById(R.id.et_id);

        Bundle args = getIntent().getExtras();

        boolean exhibit_edit = args.getBoolean("exhibit_edit",false);

        // ak ideme z aktivity previewExhibit, tak exhibit_edit = true
        // zatial posielam cez intent len id...ak by sme to tu opat chceli nacitavat z DB.
        // ked tak to mozme spravit ze posleme cez ten intent cely objekt.

        if (exhibit_edit) {   // takze treba nacitat data
            int exhibit_id = args.getInt("exhibit_id",-1);
            Log.i("AAA","Comming to edit exhibit from PreviewActivity");

            if (exhibit_id == -1) {
                // something went wrong
            }
            else {
                // load data of exhibit from DB
 
                titleEditText.setText("nejaky nazov z DB");
                aboutEditText.setText("nejaky popis z DB");
                idTextView.setText("id: "+ exhibit_id);
            }
        }
        else { // ideme pridavat novy item, takze netreba nic nacitavat do poli.

            // nic nerob
        }
    }
}
