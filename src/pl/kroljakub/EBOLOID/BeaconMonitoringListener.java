package pl.kroljakub.EBOLOID;

import com.kontakt.sdk.android.device.Beacon;
import com.kontakt.sdk.android.device.Region;
import com.kontakt.sdk.android.manager.BeaconManager;

import java.util.List;

public class BeaconMonitoringListener {
    private BeaconManager.MonitoringListener listener;
    private MyBeacons beacons = null;


    BeaconMonitoringListener() {
        beacons = new MyBeacons();

        listener = new BeaconManager.MonitoringListener() {
            @Override
            public void onMonitorStart() {

            }

            @Override
            public void onMonitorStop() {

            }

            @Override
            public void onBeaconsUpdated(Region region, List<Beacon> bcs) {
                for(Beacon b : bcs) {
                    //Log.d("UPDATE", b.getName());
                    beacons.UpdateBeacon(b);
                }
            }

            @Override
            public void onBeaconAppeared(Region region, Beacon b) {
                //Log.d("NEW", b.getName());
                beacons.UpdateBeacon(b);
            }

            @Override
            public void onRegionEntered(Region region) {

            }

            @Override
            public void onRegionAbandoned(Region region) {

            }
        };
    }

    public BeaconManager.MonitoringListener getListener() {
        return listener;
    }

    public MyBeacons getBeacons() {
        return beacons;
    }
}
