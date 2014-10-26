package pl.kroljakub.EBOLOID;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.kontakt.sdk.android.configuration.ForceScanConfiguration;
import com.kontakt.sdk.android.configuration.MonitorPeriod;
import com.kontakt.sdk.android.connection.OnServiceBoundListener;
import com.kontakt.sdk.android.device.Region;
import com.kontakt.sdk.android.manager.BeaconManager;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class EboloidService extends Service {

    BeaconManager beaconManager;
    BeaconMonitoringListener bml;

    MyBeacon lcb = null;

    Intent intent;

    String mPhoneNumber;
    String IMEI;

    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "374763594059";

    private static final int REQUEST_CODE_ENABLE_BLUETOOTH = 1;

    GpsLocalization gpsLocalization;

    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM",  msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //Log.d("REGISTER GCM", msg);
            }
        }.execute(null, null, null);
    }

    int iter;
    int gpsStartIter;

    MediaPlayer music;

    @Override
    public int onStartCommand(Intent i, int flags, int startId) {
        intent = i;

        mPhoneNumber = intent.getStringExtra("number");
        IMEI = intent.getStringExtra("imei");

        getRegId();

        music = MediaPlayer.create(this, R.raw.alarm_loud);


        beaconManager = BeaconManager.newInstance(this);
        MonitorPeriod mp = new MonitorPeriod(10000, 5000);
        beaconManager.setMonitorPeriod(mp);
        beaconManager.setForceScanConfiguration(ForceScanConfiguration.DEFAULT);


        bml = new BeaconMonitoringListener();
        beaconManager.registerMonitoringListener(bml.getListener());

        try {
            Log.d("CONNECT", "MAYBE");
            connect();
        }
        catch(Exception e) {
            Log.e("MainActivity->onStart", e.toString());
        }



        iter = 0;
        gpsStartIter = 0;


        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        MyBeacons beacons = bml.getBeacons();
                        MyBeacon cb = null;
                        for(MyBeacon b : beacons.beacons) {
                            if(b.Visible() && b.Valid() && b.GetName().matches("EBOLOID.*")) {
                                if(cb == null || cb.GetAccuracy() > b.GetAccuracy())
                                    cb = b;
                            }
                        }

                        if(GcmMessageHandler.RING) {
                            music.start();
                            music.setVolume(1.0f, 1.0f);

                            GcmMessageHandler.RING = false;
                        }

                        if(GcmMessageHandler.ALARM)
                        {
                            if(gpsLocalization == null) {
                                gpsLocalization = new GpsLocalization(EboloidService.this);
                            }

                            if(gpsStartIter < 7)
                                gpsStartIter++;
                        }

                        if(cb != null && cb != lcb) {
                            lcb = cb;
                            String url = null;

                            if(GcmMessageHandler.ALARM ) {

                                if(gpsStartIter < 7) {
                                    url = "http://kroljakub.pl/hack/update.php?phone=" + mPhoneNumber
                                            + "&imei=" + IMEI
                                            + "&gpslocationx"
                                            + "&gpslocationy"
                                            + "&beacon=" + cb.GetName()
                                            + "&major=" + cb.GetMajor()
                                            + "&minor=" + cb.GetMinor()
                                            + "&accuracy=" + cb.GetAccuracy()
                                            + "&rssi=" + cb.GetRssi()
                                            + "&deviceid=" + regid;
                                }
                                else {
                                    url = "http://kroljakub.pl/hack/update.php?phone=" + mPhoneNumber
                                            + "&imei=" + IMEI
                                            + "&gpslocationx=" + gpsLocalization.getLocation().getLatitude()
                                            + "&gpslocationy=" + gpsLocalization.getLocation().getLongitude()
                                            + "&beacon=" + cb.GetName()
                                            + "&major=" + cb.GetMajor()
                                            + "&minor=" + cb.GetMinor()
                                            + "&accuracy=" + cb.GetAccuracy()
                                            + "&rssi=" + cb.GetRssi()
                                            + "&deviceid=" + regid;
                                }
                            }
                            else {
                                iter++;
                                if(iter >= 3) {
                                    iter = 0;
                                    Log.d("UPDATE", "DATA");

                                    url = "http://kroljakub.pl/hack/update.php?phone=" + mPhoneNumber
                                            + "&imei=" + IMEI
                                            + "&gpslocationx"
                                            + "&gpslocationy"
                                            + "&beacon=" + cb.GetName()
                                            + "&major=" + cb.GetMajor()
                                            + "&minor=" + cb.GetMinor()
                                            + "&accuracy=" + cb.GetAccuracy()
                                            + "&rssi=" + cb.GetRssi()
                                            + "&deviceid=" + regid;
                                }
                            }

                            try {
                                if(url != null) {
                                    new SendData().execute(url);
                                }
                            } catch (Exception e) {
                                Log.e("TO SIE WYJEBALO", e.toString());
                            }
                        }
                    }
                },
                10,
                1000*10);

        //Log.v("DUPA", "KUPA");

        return START_STICKY;
    }

    private void connect() {
        try {
            Log.d("JEBNIE?", "CZY NIE JEBNIE?");
            beaconManager.connect(new OnServiceBoundListener() {
                @Override
                public void onServiceBound() {
                    try {
                        Log.d("BEACON START", "HUE HUE HUE");
                        beaconManager.startMonitoring(Region.EVERYWHERE);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (RemoteException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public IBinder onBind(final Intent intent) {

        return null;
    }

}
