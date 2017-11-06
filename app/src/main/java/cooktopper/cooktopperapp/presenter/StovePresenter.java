package cooktopper.cooktopperapp.presenter;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import cooktopper.cooktopperapp.R;
import cooktopper.cooktopperapp.models.Stove;
import cooktopper.cooktopperapp.presenter.requests.GetRequest;
import cooktopper.cooktopperapp.presenter.requests.PutRequest;

public class StovePresenter {

    private Context context;

    public StovePresenter(Context context) {
        this.context = context;
    }

    public Stove getStoveById(String id) {
        GetRequest getRequest = new GetRequest();
        String response = "";
        try{
            response =  getRequest.execute("http://" + context.getResources()
                    .getString(R.string.webserver_ip) +"/stove/?id=" + id).get().toString();
        } catch(InterruptedException e){
            e.printStackTrace();
        } catch(ExecutionException e){
            e.printStackTrace();
        }

        Stove stove = getStoveFromJsonString(response);

        return stove;
    }

    public Stove getStoveFromJsonString(String jsonString){
        JSONArray responseAsJson = null;
        Stove stove = null;

        try{
            responseAsJson = new JSONArray(jsonString);
            stove = new Stove(responseAsJson.getJSONObject(0).getInt("id"),
                    responseAsJson.getJSONObject(0).getString("token"),
                    responseAsJson.getJSONObject(0).getString("mac_address"));
        } catch(JSONException e){
            e.printStackTrace();
        }
        return stove;
    }

    public Stove getStoveByToken(String token){
        GetRequest getRequest = new GetRequest();
        String response = "";
        try{
            response =  getRequest.execute("http://" +
                    context.getResources().getString(R.string.webserver_ip) + "/stove/?token=" +
                    token).get().toString();

            if (response.length() <= 2){
                Log.d("TA VAZIO! >>> ", response + " | LENGHT::::" + response.length());
                Log.e("BAD RESPONSE ERROR >>> ", "EMPTY JSON");
            }

            Log.i("RESPONSE >>> ", response);

        } catch(InterruptedException e){
            e.printStackTrace();
        } catch(ExecutionException e){
            e.printStackTrace();
        }

        Stove stove = getStoveFromJsonString(response);

        return stove;
    }

    public int updateStoveFields(Stove currentStove){
        PutRequest putRequest = new PutRequest();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("token", currentStove.getToken());
            jsonObject.put("mac_address", currentStove.getMobileMacAddress());
        }
        catch(JSONException jsonException){
            Log.d("Error", "Problem while parsing burner to JSONObject");
        }

        String idAsString = String.valueOf(currentStove.getId());
        putRequest.execute("http://" + context.getResources().getString(R.string.webserver_ip) +
                "/stove/" + idAsString + "/", jsonObject.toString());

        return putRequest.getResponse();
    }
}
