package uk.ac.ncl.j_carlton.comichub;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.analytics.ecommerce.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddToLibrary extends AppCompatActivity {

    private EditText search;
    private ListView listView;
    private Button search_btn;
    List<ComicBook> comicBookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_library);

        comicBookList = new ArrayList<>();
        try {
            comicBookList = new FetchComicBooks().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        listView = (ListView) findViewById(R.id.list_view);
        search = (EditText) findViewById(R.id.search_text);

        search_btn = (Button) findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOnClick();
            }
        });
    }

    private void btnOnClick() {
        String query = search.getText().toString();
        ArrayList<ComicBook> results = new ArrayList<>();
        ComicBookAdapter adapter = new ComicBookAdapter(this, results);
        listView.setAdapter(adapter);
        results = doSearch(comicBookList, query);
        adapter.addAll(results);
    }

    private ArrayList<ComicBook> doSearch(List<ComicBook> list, String query) {
        ArrayList<ComicBook> data = new ArrayList<>();

        for (ComicBook cb : list) {
            System.out.println(cb);
            if (cb.getName() == null && cb.getVolume().contains(query))
                data.add(cb);
            if (cb.getName() != null) {
                if (cb.getName().contains(query) || cb.getVolume().contains(query))
                    data.add(cb);
            }
        }

        return data;
    }


    /**
     *
     */
    private class FetchComicBooks extends AsyncTask<String, Void, List<ComicBook>> {
        // data base authentication variables
        private final String dbURL = "jdbc:mysql://mysqldbinstance.cchftrgjl5qm.eu-west-1.rds.amazonaws.com:3306/comichub";
        private final String dbUserName = "comicaccess";
        private final String dbPass = "Password123";

        /**
         *
         * @param params
         * @return
         */
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

                for (ComicBook b: list)
                    System.out.println(b.toString());
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

            return list;
        }
    }

}
