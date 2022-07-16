package com.example.earthquakereport;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.DecimalFormat;
import android.graphics.drawable.GradientDrawable;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class EarthquakeAdapter extends ArrayAdapter<EarthQuakeDetails>
{
/*We will be using the split(String string) method in the String class to split the original string at the position
where the text “ of “ occurs. The result will be a String containing the characters PRIOR to the “ of ” text and a
String containing the characters AFTER the “ of “ text. Since we’ll frequently need to refer to the “ of “ text, we can
define a static final String constant (that is a global variable) at the top of the EarthquakeAdapter class.*/
    private static final String LOCATION_SEPARATOR = " of ";
public EarthquakeAdapter(Activity context, ArrayList<EarthQuakeDetails> details)
    {
        super(context,0,details);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent )
    {
        View listItemView=convertView;
        if(listItemView==null)
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.list_item,parent,false);
//        EarthQuakeDetails currentDetail=getItem(position);
//        TextView magTextView=listItemView.findViewById(R.id.EarthQuake_Magnitude);
//
//        magTextView.setText(String.valueOf(currentDetail.getMagnitude()));
//
//        TextView placeTextView=listItemView.findViewById(R.id.City_Name);
//        placeTextView.setText(currentDetail.getPlace());
//
//        TextView dayTextView=listItemView.findViewById(R.id.Date);
//        dayTextView.setText(currentDetail.getmDay());
//        return listItemView;
        // Find the earthquake at the given position in the list of earthquakes
        EarthQuakeDetails currentEarthquake = getItem(position);

        // Find the TextView with view ID magnitude
        TextView magnitudeView = (TextView) listItemView.findViewById(R.id.magnitude);
        // Format the magnitude to show 1 decimal place
        String formattedMagnitude = formatMagnitude(currentEarthquake.getMagnitude());
        // Display the magnitude of the current earthquake in that TextView
        magnitudeView.setText(formattedMagnitude);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();

        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getMagnitudeColor(currentEarthquake.getMagnitude());

        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);


//        // Find the TextView with view ID location
//        TextView locationView = (TextView) listItemView.findViewById(R.id.location);
//        // Display the location of the current earthquake in that TextView
//        locationView.setText(currentEarthquake.getlocation());

        String originalLocation = currentEarthquake.getlocation();//Store the Entire location string to be split in a variable
        String primarylocation="", offset="";

        /*check if the original location String contains the LOCATION_SEPARATOR first, before we decide to split the
        string with that separator. If there is no LOCATION_SEPARATOR in the original location String, then we can
        assume that we should we use the “Near the” text as the location offset, and just use the original location
        String as the primary location.*/
        if (originalLocation.contains(LOCATION_SEPARATOR)) {
            String[] parts = originalLocation.split(LOCATION_SEPARATOR);
            offset = parts[0] + LOCATION_SEPARATOR;
            primarylocation = parts[1];
        } else {
            offset = getContext().getString(R.string.near_the);
            primarylocation = originalLocation;
        }
        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.primary_location);
        primaryLocationView.setText(primarylocation);

        TextView locationOffsetView = (TextView) listItemView.findViewById(R.id.location_offset);
        locationOffsetView.setText(offset);


        // Create a new Date object from the time in milliseconds of the earthquake
        Date dateObject = new Date(currentEarthquake.gettimemilliseconds());

        // Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(dateObject);
        // Display the date of the current earthquake in that TextView
        dateView.setText(formattedDate);

        // Find the TextView with view ID time
        TextView timeView = (TextView) listItemView.findViewById(R.id.time);
        // Format the time string (i.e. "4:30PM")
        String formattedTime = formatTime(dateObject);
        // Display the time of the current earthquake in that TextView
        timeView.setText(formattedTime);



        // Return the list item view that is now showing the appropriate data
        return listItemView;


    }
    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    private String formatMagnitude(double magnitude)
    {
        DecimalFormat magnitudeFormat=new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }
    private int getMagnitudeColor(double magnitude) {
        int magnitudeColorResourceId;
        int magnitudeFloor = (int) Math.floor(magnitude);
        switch (magnitudeFloor) {
            case 0:
            case 1:
                magnitudeColorResourceId = R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);
    }

}
