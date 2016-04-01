package uk.ac.ncl.j_carlton.comichub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapted from: https://github.com/codepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
 */
public class ComicBookAdapter extends ArrayAdapter<ComicBook> {

    public ComicBookAdapter(Context context, ArrayList<ComicBook> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ComicBook cb = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comic_book, parent, false);

        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView issue = (TextView) convertView.findViewById(R.id.issue);

        if (cb.getName() == null)
            name.setText(cb.getVolume());
        else
            name.setText(cb.getName());
        issue.setText("Issue #: " + cb.getIssue());

        name.setTextSize(16);
        issue.setTextSize(16);
        return convertView;
    }
}
