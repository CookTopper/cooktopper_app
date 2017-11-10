package cooktopper.cooktopperapp.view;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import cooktopper.cooktopperapp.R;
import cooktopper.cooktopperapp.models.Burner;
import cooktopper.cooktopperapp.presenter.BurnerPresenter;

public class BurnerListAdapter extends RecyclerView.Adapter<BurnerListAdapter.ViewHolder>{

    private List<Burner> dataset;
    private Context context;
    private RadioGroup temperatureRadioGroup;
    private Button burnerStateButton;
    private RadioGroup.OnCheckedChangeListener radioGroupListener;
    private boolean cleanRadioGroup = false;
    private boolean updateList = true;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            RadioGroup.OnCheckedChangeListener{

        private View view;

        public ViewHolder(View viewToBeSet){
            super(viewToBeSet);
            view = viewToBeSet;
            view.setOnClickListener(this);
            temperatureRadioGroup = view.findViewById(R.id.temperature_radio_group);
            radioGroupListener = this;
            burnerStateButton = view.findViewById(R.id.burner_state);
            burnerStateButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v == view){
                BurnerPresenter burnerPresenter = new BurnerPresenter(context
                        .getApplicationContext());
                String burnerAsJson = burnerPresenter.getBurnerAsExtrasFormatJson(dataset.get(
                        this.getAdapterPosition()));
                Intent intent = new Intent(context, OptionsActivity.class);
                intent.putExtra("burner_json", burnerAsJson);
                context.startActivity(intent);
            }
            else if(v.getId() == R.id.burner_state){
                Burner currentBurner = dataset.get(this.getAdapterPosition());
                int currentState = currentBurner.getBurnerState().getId();
                final int ON = 2;
                if(currentState == ON){
                    turnBurnerOnOrOff(currentBurner, false);
                }
                else {
                    Toast.makeText(context, "Selecione a temperatura", Toast.LENGTH_LONG).show();
                    cleanRadioGroup = true;
                    RadioGroup temperatureOptions = view.findViewById(R.id.temperature_radio_group);
                    temperatureOptions.clearCheck();

                    TextView temperatureLabel = view.findViewById(R.id.temperature_label);
                    temperatureLabel.setVisibility(View.VISIBLE);

                    temperatureOptions.setVisibility(View.VISIBLE);
                    cleanRadioGroup = false;
                    updateList = false;
                }
            }
        }

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId){
            if(!cleanRadioGroup){
                final int BAIXA = 0, MEDIA = 1, ALTA = 2;
                int temperatureId = -1;
                switch(checkedId){
                    case R.id.low_temperature:
                        temperatureId = BAIXA;
                        break;
                    case R.id.medium_temperature:
                        temperatureId = MEDIA;
                        break;
                    case R.id.high_temperature:
                        temperatureId = ALTA;
                        break;
                }
                TextView updatingWarning = view.findViewById(R.id.updating_warning);
                updatingWarning.setText("Atualizando boca...");
                updatingWarning.setVisibility(View.VISIBLE);
                updateBurner(dataset.get(this.getAdapterPosition()), temperatureId, view);
            }
        }
    }

    public BurnerListAdapter(List<Burner> dataset, Context context){
        this.dataset = dataset;
        this.context = context;
    }

    public boolean updateList(){
        return this.updateList;
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

    private void setBurnerInfo(final Burner currentBurner, final ViewHolder holder){

        temperatureRadioGroup.setOnCheckedChangeListener(null);

        TextView burnerDescription = holder.view.findViewById(R.id.burner_description);
        setBurnerDescription(burnerDescription,currentBurner);

        Button burnerStateButton = holder.view.findViewById(R.id.burner_state);
        ImageView burner_image = holder.view.findViewById(R.id.burner_image);
        final int ON = 2;
        int burnerState = currentBurner.getBurnerState().getId();
        if(burnerState == ON){
           burnerStateButton.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
           burnerStateButton.setText("Desligar");
           burner_image.setImageDrawable(context.getResources().getDrawable(R.drawable.fire_on, null));
           showOnLayout(holder);
        }
        else {
            burnerStateButton.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            burnerStateButton.setText("Ligar");
            burner_image.setImageDrawable(context.getResources().getDrawable(R.drawable.fire_off, null));
            showOffLayout(holder);
        }

        int temperatureId = currentBurner.getTemperature().getId();
        setInitialTemperature(temperatureId, holder);

        temperatureRadioGroup.setOnCheckedChangeListener(radioGroupListener);
    }

    private void setBurnerDescription(TextView burnerDescription, Burner currentBurner) {
        if(currentBurner.getDescription().equals("1")){
            burnerDescription.setText("Boca Pequena");
        }
        else {
            burnerDescription.setText("Boca Grande");
        }
    }

    private void setInitialTemperature(int temperatureId, ViewHolder holder){
        final int BAIXA = 12, MEDIA = 11, ALTA = 1;
        switch(temperatureId){
            case BAIXA:
                RadioButton lowOption = holder.view.findViewById(R.id.low_temperature);
                lowOption.setChecked(true);
                break;
            case MEDIA:
                RadioButton mediumOption = holder.view.findViewById(R.id.medium_temperature);
                mediumOption.setChecked(true);
                break;
            case ALTA:
                RadioButton highOption = holder.view.findViewById(R.id.high_temperature);
                highOption.setChecked(true);
                break;
        }
    }

    private void turnBurnerOnOrOff(Burner currentBurner, boolean state){
        BurnerPresenter burnerPresenter = new BurnerPresenter(context);
        burnerPresenter.updateBurnerState(state, currentBurner);
        int time = (int) (new Date().getTime() / 1000.0);
        burnerPresenter.updateBurner(currentBurner, time);
    }

    private void updateBurner(Burner currentBurner, int checkedId, View view){
        final int ON = 2;
        //Update temperature
        BurnerPresenter burnerPresenter = new BurnerPresenter(context);
        burnerPresenter.updateBurnerTemperature(checkedId, currentBurner);
        //If burner is on, send the new temperature to server
        if(currentBurner.getBurnerState().getId() == ON){
            burnerPresenter.updateBurner(currentBurner, currentBurner.getTime());
        }
        //If burner is off, turn burner on with new temperature
        else {
            turnBurnerOnOrOff(currentBurner,true);
        }

        TextView updatingWarning = view.findViewById(R.id.updating_warning);
        updatingWarning.setVisibility(View.GONE);
        updateList = true;
    }

    private void showOnLayout(ViewHolder holder){

        TextView temperatureLabel = holder.view.findViewById(R.id.temperature_label);
        temperatureLabel.setVisibility(View.VISIBLE);

        RadioGroup temperatureOptions = holder.view.findViewById(R.id.temperature_radio_group);
        temperatureOptions.setVisibility(View.VISIBLE);
    }

    private void showOffLayout(ViewHolder holder){

        TextView temperatureLabel = holder.view.findViewById(R.id.temperature_label);
        temperatureLabel.setVisibility(View.GONE);

        RadioGroup temperatureOptions = holder.view.findViewById(R.id.temperature_radio_group);
        temperatureOptions.setVisibility(View.GONE);
    }

    public void setList(List<Burner> list) {
        this.dataset = list;
    }

    @Override
    public int getItemCount(){
        return dataset.size();
    }
}