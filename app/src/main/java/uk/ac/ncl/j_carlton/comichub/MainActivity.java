package uk.ac.ncl.j_carlton.comichub;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
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
import java.util.concurrent.ExecutionException;

/**
 * The class for the main activity of the application - the log in screen
 *
 * @author Jonathan Carlton - 130266400
 */
public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.login_user);
        password = (EditText) findViewById(R.id.user_pass);

        if (getIntent().hasExtra("username"))
            username.setText(getIntent().getStringExtra("username"));

        // if enter is pressed on the keyboard then attempt to log in
        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    login(v);
                }
                return false;
            }
        });
    }

    /**
     * Clears the login form
     * @param view      the view
     */
    public void clearForm(View view) {
        // restart the current activity
        finish();
        startActivity(getIntent());
    }

    /**
     * Move to the register activity, if the user
     * clicks on its respective button
     * @param view  the view which was click
     */
    public void goToRegister(View view) {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    /**
     * The login method.
     * @param view       the view
     */
    public void login(View view) {
        // if either field is empty when trying to log in, notify the user.
        if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "A field is empty.", Toast.LENGTH_LONG).show();
            startActivity(getIntent());
        }
        try {
            // attempt to log in with the credentials the user has given
            String response = new ConnectAndLogin(username.getText().toString(), password.getText().toString()).execute().get();
            String uName = username.getText().toString();
            // the user names are equal then allow the log in
            if (response.equals(uName)) {
                // start the landing screen activity
                Intent intent = new Intent(this, LandingScreen.class);
                intent.putExtra("username", uName);
                startActivity(intent);
            }

            if (response.equals("pw not equal")) {
                Toast.makeText(getApplicationContext(), "Password is incorrect", Toast.LENGTH_LONG).show();
                startActivity(getIntent());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    /**
     * AsyncTask to spawn a new thread to connect to the user database on AWS
     * and check if the authentication credentials are correct
     */
    private class ConnectAndLogin extends AsyncTask<String, Void, String> {
        // server and database connection parameters
        private final String dbURL = "jdbc:mysql://mysqldbinstance.cchftrgjl5qm.eu-west-1.rds.amazonaws.com:3306/comichub";
        private final String dbUserName = "comicaccess";
        private final String dbPass = "Password123";

        String userName;
        String password;

        /**
         * Constructor
         * @param u     user name
         * @param p     password
         */
        public ConnectAndLogin(String u, String p) {
            this.userName = u;
            this.password = p;
        }

        /**
         * method to perform in the background
         * @param params    optional parameters passed to the method
         * @return          username - if successful
         */
        @Override
        protected String doInBackground(String... params) {
            try {
                // fetch the jdbc driver class
                Class.forName("com.mysql.jdbc.Driver");

                // make the connection to the server
                Connection connection = DriverManager.getConnection(dbURL, dbUserName, dbPass);

                // select all users
                String fetchAll = "SELECT * FROM user";

                // create and execute the statement
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(fetchAll);
                Log.i("Query", "Executing query: " + fetchAll);

                String storedPw = "";
                boolean stored = false;

                // iterate over the result set tot he find the stored user
                while (resultSet.next()) {
                    String storedUserName = resultSet.getString("username");
                    if (storedUserName.equals(userName)) {
                        storedPw = resultSet.getString("password");
                        stored = true;
                        break;
                    }
                }

                // if the user isn't stored on the server then notify the user
                if (!stored) {
                    Toast.makeText(getApplicationContext(), "Not registered username: " + userName, Toast.LENGTH_LONG).show();
                    Log.i("Username", "Username isn't registered: " + userName);
                    close(resultSet, statement, connection);
                    return "username not registered";
                }

                // hash the passed password
                String hashPassedPw = hash(password);

                // if they equal - successful
                if (storedPw.equals(hashPassedPw)) {
                    Log.i("Comparing hashes", "Hashes match");
                    close(resultSet, statement, connection);
                    return userName;
                } else {
                    Log.i("Comparing hashes", "Hashes do not match");
                    close(resultSet, statement, connection);
                    return "pw not equal";
                }



            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

            return "";
        }

        /**
         * A cleaner way to close the MySQL connections mate
         * @param r     resultset
         * @param s     statement
         * @param c     connection
         */
        private void close(ResultSet r, Statement s, Connection c) {
            try {
                r.close();
                s.close();
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /**
         * Method to hash the passwords of users.
         * @param input     to be hashed
         * @return          the hash
         */
        private String hash(String input) {
            StringBuilder builder = new StringBuilder();
            try {
                // get the input bytes
                byte[] data = input.getBytes();

                // use the sha-256 digest
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                digest.update(data);

                // perform hash transformation
                for (byte b: data) builder.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return builder.toString();
        }
    }
}
