package cooktopper.cooktopperapp.presenter;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cooktopper.cooktopperapp.R;
import cooktopper.cooktopperapp.models.Programming;
import cooktopper.cooktopperapp.models.Shortcut;
import cooktopper.cooktopperapp.presenter.requests.GetRequest;

public class ShortcutPresenter {

    private Context context;

    public ShortcutPresenter(Context context) {
        this.context = context;
    }

    public List<Shortcut> getShortcuts() {

        GetRequest getRequest = new GetRequest();
        String response = "";
        try{
            response =  getRequest.execute("http://" +
                    context.getResources().getString(R.string.webserver_ip) + "/shortcut/")
                    .get().toString();

        } catch(InterruptedException e){
            e.printStackTrace();
        } catch(ExecutionException e){
            e.printStackTrace();
        }

        List<Shortcut> shortcuts = getShortcutsFromJsonString(response);

        return shortcuts;
    }

    public List<Shortcut> getShortcutsFromJsonString(String response) {
        ProgrammingPresenter programmingPresenter = new ProgrammingPresenter(context);

        JSONArray jsonArray = null;
        List<Shortcut> shortcuts = new ArrayList<>();
        try{
            jsonArray = new JSONArray(response);
            for(int i=0; i<jsonArray.length(); i++){
                int currentProgrammingId = jsonArray.getJSONObject(i).getInt("programming");
                Programming programming = programmingPresenter.getProgrammingById(
                        currentProgrammingId);

                int currentShortcutId = jsonArray.getJSONObject(i).getInt("id");
                Shortcut shortcut = new Shortcut(currentShortcutId,
                        jsonArray.getJSONObject(i).getString("description"),
                        programming);

                shortcuts.add(shortcut);
            }

        } catch(JSONException e){
            e.printStackTrace();
        }

        return shortcuts;
    }
}
