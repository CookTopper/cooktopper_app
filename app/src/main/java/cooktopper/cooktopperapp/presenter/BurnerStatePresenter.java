package cooktopper.cooktopperapp.presenter;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cooktopper.cooktopperapp.models.BurnerState;
import cooktopper.cooktopperapp.models.Stove;
import cooktopper.cooktopperapp.requests.GetRequest;

public class BurnerStatePresenter {

    private Context context;

    public BurnerStatePresenter(Context context) {
        this.context = context;
    }

    public BurnerState getBurnerStateById(String id) {
        /*GetRequest getRequest = new GetRequest(context);
        JSONArray jsonArray = getRequest.request("http://10.0.2.2:8000/burner_state/?id=" + id);
        JSONObject jsonObject = null;
        try{
            jsonObject = jsonArray.getJSONObject(0);
        } catch(JSONException e){
            e.printStackTrace();
        }
        BurnerState burnerState = null;
        try{
            burnerState = new BurnerState(jsonObject.getInt("id"), jsonObject.getString("description"));
        } catch(JSONException e){
            e.printStackTrace();
        }*/

        return new BurnerState(1,"");
    }

}
