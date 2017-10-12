package cooktopper.cooktopperapp.presenter;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cooktopper.cooktopperapp.models.Stove;
import cooktopper.cooktopperapp.models.Temperature;
import cooktopper.cooktopperapp.requests.GetRequest;

public class TemperaturePresenter {

    private Context context;

    public TemperaturePresenter(Context context) {
        this.context = context;
    }

    public Temperature getTemperatureById(String id) {
        GetRequest getRequest = new GetRequest(context);
        JSONArray jsonArray = getRequest.request("http://10.0.2.2:8000/temperature/?id=" + id);
        JSONObject jsonObject = null;
        try{
            jsonObject = jsonArray.getJSONObject(0);
        } catch(JSONException e){
            e.printStackTrace();
        }
        Temperature temperature = null;
        try{
            temperature = new Temperature(jsonObject.getInt("id"), jsonObject.getString("description"));
        } catch(JSONException e){
            e.printStackTrace();
        }

        return temperature;
    }

}
