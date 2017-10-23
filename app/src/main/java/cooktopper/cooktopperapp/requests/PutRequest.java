package cooktopper.cooktopperapp.requests;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class PutRequest {

    private Context context;

    public PutRequest(Context context) {
        this.context = context;
    }

    public void request(String url, JSONObject object) {
        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("GetResponse", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.GetResponse", error.toString());
                    }
                }
        );

        HttpRequest.getInstance(context).addToRequestQueue(putRequest);
    }
}
