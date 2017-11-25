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
    private TextView tv_title;
    private ImageView iv_coverImage;
    private TextView tv_about;
    private TextView tv_id;
    private FloatingActionButton fab_edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_exhibit);

        tv_title = (TextView) findViewById(R.id.tv_title);
        iv_coverImage = (ImageView) findViewById(R.id.iv_coverImage);
        tv_about = (TextView) findViewById(R.id.tv_about);
        tv_id = (TextView) findViewById(R.id.tv_id);

        LoadInfo();

        fab_edit = (FloatingActionButton) findViewById(R.id.fab);
        fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("AAA","fab edit clicked!");
                Intent intent = new Intent(getBaseContext(), AddExhibit.class);
                intent.putExtra("exhibit_edit",true);
                intent.putExtra("exhibit_id", Integer.parseInt( tv_id.getText()+"" ));
                startActivity(intent);
            }
        });
    }

    public void LoadInfo(){
        tv_title.setText("NAZOV DIELA");
//        iv_coverImage
        tv_about.setText("NIECO O TOMTO DIELE TU BUDE NAPISANE NEVIEM TERAZ CO LOREM LOREM LOREM");
        tv_id.setText("433");
    }
}
