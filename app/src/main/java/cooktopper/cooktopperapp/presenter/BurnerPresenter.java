package cooktopper.cooktopperapp.presenter;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cooktopper.cooktopperapp.models.BurnerState;
import cooktopper.cooktopperapp.models.Stove;
import cooktopper.cooktopperapp.models.Temperature;
import cooktopper.cooktopperapp.requests.GetRequest;
import cooktopper.cooktopperapp.models.Burner;
import cooktopper.cooktopperapp.requests.PutRequest;

public class BurnerPresenter {

    private Context context;

    public BurnerPresenter(Context context) {
        this.context = context;
    }


    public int updateBurner(Burner currentBurner){
        PutRequest putRequest = new PutRequest(context);
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("temperature", currentBurner.getTemperature().getId());
            jsonObject.put("burner_state", currentBurner.getBurnerState().getId());

        }
        catch(JSONException jsonException){

        }

        String idAsString = String.valueOf(currentBurner.getId());
        putRequest.request("http://10.0.2.2:8000/burner/" + idAsString + "/", jsonObject);

       return 1;
    }

    public  List<Burner> getBurners() {
        GetRequest getRequest = new GetRequest(context);
        JSONArray jsonArray = getRequest.request("http://10.0.2.2:8000/burner/");
        List<Burner> burners = new ArrayList<>();

        /*try{
            JSONArray jsonArray = new JSONArray(jsonObject);
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject currentJson = jsonArray.getJSONObject(i);
                StovePresenter stovePresenter = new StovePresenter(context);
                Stove stove = stovePresenter.getStoveById(String.valueOf(currentJson.getInt("stove")));

                TemperaturePresenter temperaturePresenter = new TemperaturePresenter(context);
                Temperature temperature = temperaturePresenter.getTemperatureById(String
                        .valueOf(currentJson.getInt("temperature")));

                BurnerStatePresenter burnerStatePresenter = new BurnerStatePresenter(context);
                BurnerState burnerState = burnerStatePresenter.getBurnerStateById(String
                        .valueOf(currentJson.getInt("burner_state")));

                Burner burner = new Burner(currentJson.getInt("id"),
                        currentJson.getString("description"),
                        stove, temperature, burnerState);

                burners.add(burner);
            }

        } catch(JSONException e){
            e.printStackTrace();
        }*/

        return burners;
    }

    public Burner getBurnerFromJson(String json){
        Gson gson = new Gson();
        Burner burner = gson.fromJson(json, Burner.class);
        return burner;
    }

    public String getBurnerAsJson(Burner burner){
        Gson gson = new Gson();
        String json = gson.toJson(burner);
        return json;
    }
}
