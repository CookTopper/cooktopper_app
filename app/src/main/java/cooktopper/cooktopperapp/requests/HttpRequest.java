package cooktopper.cooktopperapp.requests;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class HttpRequest {

    private static HttpRequest instance;
    private Context context;
    private RequestQueue requestQueue;

    public HttpRequest(Context context) {
        this.context = context;
        this.requestQueue = getRequestQueue();
    }

    public static synchronized HttpRequest getInstance(Context context) {
        if (instance == null) {
            instance = new HttpRequest(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}
