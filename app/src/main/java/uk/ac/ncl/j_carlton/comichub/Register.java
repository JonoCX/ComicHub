package uk.ac.ncl.j_carlton.comichub;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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
        }

        String userName = username.getText().toString();
        if (userName.length() > 16) {
            Toast.makeText(getApplicationContext(), "User name is to long (less than 16 characters", Toast.LENGTH_LONG).show();
            clearForm(view);
        }

        String uEmail = email.getText().toString();
        String uPass = password_one.getText().toString();

        new ConnectAndRegister(userName, uPass, uEmail).execute();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    private class ConnectAndRegister extends AsyncTask<String, Void, String> {
        private final String dbURL = "jdbc:mysql://mysqldbinstance.cchftrgjl5qm.eu-west-1.rds.amazonaws.com:3306/comichub";
        private final String dbUserName = "comicaccess";
        private final String dbPass = "Password123";

        String userName;
        String password;
        String email;

        public ConnectAndRegister(String u, String p, String e) {
            this.userName = u;
            this.password = hash(p);
            this.email = e;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dbURL, dbUserName, dbPass);
                String query = "SELECT * FROM user";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                Log.i("Query", "Executing query: " + query);
                while (resultSet.next()) {
                    String uName = resultSet.getString("username");
                    if (uName.equals(userName)) {
                        Toast.makeText(getApplicationContext(), "Username already registered", Toast.LENGTH_LONG).show();
                        Log.i("Username", "Username already registered: " + uName + " = " + userName);
                        return response;
                    }
                }


                String insertQuery = "INSERT INTO user VALUES ('" + userName + "', '" + email + "', '" + password + "', NOW())";
                Log.i("Query", "Inserting user data into DB");
                statement = connection.createStatement();
                int i = statement.executeUpdate(insertQuery);
                Log.i("Query", "Success, code: " + i);

                resultSet.close();
                statement.close();
                connection.close();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
            return response;
        }

        private String hash(String input) {
            StringBuilder builder = new StringBuilder();
            try {
                byte[] data = input.getBytes();
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                digest.update(data);
                for (byte b: data) builder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }
    }
}
