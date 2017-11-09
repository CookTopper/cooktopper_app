package cooktopper.cooktopperapp.presenter;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import cooktopper.cooktopperapp.R;
import cooktopper.cooktopperapp.models.BurnerState;
import cooktopper.cooktopperapp.models.Programming;
import cooktopper.cooktopperapp.models.Temperature;
import cooktopper.cooktopperapp.presenter.requests.GetRequest;
import cooktopper.cooktopperapp.presenter.requests.PostRequest;

public class ProgrammingPresenter {

    private Context context;

    public ProgrammingPresenter(Context context) {
        this.context = context;
    }

    public Programming getProgrammingByTime(int creationTime){
        GetRequest getRequest = new GetRequest();
        String response = "";
        try{
            response =  getRequest.execute("http://" +
                    context.getResources().getString(R.string.webserver_ip) + "/programming/?creation_time=" +
                    creationTime).get().toString();

        } catch(InterruptedException e){
            e.printStackTrace();
        } catch(ExecutionException e){
            e.printStackTrace();
        }

        Programming programming = getProgrammingFromJsonString(response);

        return programming;
    }

    public Programming getProgrammingById(int id){
        GetRequest getRequest = new GetRequest();
        String response = "";
        try{
            response =  getRequest.execute("http://" +
                    context.getResources().getString(R.string.webserver_ip) + "/programming/?id=" +
                    id).get().toString();

        } catch(InterruptedException e){
            e.printStackTrace();
        } catch(ExecutionException e){
            e.printStackTrace();
        }

        Programming programming = getProgrammingFromJsonString(response);

        return programming;
    }

    public int createProgramming(Programming programming){

        PostRequest postRequest = new PostRequest();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("burner_state", programming.getBurnerState().getId());
            jsonObject.put("programmed_time", programming.getProgrammedTime());
            jsonObject.put("expected_duration", programming.getExpectedDuration());
            jsonObject.put("temperature", programming.getTemperature().getId());
            jsonObject.put("creation_time", programming.getCreationTime());
        }
        catch(JSONException jsonException){
            Log.d("Error", "Problem while parsing burner to JSONObject");
        }

        postRequest.execute("http://" +
                context.getResources().getString(R.string.webserver_ip) +
                "/programming/", jsonObject.toString());
        int response = postRequest.getResponse();

        return response;
    }

    public Programming getProgrammingFromJsonString(String jsonString){
        JSONArray responseAsJson = null;
        Programming programming = null;

        try{
            responseAsJson = new JSONArray(jsonString);

            TemperaturePresenter temperaturePresenter = new TemperaturePresenter(context);
            int currentTemperatureId = responseAsJson
                    .getJSONObject(0).getInt("temperature");
            Temperature temperature = temperaturePresenter.
                    getTemperatureById(String.valueOf(currentTemperatureId));

            BurnerStatePresenter burnerStatePresenter = new BurnerStatePresenter(context);
            int currentBurnerStateId = responseAsJson
                    .getJSONObject(0).getInt("burner_state");
            BurnerState burnerState = burnerStatePresenter.
                   getBurnerStateById(String.valueOf(currentBurnerStateId));

            programming = new Programming(responseAsJson.getJSONObject(0).getInt("id"),
                    burnerState,
                    temperature,
                    responseAsJson.getJSONObject(0).getInt("creation_time"),
                    responseAsJson.getJSONObject(0).getInt("expected_duration"),
                    responseAsJson.getJSONObject(0).getInt("programmed_time"));
        } catch(JSONException e){
            e.printStackTrace();
        }
        return programming;
    }
}
