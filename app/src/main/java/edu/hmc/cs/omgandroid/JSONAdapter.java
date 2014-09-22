package edu.hmc.cs.omgandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Max on 9/22/2014.
 *
 * Adapts JSON information from openlibrary.org
 * so it can be used in a ListView
 */
public class JSONAdapter extends BaseAdapter {
    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/";

    Context mContext;
    LayoutInflater mInflater;
    JSONArray mJsonArray;

    /**
     * Constructor
     */
    public JSONAdapter(Context context, LayoutInflater inflater) {
        mContext = context;
        mInflater = inflater;
        mJsonArray = new JSONArray();
    }

    @Override
    public int getCount() {
        return mJsonArray.length();
    }

    @Override
    public Object getItem(int position) {
        return mJsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int identification) {
        // This particular dataset uses String IDs
        // but there has to be something in this method
        return identification;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // Check if the view already exists
        // If so, no need to inflate and findViewById again
        if (convertView == null) {
            // Inflate the custom row layout from your XML
            convertView = mInflater.inflate(R.layout.row_book, null);

            // Create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.img_thumbnail);
            holder.titleTextView = (TextView) convertView.findViewById(R.id.text_title);
            holder.authorTextView = (TextView) convertView.findViewById(R.id.text_author);

            // Hang onto this holder for future recycling
            convertView.setTag(holder);
        }
        else {
            // Skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }

        // Get the current book's data in JSON form
        JSONObject jsonObject = (JSONObject) getItem(position);

        // See if there is a cover ID in the Object
        if (jsonObject.has("cover_i")) {
            // If so, grab the Cover ID out from the object
            String imageID = jsonObject.optString("cover_i");

            // Construct the image URL (specific to API)
            String imageURL = IMAGE_URL_BASE + imageID + "-S.jpg";

            // Use Picasso to load the image
            // Temporarily have a placeholder image in case it's slow to load
            Picasso.with(mContext).load(imageURL).placeholder(R.drawable.ic_books).into(holder.thumbnailImageView);
        }
        else {
            // If there is no cover ID in the object, use a placeholder
            holder.thumbnailImageView.setImageResource(R.drawable.ic_books);
        }

        // Grab the title and author from the JSON
        String bookTitle = "";
        String authorName = "";

        if (jsonObject.has("title")) {
            bookTitle = jsonObject.optString("title");
        }

        if (jsonObject.has("author_name")) {
            authorName = jsonObject.optJSONArray("author_name").optString(0);
        }

        // Send these Strings to the TextViews for display
        holder.titleTextView.setText(bookTitle);
        holder.authorTextView.setText(authorName);

        return convertView;
    }

    public void updateData(JSONArray jsonArray) {
        // Update the adapter's dataset
        mJsonArray = jsonArray;
        notifyDataSetChanged();
    }

    /**
     * This is used so we only ever have to do
     * inflation and finding by ID once per view
     */
    private static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView titleTextView;
        public TextView authorTextView;
    }
}
