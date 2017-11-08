package cooktopper.cooktopperapp.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cooktopper.cooktopperapp.R;
import cooktopper.cooktopperapp.models.Burner;
import cooktopper.cooktopperapp.models.Shortcut;
import cooktopper.cooktopperapp.presenter.BurnerPresenter;

public class ShortcutListAdapter extends RecyclerView.Adapter<ShortcutListAdapter.ViewHolder> {

    private List<Shortcut> dataset;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private View view;

        public ViewHolder(View viewToBeSet){
            super(viewToBeSet);
            view = viewToBeSet;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            if(v == view){
            }
        }
    }

    public ShortcutListAdapter(List<Shortcut> dataset, Context context){
        this.dataset = dataset;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.shortcut_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i){
        TextView shortcutNameTextView = viewHolder.view.findViewById(R.id.shortcut_name);
        shortcutNameTextView.setText(dataset.get(i).getDescription());
    }

    @Override
    public int getItemCount(){
        return 0;
    }
}
