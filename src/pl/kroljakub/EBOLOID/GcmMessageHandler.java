package pl.kroljakub.EBOLOID;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmMessageHandler extends IntentService {

    String mes;
    private Handler handler;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    public static boolean ALARM = false;
    public static boolean RING = false;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();

        Log.d("GCM", "CREATE");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        //Log.d("GCM", "onHandleIntent start");
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("title");
        showToast();
        Log.d("GCM", "Received : (" +messageType+")  "+extras.getString("title"));

        GcmBroadcastReceiver.completeWakefulIntent(intent);

        if(extras.get("title").equals("ALARM")) {
            ALARM = true;
        }
        else if(extras.get("title").equals("ALARM_STOP")) {
            ALARM = false;
        }
        else if(extras.get("title").equals("RING")) {
            RING = false;
        }

        //Log.d("GCM", "onHandleIntent end");
    }

    public void showToast(){
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
            }
        });

    }
}