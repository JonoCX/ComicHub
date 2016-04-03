package uk.ac.ncl.j_carlton.comichub;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ExecutionException;

public class ViewItem extends AppCompatActivity {

    ComicBook comicBook;
    String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);

        // assign the passed comic book object to the one within the scope of the class
        comicBook = getIntent().getParcelableExtra("ComicBook");

        // get the intent of the parent activity
        String activityIntent = getIntent().getStringExtra("intent");

        // if the user has performed a search and came to the this comic book then
        // get the search query
        if (getIntent().hasExtra("search"))
            searchQuery = getIntent().getStringExtra("search");

        // Set image
        ImageView coverArt = (ImageView) findViewById(R.id.coverArt);
        String imageRef = comicBook.getImageRef();
        String split = imageRef.substring(0, imageRef.length() - 4);
        int resourceId = getResources().getIdentifier(split, "drawable", getPackageName());
        coverArt.setImageResource(resourceId);

        // Set text information.
        TextView name = (TextView) findViewById(R.id.name);
        TextView volume = (TextView) findViewById(R.id.volume);
        TextView issue = (TextView) findViewById(R.id.issue);
        TextView publisher = (TextView) findViewById(R.id.publisher);
        TextView published = (TextView) findViewById(R.id.published_date);
        String nameCb = comicBook.getName();
        if (nameCb != null) {
            name.setText("Name: " + nameCb);
            volume.setText("Volume: " + comicBook.getVolume());
            issue.setText("Issue #: " + comicBook.getIssue());
            publisher.setText("Publisher: " + comicBook.getPublisher());
            published.setText("Published: " + comicBook.getPublishedDate());
        } else {
            name.setText("Volume: " + comicBook.getVolume());
            volume.setText("Issue #: " + comicBook.getIssue());
            issue.setText("Publisher: " + comicBook.getPublisher());
            publisher.setText("Published: " + comicBook.getPublishedDate());
            published.setText("");
        }

        // if the intent of the calling activity is to add to the library
        if (activityIntent.equals("add")) {
            addToLibrary();
        } else {
            removeFromLibrary();
        }

    }

    /**
     *  Transforms the button on the view to an "add to library"
     *  button.
     */
    private void addToLibrary() {
        Button button = (Button) findViewById(R.id.add_remove_btn);
        button.setText("Add to Library");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // true for adding to the library
                btnOnClick(true);
            }
        });
    }

    /**
     *  Transforms the button on the view to an "remove from library"
     *  button
     */
    private void removeFromLibrary() {
        Button button = (Button) findViewById(R.id.add_remove_btn);
        button.setText("Remove from Library");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // false to remove from the library
                btnOnClick(false);
            }
        });
    }

    /**
     * Performs the action of either removing or adding to the library
     * @param result    true = add & false = remove
     */
    private void btnOnClick(boolean result) {
        Boolean[] res = new Boolean[1];
        res[0] = result;
        if (result) {
            try {
                // perform the update
                boolean success = new UpdateComicBook().execute(res).get();

                // signal to the user that it was successful
                if (success) {
                    Toast.makeText(getBaseContext(), "Added to Library", Toast.LENGTH_LONG).show();

                    // reload the add to library activity and pass the query to perform the search again
                    Intent intent = new Intent(this, AddToLibrary.class);
                    intent.putExtra("search", searchQuery);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Unsuccessful", Toast.LENGTH_LONG).show();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                boolean success = new UpdateComicBook().execute(res).get();

                // signal to the user that it was successful
                if (success) {
                    Toast.makeText(getBaseContext(), "Removed from Library", Toast.LENGTH_LONG).show();

                    // reload the add to library activity and pass the query to perform the search again
                    Intent intent = new Intent(this, ViewLibrary.class);
                    intent.putExtra("search", searchQuery);
                    startActivity(intent);
                } else {
                    Toast.makeText(getBaseContext(), "Unsuccessful", Toast.LENGTH_LONG).show();
                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * AsyncTask to spawn a new thread to connect to the comic book database
     * on AWS server and update a comic book to be either in or out of the
     * library
     */
    private class UpdateComicBook extends AsyncTask<Boolean, Void, Boolean> {

        // server and database connection parameters
        private final String dbURL = "jdbc:mysql://mysqldbinstance.cchftrgjl5qm.eu-west-1.rds.amazonaws.com:3306/comichub";
        private final String dbUserName = "comicaccess";
        private final String dbPass = "Password123";

        /**
         * method to perform the task in the background
         * @param params    optional string parameters
         * @return          the response from carrying out the task
         */
        @Override
        protected Boolean doInBackground(Boolean... params) {
            boolean success = false;

            // fetch the passed result, true if they want to add and false if they don't
            boolean param = params[0];
            try {
                // fetch the jdbc driver class
                Class.forName("com.mysql.jdbc.Driver");

                // make the connection to the server and database
                Connection connection = DriverManager.getConnection(dbURL, dbUserName, dbPass);
                String query;

                // if they want to add the comic book to the library
                if (param) {
                    // update the row and change the object
                    query = "UPDATE `comics` SET `inLibrary`=1 WHERE `id`=" + comicBook.getId();
                    comicBook.setInLibrary(true);
                }
                else {
                    query = "UPDATE `comics` SET `inLibrary`=0 WHERE `id`=" + comicBook.getId();
                    comicBook.setInLibrary(false);
                }

                // create and execute the statement
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);

                // close connection
                connection.close();

                // successful.
                success = true;
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

            return success;
        }
    }
}
