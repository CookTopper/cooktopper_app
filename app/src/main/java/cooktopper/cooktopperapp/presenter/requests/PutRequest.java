package cooktopper.cooktopperapp.presenter.requests;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;

public class PutRequest extends AsyncTask<String, String, String> {
    private int response;

    @Override
    protected String doInBackground(String... params){

        String httpResult = null;
        HttpURLConnection urlConnection = null;

        try {
            String url = params[0];
            urlConnection = RequestTools.setBody(url, "PUT");
            Log.d("Info", "Connection body set");

            String jsonAsString = params[1];
            httpResult = RequestTools.makeResult(urlConnection,jsonAsString);
            Log.d("Info", "RESULT: " + httpResult);

            response = urlConnection.getResponseCode();
            Log.d("Info", urlConnection.getResponseMessage()+ " " + response + " " + httpResult);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.d("Error", "Problem with URL");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error", "IOException on PUT");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", e.getMessage(), e);
            return null;
        } finally{
            if (urlConnection != null)
                urlConnection.disconnect();
        }

        return httpResult;
    }

    public int getResponse() {
        return response;
    }
}
