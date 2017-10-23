package cooktopper.cooktopperapp.requests;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GetRequest extends AsyncTask<Void, Void, JSONArray> {

    private Context context;
    private String url;
    JSONArray result;

    public GetRequest(Context context, String url) {
        this.context = context;
        this.url = url;
    }

    @Override
    protected JSONArray doInBackground(Void... params){

        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                future, future);
        HttpRequest.getInstance(context).addToRequestQueue(getRequest);

        JSONArray response = null;

        try {
            response =  future.get(20, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }

        return response;
    }
}
