package com.example.earthquakereport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity
{
    private static final String LOG_TAG = SettingsActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Log.i(LOG_TAG, "entered onCreate() of Settings Activity and called onCreate() of super and setContentView() ");

    }
    public static class EarthquakePreferenceFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener
    {


        /*
         * Called during {@link #onCreate(Bundle)} to supply the preferences for this fragment.
         * Subclasses are expected to call {@link #setPreferenceScreen(PreferenceScreen)} either
         * directly or via helper methods such as {@link #addPreferencesFromResource(int)}.
         *
         * @param savedInstanceState If the fragment is being re-created from a previous saved state,
         *                           this is the state.
         * @param rootKey            If non-null, this preference fragment should be rooted at the
         *                           {@link PreferenceScreen} with this key.
*/        @Override
        public void onCreatePreferences(@Nullable  Bundle savedInstanceState, String rootKey) {
            // Load the preferences from an XML resource
           addPreferencesFromResource(R.xml.settings);
           
           /*
            However, we still need to update the preference
            summary when the settings activity is launched.
            Given the key of a preference, we can use
            PreferenceFragment's findPreference() method to
            get the Preference object, and setup the
            preference using a helper method called
            bindPreferenceSummaryToValue().

            In EarthquakePreferenceFragment:
             */
            Preference minMagnitude=findPreference(getString(R.string.settings_min_magnitude_key));
            assert minMagnitude != null;
            bindPreferenceSummaryToValue(minMagnitude);

            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);
            Log.i(LOG_TAG, "entered onCreatePreferences() of Settings Activity and bindPreferenceSummaryToValue(minMagnitude) and  bindPreferenceSummaryToValue(orderBy); ");

           // Preference seekbarValue =findPreference(getString(R.string.settings_seekbar_key));
            //bindPreferenceSummaryToValue(seekbarValue);
            //bindPreferenceSummaryToValue(seekbarValue);

}


        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
            Log.i(LOG_TAG, "entered bindPreferenceSummaryToValue and exiting now");
        }

        /**
         * Called when a preference has been changed by the user. This is called before the state
         * of the preference is about to be updated and before the state is persisted.
         *
         * @param preference The changed preference
         * @param newValue   The new value of the preference
         * @return {@code true} to update the state of the preference with the new value
         */
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
//            String stringValue=newValue.toString();
//            preference.setSummary(stringValue);
//            return true;
            Log.i(LOG_TAG, "entered onPrefernceChange() and performing the operations ");

            String stringValue = newValue.toString();
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            Log.i(LOG_TAG, "returning true from onPreferenceChange()");
            return true;

        }
    }
}
