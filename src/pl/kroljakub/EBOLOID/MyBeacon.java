package pl.kroljakub.EBOLOID;

import com.kontakt.sdk.android.device.Beacon;

public class MyBeacon {
    int minor;
    int major;
    double lastAccuracy;
    double lastRssi;
    int timesWithoutChange;

    String name;

    public MyBeacon(Beacon b) {
        minor = b.getMinor();
        major = b.getMajor();
        lastAccuracy = b.getAccuracy();
        timesWithoutChange = 0;
        name = b.getName();
    }

    public String GetName() {
        return name;
    }

    public double GetAccuracy() {
        return lastAccuracy;
    }

    public int GetMajor() { return major; }

    public int GetMinor() { return minor; }

    public double GetRssi() {
        return lastRssi;
    }

    public boolean Equals(Beacon b) {
        return minor == b.getMinor() && major == b.getMajor();
    }

    public boolean Visible() {
        return timesWithoutChange <= 8;
    }

    public boolean Valid() {
        return name != null;
    }

    public boolean Update(Beacon b) {
        lastRssi = b.getRssi();
        if(Math.abs(b.getAccuracy() - lastAccuracy) < 0.000000000001) {
            timesWithoutChange++;
            if(timesWithoutChange > 8) {
                timesWithoutChange = 9;
                return false;
            }
            else
                return true;
        }
        else {
            lastAccuracy = b.getAccuracy();
            timesWithoutChange = 0;
            return true;
        }

    }

    @Override
    public String toString() {
        return "[" + major + "," + minor + "] " + name + " " + lastAccuracy;
    }
}
