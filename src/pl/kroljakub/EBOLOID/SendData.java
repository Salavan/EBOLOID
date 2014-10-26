package pl.kroljakub.EBOLOID;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

class SendData extends AsyncTask<String, Void, Void> {

    private Exception exception;

    protected Void doInBackground(String... url) {
        try {
            BufferedReader in = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            URI website = new URI(url[0]);
            request.setURI(website);
            HttpResponse response = httpclient.execute(request);
            in = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));

            String line = in.readLine();

        } catch (Exception e) {
            this.exception = e;
        }

        return null;
    }

}
