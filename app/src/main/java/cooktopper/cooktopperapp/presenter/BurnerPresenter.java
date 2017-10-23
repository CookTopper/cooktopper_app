package cooktopper.cooktopperapp.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;

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
        PutRequest putRequest = new PutRequest();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("temperature", currentBurner.getTemperature().getId());
            jsonObject.put("burner_state", currentBurner.getBurnerState().getId());

        }
        catch(JSONException jsonException){
            Log.d("Error", "Problem while parsing burner to JSONObject");
        }

        String idAsString = String.valueOf(currentBurner.getId());
        putRequest.execute("http://10.0.2.2:8000/burner/" + idAsString, jsonObject.toString());

       return putRequest.getResponse();
    }

    public List<Burner> getBurners() {

        GetRequest getRequest = new GetRequest();
        String response = "";
        try{
            response =  getRequest.execute("http://10.0.2.2:8000/burner/").get().toString();
        } catch(InterruptedException e){
            e.printStackTrace();
        } catch(ExecutionException e){
            e.printStackTrace();
        }

        List<Burner> burners = getBurnersFromJsonString(response);

        return burners;
    }

    public List<Burner> getBurnersFromJsonString(String response) {
        BurnerStatePresenter burnerStatePresenter = new BurnerStatePresenter(context);
        StovePresenter stovePresenter = new StovePresenter(context);
        TemperaturePresenter temperaturePresenter = new TemperaturePresenter(context);

        JSONArray jsonArray = null;
        List<Burner> burners = new ArrayList<>();
        try{
            jsonArray = new JSONArray(response);
            for(int i=0; i<jsonArray.length(); i++){
                int currentBurnerId = jsonArray.getJSONObject(i).getInt("id");
                BurnerState burnerState = burnerStatePresenter.getBurnerStateById(
                        String.valueOf(currentBurnerId));

                Stove stove = stovePresenter.getStoveById(String.valueOf(currentBurnerId));

                Temperature temperature = temperaturePresenter.getTemperatureById(String
                        .valueOf(currentBurnerId));

                Burner burner = new Burner(currentBurnerId,
                        jsonArray.getJSONObject(i).getString("description"),
                        stove,
                        temperature,
                        burnerState);

                burners.add(burner);
            }

        } catch(JSONException e){
            e.printStackTrace();
        }

        return burners;
    }

    public Burner getBurnerFromExtrasFormatJson(String json){
        Gson gson = new Gson();
        Burner burner = gson.fromJson(json, Burner.class);
        return burner;
    }

    public String getBurnerAsExtrasFormatJson(Burner burner){
        Gson gson = new Gson();
        String json = gson.toJson(burner);
        return json;
    }
}
