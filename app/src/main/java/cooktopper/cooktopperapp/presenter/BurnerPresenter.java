package cooktopper.cooktopperapp.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cooktopper.cooktopperapp.models.BurnerState;
import cooktopper.cooktopperapp.models.Programming;
import cooktopper.cooktopperapp.models.ProgrammingDetails;
import cooktopper.cooktopperapp.models.Stove;
import cooktopper.cooktopperapp.models.Temperature;
import cooktopper.cooktopperapp.requests.GetRequest;
import cooktopper.cooktopperapp.models.Burner;
import cooktopper.cooktopperapp.requests.PostRequest;
import cooktopper.cooktopperapp.requests.PutRequest;

public class BurnerPresenter {

    private Context context;

    public BurnerPresenter(Context context) {
        this.context = context;
    }

    public int updateBurner(Burner currentBurner, int time){
        PostRequest postRequest = new PostRequest();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("burner_id", currentBurner.getId());
            jsonObject.put("new_temperature", currentBurner.getTemperature().getId());
            jsonObject.put("new_burner_state", currentBurner.getBurnerState().getId());
            jsonObject.put("programmed_time", time);

        }
        catch(JSONException jsonException){
            Log.d("Error", "Problem while parsing burner to JSONObject");
        }

        postRequest.execute("http://10.0.2.2:8000/request_burner", jsonObject.toString());
        int response = postRequest.getResponse();

        return response;
    }

    private int getDurationInSeconds(int hourOn ,int minuteOn, int hourOff, int minuteOff){
        int expectedDuration;
        int scheduleHourTurnOffInMinutes = hourOff * 60 + minuteOff;
        int scheduleHourTurnOnInMinutes = hourOn * 60 + minuteOn;

        if(scheduleHourTurnOffInMinutes > scheduleHourTurnOnInMinutes){
            expectedDuration = scheduleHourTurnOffInMinutes - scheduleHourTurnOnInMinutes;
        }
        else {
            expectedDuration = 24 * 60 - scheduleHourTurnOnInMinutes + scheduleHourTurnOffInMinutes;
        }

        return expectedDuration * 60;
    }

    private int getProgrammedTimeInSeconds(int hourOn, int minuteOn, int nowHour, int nowMinute,
                                       int nowTimeInSeconds){
        int sum;
        int nowHourInMinutes = nowHour * 60 + nowMinute;
        int scheduleHourTurnOnInMinutes = hourOn * 60 + minuteOn;
        if(scheduleHourTurnOnInMinutes > nowHourInMinutes){
            sum = scheduleHourTurnOnInMinutes - nowHourInMinutes;
        }
        else {
            sum = 24 * 60 - nowHourInMinutes + scheduleHourTurnOnInMinutes;
        }

        return nowTimeInSeconds + (sum * 60);
    }

    private int scheduleBurner(Programming programming){
        PutRequest putRequest = new PutRequest();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("new_burner_state", programming.getBurner().getBurnerState().getId());
            jsonObject.put("programmed_time", programming.getProgrammingDetails()
                    .getProgrammedTime());
            jsonObject.put("expected_duration", programming.getProgrammingDetails()
                    .getExpectedDuration());
            jsonObject.put("temperature", programming.getProgrammingDetails().getTemperature());
        }
        catch(JSONException jsonException){
            Log.d("Error", "Problem while parsing burner to JSONObject");
        }

        putRequest.execute("http://10.0.2.2:8000/programming", jsonObject.toString());

        return putRequest.getResponse();
    }

    public int scheduleBurnerOnAndOff(Burner currentBurner, int hourOn, int minuteOn,
                                          int hourOff, int minuteOff){
        int nowHour = Calendar.HOUR_OF_DAY;
        int nowMinute = Calendar.MINUTE;
        int nowTimeInSeconds =  (int) (new Date().getTime() / 1000.0);

        int programmedHour = getProgrammedTimeInSeconds(hourOn, minuteOn, nowHour, nowMinute,
                nowTimeInSeconds);

        int expectedDuration = getDurationInSeconds(hourOn, minuteOn, hourOff, minuteOff);

        ProgrammingDetails programmingDetails = new ProgrammingDetails(programmedHour,
                expectedDuration, currentBurner.getTemperature(), currentBurner.getBurnerState());
        Programming programming = new Programming(currentBurner, programmingDetails);

        int scheduleResponse = scheduleBurner(programming);

        return scheduleResponse;
    }

    public int scheduleBurnerOnOrOff(Burner currentBurner, int hour, int minute){
        int nowHour = Calendar.HOUR_OF_DAY;
        int nowMinute = Calendar.MINUTE;
        int nowTimeInSeconds =  (int) (new Date().getTime() / 1000.0);

        int programmedHour = getProgrammedTimeInSeconds(hour, minute, nowHour, nowMinute,
                nowTimeInSeconds);

        int expectedDuration = -1;

        ProgrammingDetails programmingDetails = new ProgrammingDetails(programmedHour,
                expectedDuration, currentBurner.getTemperature(), currentBurner.getBurnerState());
        Programming programming = new Programming(currentBurner, programmingDetails);

        int scheduleResponse = scheduleBurner(programming);

        return scheduleResponse;
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
                int currentBurnerStateId = jsonArray.getJSONObject(i).getInt("burner_state");
                BurnerState burnerState = burnerStatePresenter.getBurnerStateById(
                        String.valueOf(currentBurnerStateId));

                int currentStoveId = jsonArray.getJSONObject(i).getInt("stove");
                Stove stove = stovePresenter.getStoveById(String.valueOf(currentStoveId));

                int currentTemperatureId = jsonArray.getJSONObject(i).getInt("temperature");
                Temperature temperature = temperaturePresenter.getTemperatureById(String
                        .valueOf(currentTemperatureId));

                int currentBurnerId = jsonArray.getJSONObject(i).getInt("id");
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
