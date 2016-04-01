package uk.ac.ncl.j_carlton.comichub;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Inspiration taken from: http://androidopentutorials.com/android-listview-with-alphabetical-side-index/
 */
public class ViewLibrary extends AppCompatActivity implements View.OnClickListener {

    Map<String, Integer> sideIndex;
    ListView library;
    ArrayList<ComicBook> arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_library);

        library = (ListView) findViewById(R.id.comic_list);
        arr = new ArrayList<>();
        ComicBookAdapter adapter = new ComicBookAdapter(this, arr);
        library.setAdapter(adapter);
        arr = getLibrary();
        adapter.addAll(arr);

        getIndexList(arr);
        displayIndex();

        library.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listViewOnClick(position);
            }
        });
    }

    private boolean listViewOnClick(int position) {
        ComicBook item = arr.get(position);
        Intent intent = new Intent(this, ViewItem.class);
        intent.putExtra("ComicBook", item);
        intent.putExtra("intent", "remove");
        startActivity(intent);
        return true;
    }

    private void getIndexList(ArrayList<ComicBook> arr) {
        sideIndex = new LinkedHashMap<>();
        int i = 0;
        for (ComicBook cb : arr) {
            String firstLetter = cb.getVolume().substring(0, 1);
            if (sideIndex.get(firstLetter) == null) {
                sideIndex.put(firstLetter, i);
                i++;
            }
        }
    }

    private void displayIndex() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.side_index);
        TextView textView;
        List<String> indexList = new ArrayList<>(sideIndex.keySet());
        for (String s : indexList) {
            textView = (TextView) getLayoutInflater().inflate(R.layout.index_side, null);
            textView.setText(s);
            textView.setOnClickListener(this);
            linearLayout.addView(textView);
        }
    }

    private ArrayList<ComicBook> getLibrary() {
        ArrayList<ComicBook> returnList = new ArrayList<>();
        try {
            List<ComicBook> fetchComicBooks = new FetchComicBooks().execute().get();
            for (ComicBook cb : fetchComicBooks) {
                if (cb.isInLibrary())
                    returnList.add(cb);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return returnList;
    }

    @Override
    public void onClick(View v) {
        TextView selected = (TextView) v;
        library.setSelection(sideIndex.get(selected.getText()));
    }
}
