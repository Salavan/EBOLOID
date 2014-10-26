package pl.kroljakub.EBOLOID;

/**
 * Created by Aleksadner on 2014-10-25.
 */

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

/**
 * Created by Aleksadner on 2014-10-25.
 */
public class GpsLocalization implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener {

    private LocationClient mLocationClient;
    private LocationRequest mLocationRequest;
    private Context mcontext;
    private Location mlocation;

    public GpsLocalization(Context context) {
        Log.v("Debug", "Constructor");
        this.mcontext = context;
        mLocationRequest = LocationRequest.create();

        //5 sek
        mLocationRequest.setInterval(5000);

        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        //1 sek
        mLocationRequest.setFastestInterval(1000);

        mLocationClient = new LocationClient(context, this, this);

        mLocationClient.connect();
    }


    public Location getLocation()  {
        return mlocation;

    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.v("Debug", "onConnected");
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    @Override
    public void onDisconnected() {
        Log.v("Debug", "onDisconnected");
    }

    @Override
    public void onLocationChanged(Location location) {
        mlocation = location;
        Log.v("GPS_DEBUG", location.getLatitude()+", "+location.getLongitude());
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.v("Debug", "onConnectionFailed");
    }

}