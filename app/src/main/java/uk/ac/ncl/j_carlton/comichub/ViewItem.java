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
        comicBook = getIntent().getParcelableExtra("ComicBook");
        String activityIntent = getIntent().getStringExtra("intent");
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

        if (activityIntent.equals("add")) {
            addToLibrary();
        } else {
            removeFromLibrary();
        }

    }

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

    private void removeFromLibrary() {
        Button button = (Button) findViewById(R.id.add_remove_btn);
        button.setText("Remove from Library");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOnClick(false);
            }
        });
    }

    private void btnOnClick(boolean result) {
        Boolean[] res = new Boolean[1];
        res[0] = result;
        if (result) {
            try {
                boolean success = new updateComicBook().execute(res).get();
                Toast.makeText(getBaseContext(), "Added to Library", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, AddToLibrary.class);
                intent.putExtra("search", searchQuery);
                startActivity(intent);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                boolean success = new updateComicBook().execute(res).get();
                Toast.makeText(getBaseContext(), "Removed from Library", Toast.LENGTH_LONG).show();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }


    private class updateComicBook extends AsyncTask<Boolean, Void, Boolean> {
        private final String dbURL = "jdbc:mysql://mysqldbinstance.cchftrgjl5qm.eu-west-1.rds.amazonaws.com:3306/comichub";
        private final String dbUserName = "comicaccess";
        private final String dbPass = "Password123";

        @Override
        protected Boolean doInBackground(Boolean... params) {
            boolean success = false;
            boolean param = params[0];
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(dbURL, dbUserName, dbPass);
                String query;
                if (param) {
                    query = "UPDATE `comics` SET `inLibrary`=1 WHERE `id`=" + comicBook.getId();
                    comicBook.setInLibrary(true);
                }
                else {
                    query = "UPDATE `comics` SET `inLibrary`=0 WHERE `id`=" + comicBook.getId();
                    comicBook.setInLibrary(false);
                }
                Statement statement = connection.createStatement();
                statement.executeUpdate(query);
                connection.close();
                success = true;
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

            return success;
        }
    }
}
