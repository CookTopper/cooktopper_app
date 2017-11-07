package cooktopper.cooktopperapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cooktopper.cooktopperapp.R;
import cooktopper.cooktopperapp.models.Burner;
import cooktopper.cooktopperapp.presenter.BurnerPresenter;

public class BurnerFragment extends Fragment {

    private RecyclerView recyclerView;
    private BurnerListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public BurnerFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_burner, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        BurnerPresenter burnerPresenter = new BurnerPresenter(getContext());
        List<Burner> burners = burnerPresenter.getBurners();

        adapter = new BurnerListAdapter(burners, getContext());
        recyclerView.setAdapter(adapter);

        Thread listUpdater = new Thread() {
            @Override
            public void run() {
                while(true) {
                    getActivity().runOnUiThread(new Runnable(){
                        public void run(){
                            if(adapter.updateList())
                                refreshData();
                        }
                    });
                    try{
                        sleep(5000);
                    } catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        };

        listUpdater.start();

        return view;
    }

    private void refreshData() {
        BurnerPresenter burnerPresenter = new BurnerPresenter(getContext());
        List<Burner> burners = burnerPresenter.getBurners();
        adapter.setList(burners);
        adapter.notifyDataSetChanged();
    }
}