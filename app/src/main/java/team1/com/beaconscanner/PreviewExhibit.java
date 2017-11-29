package team1.com.beaconscanner;


import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.security.auth.login.LoginException;

import team1.com.beaconscanner.exhibit.Exhibit;

public class PreviewExhibit extends AppCompatActivity {
    private TextView titleTextView;
    private ImageView imageView;
    private TextView aboutTextView;
    private Exhibit exhibit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_exhibit);

        titleTextView = (TextView) findViewById(R.id.title);
        imageView = (ImageView) findViewById(R.id.image);
        aboutTextView = (TextView) findViewById(R.id.about);


        exhibit = getIntent().getExtras().getParcelable("exhibit");

        fillActivityFields();

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openEditIntent();
            }
        });
    }

    private void fillActivityFields() {
        titleTextView.setText(exhibit.getTitle());
        Picasso.with(PreviewExhibit.this)
                .load( Uri.parse( exhibit.getImagePath() ) )
//                .placeholder(R.drawable.ic_action_name)
//                .error(R.drawable.user_placeholder_error)
//                .resize(800,800)
                .centerCrop()
                .fit()
                .into(imageView)
        ;
        aboutTextView.setText(exhibit.getAbout());
    }

    private void openEditIntent() {
        Intent intent = new Intent(getBaseContext(), EditExhibit.class);

        intent.putExtra("exhibit", exhibit);

        startActivity(intent);
    }
}
