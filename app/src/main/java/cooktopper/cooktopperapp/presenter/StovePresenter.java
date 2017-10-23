package cooktopper.cooktopperapp.presenter;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import cooktopper.cooktopperapp.models.Stove;
import cooktopper.cooktopperapp.requests.GetRequest;

public class StovePresenter {

    private Context context;

    public StovePresenter(Context context) {
        this.context = context;
    }

    public Stove getStoveById(String id) {
        GetRequest getRequest = new GetRequest();
        String response = "";
        try{
            response =  getRequest.execute("http://127.0.0.1:8000/stove/?id=" + id).get()
                    .toString();
        } catch(InterruptedException e){
            e.printStackTrace();
        } catch(ExecutionException e){
            e.printStackTrace();
        }

        Stove stove = getStoveFromJsonString(response);

        return stove;
    }

    public Stove getStoveFromJsonString(String jsonString){
        JSONArray responseAsJson = null;
        Stove stove = null;

        try{
            responseAsJson = new JSONArray(jsonString);
            stove = new Stove(responseAsJson.getJSONObject(0).getInt("id"),
                    responseAsJson.getJSONObject(0).getString("token"));
        } catch(JSONException e){
            e.printStackTrace();
        }

        return stove;
    }
}
