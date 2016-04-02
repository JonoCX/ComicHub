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
 *
 * Custom adaptor so the Comic Book objects can be displayed in a
 * list view.
 *
 * @author Jonathan Carlton - 130266400
 */
public class ComicBookAdapter extends ArrayAdapter<ComicBook> {

    /**
     * Object constructor with a call to the master class - ArrayAdapter
     *
     * @param context the context for the adapter
     * @param list    the list of comic books to be displayed
     */
    public ComicBookAdapter(Context context, ArrayList<ComicBook> list) {
        super(context, 0, list);
    }

    /**
     * Get the manipulated view which displays the comic books
     * in a list view. Each entry in the list view is treated as a separate view.
     *
     * @param position      the position of the to-be-rendered comic book in the adapter
     * @param convertView   the view to be converted
     * @param parent        it's parent
     * @return the manipulated view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // fetch the comic book at the position passed
        ComicBook cb = getItem(position);

        // if the view is null
        if (convertView == null)
            // inflate it hold the comic book
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comic_book, parent, false);

        // the two text views within the R.layout.item_comic_book
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView issue = (TextView) convertView.findViewById(R.id.issue);

        // if the name is null, then get the volume
        if (cb.getName() == null)
            name.setText(cb.getVolume());
        else
            name.setText(cb.getName());
        issue.setText("Issue #: " + cb.getIssue());

        // change the text sizes.
        name.setTextSize(16);
        issue.setTextSize(16);

        return convertView;
    }
}
