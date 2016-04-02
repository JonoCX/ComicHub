package uk.ac.ncl.j_carlton.comichub;

import android.os.AsyncTask;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * A class to fetch all of the comic books stored on the AWS MySQL server.
 *
 * This class has to extend the AsyncTask class as it needs to spawn another
 * thread to perform this task on or the app will choke up.
 *
 * @author Jonathan Carlton 130266400
 */
public class FetchComicBooks extends AsyncTask<String, Void, List<ComicBook>> {

    // data base authentication variables
    private final String dbURL = "jdbc:mysql://mysqldbinstance.cchftrgjl5qm.eu-west-1.rds.amazonaws.com:3306/comichub";
    private final String dbUserName = "comicaccess";
    private final String dbPass = "Password123";

    /**
     * Overridden method from the AsyncTask parent class.
     * @param params    optional - any parameters that want to be passed
     * @return          the full list of comic books stored on the AWS MySQL server.
     */
    @Override
    protected List<ComicBook> doInBackground(String... params) {
        List<ComicBook> list = new ArrayList<>();

        try {
            // fetch the jdbc driver class
            Class.forName("com.mysql.jdbc.Driver");

            // create the connection to the database on the server
            Connection connection = DriverManager.getConnection(dbURL, dbUserName, dbPass);

            // select all
            String query = "SELECT * FROM comics";

            // create the statement and execute it
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Log.i("Query", "Executing query: " + query);

            // iterate over the results
            while (resultSet.next()) {
                int in_library = resultSet.getInt("inLibrary");
                // if not in the library then add a new comic book with that variable set to false to the list
                if (in_library == 0) {
                    list.add(new ComicBook(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("volume"),
                            resultSet.getInt("issue"),
                            resultSet.getString("publisher"),
                            resultSet.getString("publish-date"),
                            resultSet.getString("image-ref"),
                            false
                    ));
                }
                // if in the library then add a new comic book with that variable set to true to the list
                else {
                    list.add(new ComicBook(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("volume"),
                            resultSet.getInt("issue"),
                            resultSet.getString("publisher"),
                            resultSet.getString("publish-date"),
                            resultSet.getString("image-ref"),
                            true
                    ));
                }
            }
            // close the connection
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
