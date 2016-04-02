package uk.ac.ncl.j_carlton.comichub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Activity class for the add to library screen.
 *
 * @author Jonathan Carlton - 130266400
 */
public class AddToLibrary extends AppCompatActivity {

    /*
        Activity components
     */
    private EditText search;
    private ListView listView;
    private Button search_btn;
    List<ComicBook> comicBookList;
    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_library);

        // fetch all of the comic books stored in the AWS database
        comicBookList = new ArrayList<>();
        try {
            comicBookList = new FetchComicBooks().execute().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


        listView = (ListView) findViewById(R.id.list_view);
        search = (EditText) findViewById(R.id.search_text);

        // if enter is pressed on the key board, call the btnOnClick method
        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_ENTER) {
                    btnOnClick();
                }
                return false;
            }
        });

        Intent intent = getIntent();

        // has the search already been performed?
        if (intent.hasExtra("search")) {
            // if so, reload the search query.
            search.setText(intent.getStringExtra("search"));
            btnOnClick();
        }

        search_btn = (Button) findViewById(R.id.search_btn);
        // when the search button is pressed, load the btnOnClick method
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOnClick();
            }
        });

        // make each list item clickable
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // if the method call is successful just carry on.
                if (listViewOnClick(position)) {
                }
                // if not, output to screen that the search field is empty.
                else {
                    Toast.makeText(getBaseContext(), "Search field empty", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * Called when a item in the list view is clicked on by the user
     * @param position      the position of the item that the user has clicked on
     * @return              true if there is a search query and false if not.
     */
    private boolean listViewOnClick(int position) {
        if (searchQuery != null) {
            // perform the search for Comic Books matching the search query
            ArrayList<ComicBook> results = doSearch(comicBookList, searchQuery);

            // fetch the comic book at the position pass (the one that has been clicked)
            ComicBook item = results.get(position);

            // put the comic book into the intent
            Intent intent = new Intent(this, ViewItem.class);
            intent.putExtra("ComicBook", item);

            // User wants to add, tell the view item that is the case.
            intent.putExtra("intent", "add");

            // add the search query into the intent
            intent.putExtra("search", searchQuery);

            // start the view item activity.
            startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * Called when the search button is clicked by the user
     */
    private void btnOnClick() {
        // fetch the query by the user
        String query = search.getText().toString();

        // create a custom adapter and set it to the list view on the activity
        ArrayList<ComicBook> results = new ArrayList<>();
        ComicBookAdapter adapter = new ComicBookAdapter(this, results);
        listView.setAdapter(adapter);

        // perform the search for the query by the user
        results = doSearch(comicBookList, query);

        // add the results to the adapter, this will display them on the activity.
        adapter.addAll(results);
        searchQuery = query;
    }

    /**
     * Called whenever a search is performed for a specific comic book or a range
     * of comic books
     * @param list      the list of comic books
     * @param query     the query performed by the user
     * @return          the array list of results which match the query
     */
    private ArrayList<ComicBook> doSearch(List<ComicBook> list, String query) {
        ArrayList<ComicBook> data = new ArrayList<>();

        // iterate of the passed list
        for (ComicBook cb : list) {
            // if the current comic book is not in the library
            if (!cb.isInLibrary()) {
                /*
                    if its name is null (some comic books don't have names, just a single volume name)
                    and the current comic book contains the passed query within its volume name then
                    add it.
                 */
                if (cb.getName() == null && cb.getVolume().toLowerCase().contains(query.toLowerCase()))
                    data.add(cb);

                // if the name isn't null
                if (cb.getName() != null) {
                    // if the comic books name or volume contains the search query then add
                    if (cb.getName().toLowerCase().contains(query.toLowerCase()) || cb.getVolume().toLowerCase().contains(query.toLowerCase()))
                        data.add(cb);
                }
            }
        }

        return data;
    }
}
