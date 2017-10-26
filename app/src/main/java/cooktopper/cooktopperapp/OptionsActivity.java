package cooktopper.cooktopperapp;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Calendar;

import cooktopper.cooktopperapp.models.Burner;
import cooktopper.cooktopperapp.models.BurnerState;
import cooktopper.cooktopperapp.models.Temperature;
import cooktopper.cooktopperapp.presenter.BurnerPresenter;

public class OptionsActivity extends AppCompatActivity implements
        MaterialSpinner.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener,
        View.OnClickListener {

    private Burner currentBurner;
    private final int ON = 2;
    private final int OFF = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MaterialSpinner spinner = findViewById(R.id.temperature);
        spinner.setItems("Baixa", "Média", "Alta");
        spinner.setOnItemSelectedListener(this);

        setCurrentBurner();

        Switch burnerState = findViewById(R.id.burner_state);
        burnerState.setOnCheckedChangeListener(this);
        setSwitchInitialState(burnerState);

        Button setTimeButton = findViewById(R.id.set_time_button);
        setTimeButton.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item){
        changeBurnerTemperature(position);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        if(isChecked) {
            changeBurnerState(ON);
            showOnLayout();
        } else {
            changeBurnerState(OFF);
            showOffLayout();
        }
    }

    private void setCurrentBurner() {
        BurnerPresenter burnerPresenter = new BurnerPresenter(getApplicationContext());
        this.currentBurner = burnerPresenter.getBurnerFromExtrasFormatJson(getIntent().getExtras()
                .getString("burner_json"));
    }

    private void setSwitchInitialState(Switch burnerState) {
        if(currentBurner.getBurnerState().getId() == ON) {
            burnerState.setChecked(true);
            showOnLayout();

        } else {
            burnerState.setChecked(false);
            showOffLayout();
        }
    }

    private void showOnLayout() {
        findViewById(R.id.burner_on_layout).setVisibility(View.VISIBLE);
        findViewById(R.id.burner_off_layout).setVisibility(View.GONE);
    }

    private void showOffLayout() {
        findViewById(R.id.burner_on_layout).setVisibility(View.GONE);
        findViewById(R.id.burner_off_layout).setVisibility(View.VISIBLE);
    }

    private void changeBurnerState(int state) {
        BurnerState burnerState;
        if(state == ON){
            burnerState = new BurnerState(2, "Ligada");
        }
        else{
            burnerState = new BurnerState(1, "Desligada");
        }
        BurnerPresenter burnerPresenter = new BurnerPresenter(getApplicationContext());
        currentBurner.setBurnerState(burnerState);
        burnerPresenter.updateBurner(currentBurner);
    }

    private void changeBurnerTemperature(int temperatureId) {
        Temperature temperature = null;
        final int BAIXA = 0, MEDIA = 1, ALTA = 2;
        switch(temperatureId){
            case BAIXA:
                temperature = new Temperature(12, "baixa");
                break;
            case MEDIA:
                temperature = new Temperature(11, "media");
                break;
            case ALTA:
                temperature = new Temperature(1, "alta");
                break;
        }

        BurnerPresenter burnerPresenter = new BurnerPresenter(getApplicationContext());
        currentBurner.setTemperature(temperature);
        burnerPresenter.updateBurner(currentBurner);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.set_time_button:
                showTimePicker();;
                break;
        }
    }

    private void showTimePicker(){
        TimePickerDialog timePicker;
        Calendar currentTime = Calendar.getInstance();
        final Button setTimeButton = findViewById(R.id.set_time_button);
        final TextView hourTextView = findViewById(R.id.hour_text_view);
        timePicker = new TimePickerDialog(OptionsActivity.this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
               hourTextView.setText(hourOfDay + ":" + minute);
               hourTextView.setVisibility(View.VISIBLE);
               setTimeButton.setVisibility(View.GONE);
            }
        }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), true);

        timePicker.show();
    }
}
