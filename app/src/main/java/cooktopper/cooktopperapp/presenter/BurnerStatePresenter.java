package cooktopper.cooktopperapp.presenter;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import cooktopper.cooktopperapp.R;
import cooktopper.cooktopperapp.models.BurnerState;
import cooktopper.cooktopperapp.models.Stove;
import cooktopper.cooktopperapp.requests.GetRequest;

public class BurnerStatePresenter {

    private Context context;

    public BurnerStatePresenter(Context context) {
        this.context = context;
    }

    public BurnerState getBurnerStateById(String id) {
        GetRequest getRequest = new GetRequest();
        String response = "";
        try{
            response =  getRequest.execute("http://" +
                    context.getResources().getString(R.string.webserver_ip) + "/burner_state/?id=" +
                    id).get().toString();
        } catch(InterruptedException e){
            e.printStackTrace();
        } catch(ExecutionException e){
            e.printStackTrace();
        }

        BurnerState burnerState = getBurnerStateFromJsonString(response);

        return burnerState;
    }

    public BurnerState getBurnerStateFromJsonString(String jsonString){
        JSONArray responseAsJson = null;
        BurnerState burnerState = null;

        try{
            responseAsJson = new JSONArray(jsonString);
            burnerState = new BurnerState(responseAsJson.getJSONObject(0).getInt("id"),
                    responseAsJson.getJSONObject(0).getString("description"));
        } catch(JSONException e){
            e.printStackTrace();
        }

        return burnerState;
    }
}
