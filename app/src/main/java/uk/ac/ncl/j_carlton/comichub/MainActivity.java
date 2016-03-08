package uk.ac.ncl.j_carlton.comichub;

import android.Manifest;
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
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;

//    private static final String[] LOCATION_PERMS={
//            Manifest.permission.ACCESS_FINE_LOCATION,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//    };
//    private static final int INITIAL_REQUEST=1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.login_user);
        password = (EditText) findViewById(R.id.user_pass);

        //requestPermissions(LOCATION_PERMS, INITIAL_REQUEST);
    }

    public void clearForm(View view) {
        finish();
        startActivity(getIntent());
    }

    /**
     * Move to the register activity, if the user
     * clicks on its respective button
     * @param view  the view which was click
     */
    public void goToRegister(View view) {
        Intent intent = new Intent(this, NearMe.class);
        startActivity(intent);
    }

    /**
     *
     * @param view
     */
    public void login(View view) {
        try {
            String response = new ConnectAndLogin(username.getText().toString(), password.getText().toString()).execute().get();
            String uName = username.getText().toString();
            if (response.equals(uName)) {
                Intent intent = new Intent(this, NearMe.class);
                intent.putExtra("username", uName);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    private class ConnectAndLogin extends AsyncTask<String, Void, String> {
        private final String dbURL = "jdbc:mysql://mysqldbinstance.cchftrgjl5qm.eu-west-1.rds.amazonaws.com:3306/comichub";
        private final String dbUserName = "comicaccess";
        private final String dbPass = "Password123";

        String userName;
        String password;

        public ConnectAndLogin(String u, String p) {
            this.userName = u;
            this.password = p;
        }

        @Override
        protected String doInBackground(String... params) {
            String response = "";

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dbURL, dbUserName, dbPass);
                String fetchAll = "SELECT * FROM user";
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(fetchAll);
                Log.i("Query", "Executing query: " + fetchAll);

                String storedPw = "";
                boolean stored = false;
                while (resultSet.next()) {
                    String storedUserName = resultSet.getString("username");
                    if (storedUserName.equals(userName)) {
                        storedPw = resultSet.getString("password");
                        stored = true;
                        break;
                    }
                }

                if (!stored) {
                    Toast.makeText(getApplicationContext(), "Not registered username: " + userName, Toast.LENGTH_LONG).show();
                    Log.i("Username", "Username isn't registered: " + userName);
                    close(resultSet, statement, connection);
                    return "username not registered";
                }

                String hashPassedPw = hash(password);
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

            return response;
        }

        private void close(ResultSet r, Statement s, Connection c) {
            try {
                r.close();
                s.close();
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
