package cooktopper.cooktopperapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cooktopper.cooktopperapp.R;

public class ShortcutFragment extends Fragment {

    public ShortcutFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        View view = inflater.inflate(R.layout.fragment_shortcut, container, false);

        return view;
    }

}
