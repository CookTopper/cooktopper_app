package cooktopper.cooktopperapp.requests;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestTools {

    public static String makeResult(HttpURLConnection urlConnection, String bodyMessage) throws IOException{

        OutputStream out = urlConnection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
        writer.write(bodyMessage);
        writer.flush();
        writer.close();

        String line;
        StringBuffer jsonString = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

        while((line = br.readLine()) != null){
            jsonString.append(line);
        }

        String result = jsonString.toString();

        return result;
    }

    public static HttpURLConnection setBody(String urlString, String requestMethod) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection;
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setRequestMethod(requestMethod);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.connect();

        return urlConnection;
    }
}
