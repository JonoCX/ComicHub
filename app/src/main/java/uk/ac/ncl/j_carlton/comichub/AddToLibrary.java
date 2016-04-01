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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private String searchQuery;

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

        //final ArrayList<ComicBook> results = doSearch(comicBookList, searchQuery);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listViewOnClick(position)) {}
                else {
                    Toast.makeText(getBaseContext(), "Search field empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean listViewOnClick(int position) {
        if (searchQuery != null) {
            ArrayList<ComicBook> results = doSearch(comicBookList, searchQuery);
            ComicBook item = results.get(position);
            //Toast.makeText(getBaseContext(), item.toString(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, ViewItem.class);
            intent.putExtra("ComicBook", item);

            // User wants to add, tell the view item that is the case.
            intent.putExtra("intent", "add");
            startActivity(intent);
            return true;
        }
        return false;
    }

    private void btnOnClick() {
        String query = search.getText().toString();
        ArrayList<ComicBook> results = new ArrayList<>();
        ComicBookAdapter adapter = new ComicBookAdapter(this, results);
        listView.setAdapter(adapter);
        results = doSearch(comicBookList, query);
        adapter.addAll(results);
        searchQuery = query;
    }

    private ArrayList<ComicBook> doSearch(List<ComicBook> list, String query) {
        ArrayList<ComicBook> data = new ArrayList<>();

        for (ComicBook cb : list) {
            System.out.println(cb);
            if (cb.getName() == null && cb.getVolume().toLowerCase().contains(query.toLowerCase()))
                data.add(cb);
            if (cb.getName() != null) {
                if (cb.getName().toLowerCase().contains(query.toLowerCase()) || cb.getVolume().toLowerCase().contains(query.toLowerCase()))
                    data.add(cb);
            }
        }

        return data;
    }
}
