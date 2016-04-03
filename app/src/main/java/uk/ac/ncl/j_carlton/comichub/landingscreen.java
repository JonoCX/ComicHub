package uk.ac.ncl.j_carlton.comichub;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * The class associated with the landing screen activity.
 *
 * @author Jonathan Carlton - 130266400
 */
public class LandingScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landingscreen);
    }

    /**
     * If the 'find stores near me' button is clicked, this method
     * is called. It loads the google maps app installed on the
     * android operating system and performs a search.
     * @param v     the view
     */
    public void openGoogleMapsSearch(View v) {
        // the search query - comic books stores in the area of the phone
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=comic book store");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

        // set package to that of the google maps app
        mapIntent.setPackage("com.google.android.apps.maps");

        // then start it
        startActivity(mapIntent);
    }

    /**
     * If the 'logout' button is pressed, this method is called.
     * @param v     the view
     */
    public void logout(View v) {
        // re-direct back to the starting activity
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * If the 'add to library' button is click, this method
     * is called.
     * @param v     the view
     */
    public void addToLibrary(View v) {
        // direct to the add to library activity
        Intent intent = new Intent(this, AddToLibrary.class);
        startActivity(intent);
    }

    /**
     * If the 'view library' button is click, this method
     * is called.
     * @param v     the view
     */
    public void viewLibrary(View v) {
        // direct to the view library activity
        Intent intent = new Intent(this, ViewLibrary.class);
        startActivity(intent);
    }
}
