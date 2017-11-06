package cooktopper.cooktopperapp.presenter.requests;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetRequest extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... params){

        HttpURLConnection urlConnection = null;
        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        } catch (MalformedURLException MalformedURL) {
            Log.e(params[0]," MALFORMED URL");
        } catch (IOException InOutException) {
            Log.e("Connection failed",",Internet may be off");
        } finally {
            urlConnection.disconnect();
        }

        return result.toString();
    }
}
