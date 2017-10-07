package cooktopper.cooktopperapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class BurnerListAdapter extends RecyclerView.Adapter<ViewHolder>{

    private String[] dataset;

    public BurnerListAdapter(String[] myDataset){
        dataset = myDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.burner_list_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        TextView tv = holder.view.findViewById(R.id.info_text);
        tv.setText(dataset[position]);

    }

    @Override
    public int getItemCount(){
        return dataset.length;
    }
}