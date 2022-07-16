package com.example.earthquakereport;

import android.content.Context;
//import androidx.loader.content.AsyncTaskLoader;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

/**
 * Loads a list of earthquakes by using an AsyncTask to perform the
 * network request to the given URL.
 */

/**To define the EarthquakeLoader class, we extend AsyncTaskLoader and specify List as the generic parameter, which explains what type of data is expected
 * to be loaded. In this case, the loader is loading a list of Earthquake objects. Then we take a String URL in the constructor, and in loadInBackground(),
 * we'll do the exact same operations as in doInBackground back in EarthquakeAsyncTask. Important: Notice that we also override the onStartLoading()
 * method to call forceLoad() which is a required step to actually trigger the loadInBackground() method to execute.*/
public class EarthquakeLoader extends AsyncTaskLoader<List<EarthQuakeDetails>>
{

    /**TAG FOR LOG MESSAGES*/
    private static final String LOG_TAG=EarthquakeLoader.class.getName();

    /**Query URL*/
    private String mUrl;
    /**
     * Constructs a new {@link EarthquakeLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public EarthquakeLoader(Context context,String url)
    {

        super(context);
        Log.i(LOG_TAG,"inside public constructor EarthquakeLoader and called super() and assigned mUrl=url");
        mUrl=url;
    }

    @Override
    protected void onStartLoading()
    {    /**Force an asynchronous load. Unlike startLoading() this will ignore a previously loaded data set and load a new one. This simply calls through
     to the implementation's onForceLoad(). You generally should only call this when the loader is started -- that is, isStarted() returns true.
     Must be called from the process's main thread.*/

        forceLoad();
    }
    /**'This is on a background thread
     *
     */
    @Override

    public List<EarthQuakeDetails>  loadInBackground()
    {
        if (mUrl == null) {
            Log.i(LOG_TAG,"inside loadInBackground() and mUrl==null therefore returming null");
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<EarthQuakeDetails> earthquakes = QueryUtils.fetchEarthquakeData(mUrl);
        Log.i(LOG_TAG,"inside loadInBackground()Perform the network request, parse the response, and extract and returning a list of earthquakes.");
        return earthquakes;
    }
}
