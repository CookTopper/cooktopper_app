package cooktopper.cooktopperapp.requests;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class GetRequest {

    private Context context;
    JSONObject result;

    public GetRequest(Context context) {
        this.context = context;
    }

    public JSONObject request(String url){
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        Log.d("Response", response.toString());
                        result = response;

                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        HttpRequest.getInstance(context).addToRequestQueue(getRequest);

        return result;
    }
}
