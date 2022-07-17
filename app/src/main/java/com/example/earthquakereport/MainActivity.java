package com.example.earthquakereport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import android.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuakeDetails>>
{
    /** URL for earthquake data from the USGS dataset */
   /* private static final String USGS_REQUEST_URL =
            "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=4&limit=50";*/
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";//For URI builder
    private static final String LOG_TAG = MainActivity.class.getName();
    /*When we get to the onPostExecute() method, we need to update
    the ListView. The only way to update the contents of the list is
    to update the data set within the EarthquakeAdapter. To access and
    modify the instance of the EarthquakeAdapter, we need to make it
     a global variable in the EarthquakeActivity.*/
    private EarthquakeAdapter mAdapter;
   

    /**
     *  /**
     *  * Constant value for the earthquake loader ID. We can choose any integer.
     *  * This really only comes into play if you're using multiple loaders.
     *  */

    private static final int EARTHQUAKE_LOADER_ID=1;

    private TextView mEmptyStateTextView;
/**We need onCreateLoader(), for when the LoaderManager has determined that the loader with our specified ID isn't running, so we should create a new one.
 * @return*/
    @Override
    public Loader<List<EarthQuakeDetails>> onCreateLoader(int i, Bundle bundle) {
        /*// Create a new loader for the given URL
        return new EarthquakeLoader(this, USGS_REQUEST_URL);*///Used before constructing desired url


            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
            String minMagnitude = sharedPrefs.getString(
                    getString(R.string.settings_min_magnitude_key),
                    getString(R.string.settings_min_magnitude_default));
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

           // Log.e(LOG_TAG+"###","Count="+sharedPrefs.getString(getString(R.string.settings_seekbar_key),getString(R.string.settings_seekbar_default)));
            //String count=sharedPrefs.getString(getString(R.string.settings_seekbar_key),getString(R.string.settings_seekbar_default));
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();

            uriBuilder.appendQueryParameter("format", "geojson");
            uriBuilder.appendQueryParameter("limit", "10");
            uriBuilder.appendQueryParameter("minmag", minMagnitude);
            uriBuilder.appendQueryParameter("orderby", orderBy);
        Log.i(LOG_TAG, "entered onCreateLoader() method, will return new loader for given URL");
            return new EarthquakeLoader(this, uriBuilder.toString());

    }
    /**We need onLoadFinished(), where we'll do exactly what we did in onPostExecute(), and use the earthquake data to update our UI - by updating the dataset in the adapter.*/
    @Override
    public void onLoadFinished(Loader<List<EarthQuakeDetails>> loader, List<EarthQuakeDetails> earthquakes) {

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.progress_bar);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_earthquakes);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }
        Log.i(LOG_TAG, "checked (earthquakes != null && earthquakes.isEmpty()) and added earthquakes to adapter using addAll ");
    }
    /**And we need onLoaderReset(), we're we're being informed that the data from our loader is no longer valid. This isn't actually a case that's going to come up with our simple loader, but the correct thing to do is to remove all the earthquake data from our UI by clearing out the adapter’s data set.*/
    @Override
    public void onLoaderReset(Loader<List<EarthQuakeDetails>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

    /**Finally, to retrieve an earthquake, we need to get the loader manager and tell the loader manager to initialize the loader with the specified ID,
     * the second argument allows us to pass a bundle of additional information, which we'll skip. The third argument is what object should receive the
     * LoaderCallbacks (and therefore, the data when the load is complete!) - which will be this activity. This code goes inside the onCreate() method of
     * the EarthquakeActivity, so that the loader can be initialized as soon as the app opens.*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

     super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "entered onCreate() and passed values to super.onCreate()");
        setContentView(R.layout.activity_main);
        Log.i(LOG_TAG, "setcontentView() called");
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new EarthquakeAdapter(this, new ArrayList<EarthQuakeDetails>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected earthquake.
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                EarthQuakeDetails currentEarthquake = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);


            }
        });

        /**To avoid the “No earthquakes found.” message blinking on the screen when the app first launches, we can
         * leave the empty state TextView blank, until the first load completes. In the onLoadFinished callback method,
         * we can set the text to be the string “No earthquakes found.” It’s okay if this text is set every time the
         * loader finishes because it’s not too expensive of an operation. There’s always tradeoffs, and this user
         * experience is better.*/
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

//        // Get a reference to the LoaderManager, in order to interact with loaders.
//        LoaderManager loaderManager = getLoaderManager();
//
//        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
//        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
//        // because this activity implements the LoaderCallbacks interface).
//        loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        Log.i(LOG_TAG, "entered onCreateOptionsMenu and return true");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            Log.i(LOG_TAG, "entered onOptionsItemSelected(MenuItem item) and return true id == R.id.action_settings ");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
