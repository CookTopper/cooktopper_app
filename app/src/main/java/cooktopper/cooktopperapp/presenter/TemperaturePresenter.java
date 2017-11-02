package cooktopper.cooktopperapp.presenter;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import cooktopper.cooktopperapp.R;
import cooktopper.cooktopperapp.models.Stove;
import cooktopper.cooktopperapp.models.Temperature;
import cooktopper.cooktopperapp.requests.GetRequest;

public class TemperaturePresenter {

    private Context context;

    public TemperaturePresenter(Context context) {
        this.context = context;
    }

    public Temperature getTemperatureFromJsonString(String jsonString){
        JSONArray responseAsJson = null;
        Temperature temperature = null;

        try{
            responseAsJson = new JSONArray(jsonString);
            temperature = new Temperature(responseAsJson.getJSONObject(0).getInt("id"),
                    responseAsJson.getJSONObject(0).getString("description"));
        } catch(JSONException e){
            e.printStackTrace();
        }

        return temperature;
    }

    public Temperature getTemperatureById(String id) {
        GetRequest getRequest = new GetRequest();
        String response = "";
        try{
            response =  getRequest.execute("http://" +
                    context.getResources().getString(R.string.webserver_ip) + "/temperature/?id="
                    + id).get().toString();
        } catch(InterruptedException e){
            e.printStackTrace();
        } catch(ExecutionException e){
            e.printStackTrace();
        }

        Temperature temperature = getTemperatureFromJsonString(response);

        return temperature;
    }
}
