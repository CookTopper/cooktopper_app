package cooktopper.cooktopperapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cooktopper.cooktopperapp.models.Burner;
import cooktopper.cooktopperapp.models.BurnerState;
import cooktopper.cooktopperapp.models.Stove;
import cooktopper.cooktopperapp.models.Temperature;

public class BurnerFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public BurnerFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_burner, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        Stove stove = new Stove(1, "abc");
        Temperature temperature = new Temperature(1, "Baixa");
        BurnerState burnerStateOn = new BurnerState(1, "Ligada");
        BurnerState burnerStateOff = new BurnerState(0, "Desligada");
        Burner burner1 = new Burner(1, "Boca Pequena", stove, temperature, burnerStateOn);
        Burner burner2 = new Burner(2, "Boca Grande", stove, temperature, burnerStateOff);
        List<Burner> burners = new ArrayList<>();
        burners.add(burner1);
        burners.add(burner2);

        adapter = new BurnerListAdapter(burners, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}