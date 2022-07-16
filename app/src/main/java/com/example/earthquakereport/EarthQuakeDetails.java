package com.example.earthquakereport;

public class EarthQuakeDetails
{
    //EarthQuake magnitude
    private double mmagnitude;
    //Place of Occurance
    private String mplace;
    private long mTimeInMilliseconds;//Day of Occurance

    private String mUrl;
    public EarthQuakeDetails(double magnitude, String place, long time, String url )
    {
        mmagnitude=magnitude;
        mplace=place;
        mTimeInMilliseconds=time;
        mUrl=url;
    }
    public double getMagnitude()
    {
        return mmagnitude;
    }
    public String getlocation()
    {
        return mplace;
    }
    public long gettimemilliseconds()
    {
        return mTimeInMilliseconds;
    }
    public String getUrl()
    {
        return mUrl;
    }

}
