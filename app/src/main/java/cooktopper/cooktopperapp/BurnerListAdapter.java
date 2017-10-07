package cooktopper.cooktopperapp;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cooktopper.cooktopperapp.models.Burner;

public class BurnerListAdapter extends RecyclerView.Adapter<ViewHolder>{

    private List<Burner> dataset;
    private Context context;

    public BurnerListAdapter(List<Burner> datasetToBeSet, Context contextToBeSet){
        dataset = datasetToBeSet;
        context = contextToBeSet;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.burner_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        Burner currentBurner = dataset.get(position);

        setBurnerInfo(currentBurner, holder);
    }

    private void setBurnerInfo(Burner currentBurner, ViewHolder holder){

        TextView burner_description = holder.view.findViewById(R.id.burner_description);
        burner_description.setText(currentBurner.getDescription());

        final int ON = 1;
        int burnerState = currentBurner.getBurnerState().getId();
        ImageView burner_image = holder.view.findViewById(R.id.burner_image);
        if(burnerState == ON){
            //Sets burner image to on burner image
            burner_image.setImageDrawable(context.getResources().getDrawable(R.drawable.fire_on, null));

            //Sets time and temperature info
            TextView burner_temperature = holder.view.findViewById(R.id.temperature);
            burner_temperature.setText("Temperatura: " + currentBurner.getTemperature().getDescription());

            TextView burner_time = holder.view.findViewById(R.id.time);
            burner_time.setText("1m26s");
        }
        else {
            //Sets burner image to off burner image
            burner_image.setImageDrawable(context.getResources().getDrawable(R.drawable.fire_off, null));

            //Sets temperature as off
            TextView burner_temperature = holder.view.findViewById(R.id.temperature);
            burner_temperature.setText("Desligada");
        }

    }

    @Override
    public int getItemCount(){
        return dataset.size();
    }
}