package cooktopper.cooktopperapp.requests;

import android.util.Log;

import org.json.JSONArray;

import java.util.concurrent.ExecutionException;

public class GetResponse implements Runnable {

    private GetRequest request;
    private JSONArray response;

    public GetResponse(GetRequest request){
        this.request = request;
    }

    @Override
    public void run(){
        try{
            JSONArray jsonArray = request.execute().get();
            Log.d("getReponse", jsonArray.toString());
            setResponse(jsonArray);
        } catch(InterruptedException e){
            e.printStackTrace();
        } catch(ExecutionException e){
            e.printStackTrace();
        }
    }

    private void setResponse(JSONArray response){
        this.response = response;
    }

    public JSONArray getResponse(){
        return this.response;
    }

}
