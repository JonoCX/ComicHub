package uk.ac.ncl.j_carlton.comichub;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Jonathan on 05-Mar-16.
 */
public class DBConnection extends AsyncTask<Void, Void, Boolean> {

    Connection connection = null;

    final String dbURL = "jdbc:mysql://mysqldbinstance.cchftrgjl5qm.eu-west-1.rds.amazonaws.com:3306/comichub";
    final String userName = "comicaccess";
    final String pass = "Password123";
    final String driver = "com.mysql.jdbc.Driver";

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Class.forName(driver);
            Log.i("Connection", "Connecting to DB");
            connection = DriverManager.getConnection(dbURL, userName, pass);
            if (connection != null)
                Log.i("Connecting", "Connection made!");
            else
                Log.i("Connecting", "Connection failed");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private Connection getConnection() { return connection; }

}
