package pl.kroljakub.EBOLOID;

import com.kontakt.sdk.android.device.Beacon;

import java.util.LinkedList;
import java.util.List;

public class MyBeacons {
    List<MyBeacon> beacons;
    MyBeacons() {
        beacons = new LinkedList<MyBeacon>();
    }

    public void UpdateBeacon(Beacon beacon) {
        boolean ok = false;
        for (MyBeacon b : beacons) {
            if(b.Equals(beacon)) {

                ok = true;
                b.Update(beacon);
                break;
            }
        }

        if(!ok)
            beacons.add(new MyBeacon(beacon));
    }
}
