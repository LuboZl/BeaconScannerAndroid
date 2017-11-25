package team1.com.beaconscanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class AddExhibit extends AppCompatActivity {

    private EditText et_title;
//    private Image coverImage;
    private EditText et_about;
    private TextView et_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exhibit);

        et_title = (EditText) findViewById(R.id.et_title);
//        coverImage = () findViewById(R.id.);
        et_about = (EditText) findViewById(R.id.et_about);
        et_id    = (TextView) findViewById(R.id.et_id);





        Bundle args = getIntent().getExtras();

        boolean exhibit_edit = args.getBoolean("exhibit_edit",false);

        // ak ideme z aktivity previewExhibit, tak exhibit_edit = true
        // zatial posielam cez intent len id...ak by sme to tu opat chceli nacitavat z DB.
        // ked tak to mozme spravit ze posleme cez ten intent cely objekt.

        if(exhibit_edit){   // takze treba nacitat data

            int exhibit_id = args.getInt("exhibit_id",-1);
            Log.i("AAA","Comming to edit exhibit from PreviewActivity");

            if(exhibit_id == -1){
                // something went wrong
            }else {
                // load data of exhibit from DB

                et_title.setText("nejaky nazov z DB");
                et_about.setText("nejaky popis z DB");
                et_id.setText("id: "+ exhibit_id);
            }
        }else { // ideme pridavat novy item, takze netreba nic nacitavat do poli.

            // nic nerob
        }
    }
}
