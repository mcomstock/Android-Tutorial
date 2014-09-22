package edu.hmc.cs.omgandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ShareActionProvider;

import com.squareup.picasso.Picasso;

/**
 * Created by Max on 9/22/2014.
 */
public class DetailActivity extends Activity {
    private static final String IMAGE_URL_BASE = "http://covers.openlibrary.org/b/id/"; // 13
    String mImageURL; // 13
    ShareActionProvider mShareActionProvider; // 14

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tell the activity which XML layout is right
        setContentView(R.layout.activity_detail);

        // Enable the "Up" button for more navigation options
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Access the ImageView from XML
        ImageView imageView = (ImageView) findViewById(R.id.img_cover);

        // 13. Unpack the coverID from its trip inside the Intent
        String coverID = this.getIntent().getExtras().getString("coverID");

        // See if there is a valid coverID
        if (coverID.length() > 0) {
            // Use the ID to construct an image URL
            mImageURL = IMAGE_URL_BASE + coverID + "-L.jpg";

            // Use picasso to load the image
            Picasso.with(this).load(mImageURL).placeholder(R.drawable.img_books_loading).into(imageView);
        }
    }

    private void setShareIntent() {
        // Create an Intent with the contents of the TextView
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Book Recommendation!");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mImageURL);

        // Make sure the provider knows
        // it should work with that intent
        mShareActionProvider.setShareIntent(shareIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        // This adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.main, menu);

        // Access the Share Item defined in menu XML
        MenuItem shareItem = menu.findItem(R.id.menu_item_share);

        // Access the object responsible for
        // putting together the submenu
        if (shareItem != null) {
            mShareActionProvider = (ShareActionProvider) shareItem.getActionProvider();
        }

        setShareIntent();

        return true;
    }
}
