package cooktopper.cooktopperapp.presenter;

import android.content.Context;

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
        GetRequest getRequest = new GetRequest(context);
        JSONObject jsonObject = getRequest.request("http://10.0.2.2:8000/burner_state/?id=" + id);
        BurnerState burnerState = null;
        try{
            burnerState = new BurnerState(jsonObject.getInt("id"), jsonObject.getString("description"));
        } catch(JSONException e){
            e.printStackTrace();
        }

        return burnerState;
    }

}
