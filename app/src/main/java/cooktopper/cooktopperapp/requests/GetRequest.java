package cooktopper.cooktopperapp.requests;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

public class GetRequest {

    private Context context;
    JSONArray result;

    public GetRequest(Context context) {
        this.context = context;
    }

    public JSONArray request(String url){
        JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response){
                        Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
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
