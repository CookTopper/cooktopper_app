package cooktopper.cooktopperapp.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cooktopper.cooktopperapp.R;
import cooktopper.cooktopperapp.models.Burner;
import cooktopper.cooktopperapp.models.Shortcut;
import cooktopper.cooktopperapp.presenter.BurnerPresenter;
import cooktopper.cooktopperapp.presenter.ShortcutPresenter;

public class ShortcutListFragment extends Fragment{

    private RecyclerView recyclerView;
    private ShortcutListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public ShortcutListFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_shortcut_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_shortcuts);
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        ShortcutPresenter shortcutPresenter = new ShortcutPresenter(getContext());
        List<Shortcut> shortcuts = shortcutPresenter.getShortcuts();

        adapter = new ShortcutListAdapter(shortcuts, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }
}