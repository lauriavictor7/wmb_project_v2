package br.lauriavictor.wmb.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import br.lauriavictor.wmb.R;

public class WmbMainActivity extends AppCompatActivity {

    Button mButtonRegister, mButtonPlaces, mButtonBeer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wmb_main);

        mButtonRegister = (Button) findViewById(R.id.buttonRegister);
        mButtonPlaces = (Button) findViewById(R.id.buttonFindPlaces);
        mButtonBeer = (Button) findViewById(R.id.buttonBeer);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WmbAddBeerActivity.class));
            }
        });

        mButtonPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchNearby();
            }
        });

        mButtonBeer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WmbListActivity.class));
            }
        });
    }

    private void searchNearby() {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=bares");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }
}
