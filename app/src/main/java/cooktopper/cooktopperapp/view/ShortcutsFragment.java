package cooktopper.cooktopperapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ShortcutsFragment extends Fragment{


    public ShortcutsFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.fragment_shortcuts, container, false);
        TextView textView = rootView.findViewById(R.id.a);
        textView.setText("atalhos");
        return rootView;
    }
}