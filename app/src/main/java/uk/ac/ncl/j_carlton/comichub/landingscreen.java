package uk.ac.ncl.j_carlton.comichub;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LandingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingscreen);
    }

    public void openGoogleMapsSearch(View v) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=comic book store");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    public void logout(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void addToLibrary(View v) {
        Intent intent = new Intent(this, AddToLibrary.class);
        startActivity(intent);
    }

    public void viewLibrary(View v) {
        Intent intent = new Intent(this, ViewLibrary.class);
        startActivity(intent);
    }
}
