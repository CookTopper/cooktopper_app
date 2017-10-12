package cooktopper.cooktopperapp.presenter;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cooktopper.cooktopperapp.models.Stove;
import cooktopper.cooktopperapp.requests.GetRequest;

public class StovePresenter {

    private Context context;

    public StovePresenter(Context context) {
        this.context = context;
    }

    public Stove getStoveById(String id) {
        GetRequest getRequest = new GetRequest(context);
        JSONArray jsonArray = getRequest.request("http://10.0.2.2:8000/stove/?id=" + id);
        JSONObject jsonObject = null;
        try{
            jsonObject = jsonArray.getJSONObject(0);
        } catch(JSONException e){
            e.printStackTrace();
        }
        Stove stove = null;
        try{
            stove = new Stove(jsonObject.getInt("id"), jsonObject.getString("token"));
        } catch(JSONException e){
            e.printStackTrace();
        }

        return stove;
    }
}
