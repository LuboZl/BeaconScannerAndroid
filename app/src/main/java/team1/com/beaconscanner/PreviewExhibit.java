package team1.com.beaconscanner;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PreviewExhibit extends AppCompatActivity {
    private TextView titleTextView;
    private ImageView imageView;
    private TextView aboutTextView;
    private TextView idTextView;
    private FloatingActionButton fab_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_exhibit);

        titleTextView = (TextView) findViewById(R.id.title);
        imageView = (ImageView) findViewById(R.id.image);
        aboutTextView = (TextView) findViewById(R.id.about);
        idTextView = (TextView) findViewById(R.id.id);

        LoadInfo();

        fab_edit = (FloatingActionButton) findViewById(R.id.fab);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("AAA","fab edit clicked!");
                Intent intent = new Intent(getBaseContext(), AddExhibit.class);
                intent.putExtra("exhibit_edit",true);
                intent.putExtra("exhibit_id", Integer.parseInt( idTextView.getText()+"" ));
                startActivity(intent);
            }
        });
    }

    public void LoadInfo(){
        titleTextView.setText("NAZOV DIELA");
//        iv_coverImage
        aboutTextView.setText("NIECO O TOMTO DIELE TU BUDE NAPISANE NEVIEM TERAZ CO LOREM LOREM LOREM");
        idTextView.setText("433");
    }
}
