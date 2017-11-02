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

import com.google.gson.Gson;

import java.util.List;

import cooktopper.cooktopperapp.models.Burner;
import cooktopper.cooktopperapp.presenter.BurnerPresenter;

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
            BurnerPresenter burnerPresenter = new BurnerPresenter(context.getApplicationContext());
            String burnerAsJson = burnerPresenter.getBurnerAsExtrasFormatJson(dataset.get(
                    this.getAdapterPosition()));
            Intent intent = new Intent(context, OptionsActivity.class);
            intent.putExtra("burner_json", burnerAsJson);
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
        if(currentBurner.getDescription().equals("1")){
            burner_description.setText("Boca Pequena");
        }
        else {
            burner_description.setText("Boca Grande");
        }

        final int ON = 2;
        int burnerState = currentBurner.getBurnerState().getId();
        ImageView burner_image = holder.view.findViewById(R.id.burner_image);
        if(burnerState == ON){
            //Sets burner image to on burner image
            burner_image.setImageDrawable(context.getResources().getDrawable(R.drawable.fire_on, null));
        }
        else {
            //Sets burner image to off burner image
            burner_image.setImageDrawable(context.getResources().getDrawable(R.drawable.fire_off, null));
        }
    }

    public void setList(List<Burner> list) {
        this.dataset = list;
    }


    @Override
    public int getItemCount(){
        return dataset.size();
    }
}