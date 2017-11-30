package cooktopper.cooktopperapp.presenter;


import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.concurrent.ExecutionException;

import cooktopper.cooktopperapp.R;
import cooktopper.cooktopperapp.models.Burner;
import cooktopper.cooktopperapp.presenter.requests.GetRequest;

public class SmokeSensorPresenter {

    private Context context;

    public SmokeSensorPresenter(Context context) {
        this.context = context;
    }

    public int getSmokeSensor() {

        GetRequest getRequest = new GetRequest();
        String response = "";
        try{
            response =  getRequest.execute("http://" +
                    context.getResources().getString(R.string.webserver_ip) + "/smoke_sensor").get();

        } catch(InterruptedException e){
            e.printStackTrace();
        } catch(ExecutionException e){
            e.printStackTrace();
        }

        JSONArray jsonArray = null;
        int smokeSensorLevel = -1;
        try{
            jsonArray = new JSONArray(response);
            smokeSensorLevel = jsonArray.getJSONObject(0).getInt("smoke_level");
        } catch(JSONException e){
            e.printStackTrace();
        }

        return smokeSensorLevel;
    }
}
