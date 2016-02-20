package uk.ac.ncl.j_carlton.comichub;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends AppCompatActivity {

    EditText username;
    EditText password_one;
    EditText password_two;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Find all of the activity elements by their IDs
        username = (EditText) findViewById(R.id.username);
        password_one = (EditText) findViewById(R.id.first_password);
        password_two = (EditText) findViewById(R.id.second_password);
        email = (EditText) findViewById(R.id.email);

    }

    /**
     * Clear the form
     * @param view  the view which was clicked
     */
    public void clearForm(View view) {
        // finish the current activity
        finish();

        // restart itself
        startActivity(getIntent());
    }

    /**
     *
     * @param view
     */
    public void submitForm(View view) {
        // check for password equality
        if (!(password_one.getText().toString().equals(password_two.getText().toString()))) {
            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_LONG).show();
            clearForm(view);
        } else {

        }
    }
}
