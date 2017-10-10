package cooktopper.cooktopperapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cooktopper.cooktopperapp.models.Burner;

public class BurnerListAdapter extends RecyclerView.Adapter<BurnerListAdapter.ViewHolder>{

    private List<Burner> dataset;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public View view;

        public ViewHolder(View viewToBeSet){
            super(viewToBeSet);
            view = viewToBeSet;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, OptionsActivity.class);
            intent.putExtra("id", dataset.get(this.getAdapterPosition()).getId());
            intent.putExtra("description", dataset.get(this.getAdapterPosition()).getDescription());
            intent.putExtra("temperature", dataset.get(this.getAdapterPosition()).getTemperature()
                    .getDescription());
            intent.putExtra("state", dataset.get(this.getAdapterPosition()).getBurnerState()
                    .getId());
            context.startActivity(intent);
        }
    }

    public BurnerListAdapter(List<Burner> dataset, Context context){
       this. dataset = dataset;
       this.context = context;
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