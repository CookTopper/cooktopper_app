package cooktopper.cooktopperapp.presenter;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cooktopper.cooktopperapp.R;
import cooktopper.cooktopperapp.models.BurnerState;
import cooktopper.cooktopperapp.models.Programming;
import cooktopper.cooktopperapp.models.Stove;
import cooktopper.cooktopperapp.models.Temperature;
import cooktopper.cooktopperapp.presenter.requests.GetRequest;
import cooktopper.cooktopperapp.models.Burner;
import cooktopper.cooktopperapp.presenter.requests.PostRequest;

public class BurnerPresenter {

    private Context context;

    public BurnerPresenter(Context context) {
        this.context = context;
    }

    public void updateBurnerState(boolean state, Burner burner) {
        BurnerState burnerState;
        if(state){
            burnerState = new BurnerState(2, "Ligada");
        }
        else {
            burnerState = new BurnerState(1, "Desligada");
        }

        burner.setBurnerState(burnerState);
    }

    public void updateBurnerTemperature(int temperatureId, Burner burner) {
        Temperature temperature = null;
        final int BAIXA = 0, MEDIA = 1, ALTA = 2;
        switch(temperatureId){
            case BAIXA:
                temperature = new Temperature(12, "baixa");
                break;
            case MEDIA:
                temperature = new Temperature(11, "media");
                break;
            case ALTA:
                temperature = new Temperature(1, "alta");
                break;
        }

        burner.setTemperature(temperature);
    }

    public int updateBurner(Burner currentBurner, int time){
        PostRequest postRequest = new PostRequest();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("burner_id", currentBurner.getId());
            jsonObject.put("new_temperature", currentBurner.getTemperature().getId());
            jsonObject.put("new_burner_state", currentBurner.getBurnerState().getId());
            jsonObject.put("programmed_time", time);
            jsonObject.put("programming_id", 0);

        }
        catch(JSONException jsonException){
            Log.d("Error", "Problem while parsing burner to JSONObject");
        }

        postRequest.execute("http://" + context.getResources().getString(R.string.webserver_ip) +
                "/request_burner/", jsonObject.toString());
        int response = postRequest.getResponse();

        return response;
    }

    private int getDurationInSeconds(int hourOn ,int minuteOn, int hourOff, int minuteOff){
        int expectedDuration;
        int scheduleHourTurnOffInMinutes = hourOff * 3600 + minuteOff * 60;
        int scheduleHourTurnOnInMinutes = hourOn * 3600 + minuteOn * 60;

        if(scheduleHourTurnOffInMinutes > scheduleHourTurnOnInMinutes){
            expectedDuration = scheduleHourTurnOffInMinutes - scheduleHourTurnOnInMinutes;
        }
        else {
            expectedDuration = 24 * 3600 - scheduleHourTurnOnInMinutes * 60 +
                    scheduleHourTurnOffInMinutes * 60;
        }

        return expectedDuration;
    }

    private int getProgrammedTimeInSeconds(int hourOn, int minuteOn,
                                           int nowSeconds,
                                           int nowTimeInSeconds) {
        int startTimeInSeconds;
        int typedTimeInSeconds = hourOn * 3600 + minuteOn * 60;
        int absoluteTimeInSeconds = (int) (new Date().getTime() / 1000.0);
        if(typedTimeInSeconds > nowTimeInSeconds){
            startTimeInSeconds = (absoluteTimeInSeconds - nowSeconds) + (typedTimeInSeconds -
                nowTimeInSeconds);
        }
        else {
            startTimeInSeconds = (absoluteTimeInSeconds - nowSeconds) + (24 * 3600 - nowTimeInSeconds
                    + typedTimeInSeconds);
        }

        return startTimeInSeconds;
    }

    private int scheduleBurner(Programming programming, Burner currentBurner){
        PostRequest postRequest = new PostRequest();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("new_burner_state", programming.getBurnerState().getId());
            jsonObject.put("programmed_time", programming.getProgrammedTime());
            jsonObject.put("programming_id", programming.getId());
            jsonObject.put("burner_id", currentBurner.getId());
            jsonObject.put("new_temperature", programming.getTemperature().getId());
        }
        catch(JSONException jsonException){
            Log.d("Error", "Problem while parsing burner to JSONObject");
        }

        postRequest.execute("http://" +
                context.getResources().getString(R.string.webserver_ip) +
                "/request_burner/", jsonObject.toString());
        int response = postRequest.getResponse();

        return response;
    }

    public int scheduleBurnerOnAndOff(Burner currentBurner, int hourOn, int minuteOn,
                                          int hourOff, int minuteOff){
        Calendar now = Calendar.getInstance();
        int nowHour = now.get(Calendar.HOUR_OF_DAY);
        int nowMinute = now.get(Calendar.MINUTE);
        int nowSeconds = now.get(Calendar.SECOND);

        int nowTimeInSeconds =  nowHour * 3600 + nowMinute * 60;

        int programmedHour = getProgrammedTimeInSeconds(hourOn, minuteOn,
                                                        nowSeconds,
                                                        nowTimeInSeconds);

        int expectedDuration = getDurationInSeconds(hourOn, minuteOn, hourOff, minuteOff);

        int absoluteTimeInSeconds = (int) (new Date().getTime() / 1000.0);
        Programming programming = new Programming(currentBurner.getBurnerState(),
                currentBurner.getTemperature(),
                absoluteTimeInSeconds,
                expectedDuration,
                programmedHour);

        ProgrammingPresenter programmingPresenter = new ProgrammingPresenter(context);
        programmingPresenter.createProgramming(programming);

        Programming programmingUpdated = programmingPresenter.getProgrammingByTime(
                programming.getCreationTime());

        //Schedule burner to turn on
        scheduleBurner(programmingUpdated, currentBurner);

        //Schedule burner to turn off
        programmingUpdated.setProgrammedTime(programmedHour + expectedDuration);
        BurnerState burnerState = new BurnerState(1, "Desligada");
        programmingUpdated.setBurnerState(burnerState);
        int scheduleResponseOff = scheduleBurner(programmingUpdated, currentBurner);

        return scheduleResponseOff;
    }

    public int scheduleBurnerOnOrOff(Burner currentBurner, int hour, int minute){
        Calendar now = Calendar.getInstance();
        int nowHour = now.get(Calendar.HOUR_OF_DAY);
        int nowMinute = now.get(Calendar.MINUTE);
        int nowSeconds = now.get(Calendar.SECOND);

        int nowTimeInSeconds =  nowHour * 3600 + nowMinute * 60;

        int programmedHour = getProgrammedTimeInSeconds(hour, minute,
                                                        nowSeconds,
                                                        nowTimeInSeconds);
        int expectedDuration = -1;

        int absoluteTimeInSeconds = (int) (new Date().getTime() / 1000.0);
        Programming programming = new Programming(currentBurner.getBurnerState(),
                currentBurner.getTemperature(),
                absoluteTimeInSeconds,
                expectedDuration,
                programmedHour);

        ProgrammingPresenter programmingPresenter = new ProgrammingPresenter(context);
        programmingPresenter.createProgramming(programming);

        Programming programmingUpdated = programmingPresenter.getProgrammingByTime(
                programming.getCreationTime());

        int scheduleResponse = scheduleBurner(programmingUpdated, currentBurner);

        return scheduleResponse;
    }

    public List<Burner> getBurners() {

        GetRequest getRequest = new GetRequest();
        String response = "";
        try{
            response =  getRequest.execute("http://" +
                    context.getResources().getString(R.string.webserver_ip) + "/burner/")
                    .get().toString();

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
                        burnerState,
                        jsonArray.getJSONObject(i).getInt("time"));

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
