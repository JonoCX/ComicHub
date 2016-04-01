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
 * Created by Jonathan on 01-Apr-16.
 */
public class FetchComicBooks extends AsyncTask<String, Void, List<ComicBook>> {

    // data base authentication variables
    private final String dbURL = "jdbc:mysql://mysqldbinstance.cchftrgjl5qm.eu-west-1.rds.amazonaws.com:3306/comichub";
    private final String dbUserName = "comicaccess";
    private final String dbPass = "Password123";

    @Override
    protected List<ComicBook> doInBackground(String... params) {
        List<ComicBook> list = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(dbURL, dbUserName, dbPass);
            String query = "SELECT * FROM comics";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Log.i("Query", "Executing query: " + query);

            while (resultSet.next()) {
                int in_library = resultSet.getInt("inLibrary");
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
                } else {
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
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
