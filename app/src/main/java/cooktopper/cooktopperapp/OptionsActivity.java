package cooktopper.cooktopperapp;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Calendar;

import cooktopper.cooktopperapp.models.Burner;
import cooktopper.cooktopperapp.models.BurnerState;
import cooktopper.cooktopperapp.models.ProgrammingDetails;
import cooktopper.cooktopperapp.models.ProgrammingType;
import cooktopper.cooktopperapp.models.Temperature;
import cooktopper.cooktopperapp.presenter.BurnerPresenter;

public class OptionsActivity extends AppCompatActivity implements
        MaterialSpinner.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener,
        View.OnClickListener {

    private Burner currentBurner;
    private final int ON = 2;
    private final int OFF = 1;
    private Temperature temperatureFromSchedule;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MaterialSpinner spinnerToTurnOn = findViewById(R.id.temperature);
        spinnerToTurnOn.setItems("Selecionar temperatura", "Baixa", "Média", "Alta");
        spinnerToTurnOn.setOnItemSelectedListener(this);

        MaterialSpinner spinnerToTurnOff = findViewById(R.id.schedule_temperature);
        spinnerToTurnOff.setItems("Selecionar temperatura", "Baixa", "Média", "Alta");
        spinnerToTurnOff.setOnItemSelectedListener(this);

        setCurrentBurner();

        Switch burnerState = findViewById(R.id.burner_state);
        burnerState.setOnCheckedChangeListener(this);
        setSwitchInitialState(burnerState);

        Button setTimeButton = findViewById(R.id.set_time_to_turn_on_button);
        setTimeButton.setOnClickListener(this);

        Button setTimeToTurnOffButton = findViewById(R.id.set_time_to_turn_off_button);
        setTimeToTurnOffButton.setOnClickListener(this);

        Button confirmSchedulingButton = findViewById(R.id.confirm_scheduling_button);
        confirmSchedulingButton.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item){
        switch(view.getId()){
            case R.id.temperature:
                changeBurnerTemperature(position);
                break;
            case R.id.schedule_temperature:
                setScheduleTemperature(position);
                break;
        }
    }

    private void setScheduleTemperature(int temperatureId) {
        temperatureFromSchedule = getTemperatureFromSpinner(temperatureId);
    }

    private Temperature getTemperatureFromSpinner(int temperatureId) {
        final int NAO_SELECIONADO = 0, BAIXA = 1, MEDIA = 2, ALTA = 3;
        Temperature temperature = null;
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

        return temperature;
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
        Temperature temperature = getTemperatureFromSpinner(temperatureId);

        BurnerPresenter burnerPresenter = new BurnerPresenter(getApplicationContext());
        currentBurner.setTemperature(temperature);
        burnerPresenter.updateBurner(currentBurner);
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.set_time_to_turn_on_button:
                showTimePickerToTurnOn();;
                break;
            case R.id.set_time_to_turn_off_button:
                showTimePickerToTurnOff();
                break;
            case R.id.confirm_scheduling_button:
                confirmScheduling();
                break;
        }
    }

    private void confirmScheduling(){
        TextView hourTextViewOff = findViewById(R.id.hour_to_turn_off_text_view);
        TextView hourTextViewOn = findViewById(R.id.hour_to_turn_on_text_view);
        if(hourTextViewOn.getText().length() == 0 && temperatureFromSchedule == null){
            Toast.makeText(this, "Por favor, selecione a hora e a temperatura a qual a boca deve " +
                            "ser ligada", Toast.LENGTH_LONG).show();
        }
        else if(hourTextViewOn.getText().length() == 0){
            Toast.makeText(this, "Por favor, selecione a hora a qual a boca deve " +
                    "ser ligada", Toast.LENGTH_LONG).show();
        }
        else if(temperatureFromSchedule == null) {
                Toast.makeText(this, "Por favor, selecione a temperatura a qual a boca deve " +
                        "ser ligada", Toast.LENGTH_LONG).show();
        }
        else if(hourTextViewOff.getText().length() == 0){
            confirmNoTimeToTurnOff();
        }
        else{
            //Program with schedule to turn off
        }
    }

    private void confirmNoTimeToTurnOff(){
        TextView hourTextView = findViewById(R.id.hour_to_turn_on_text_view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage("Programar pra ligar às " + hourTextView.getText() + "h sem horário de " +
                "desligamento?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1){
                //Program without schedule to turn off
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1){
                arg0.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showTimePickerToTurnOn(){
        TimePickerDialog timePicker;
        Calendar currentTime = Calendar.getInstance();
        final Button setTimeButton = findViewById(R.id.set_time_to_turn_on_button);
        final TextView hourTextView = findViewById(R.id.hour_to_turn_on_text_view);
        final TextView hourTextViewHint = findViewById(R.id.change_hour_hint);
        final LinearLayout scheduleBurnerOffLayout = findViewById(R.id.schedule_burner_off_layout);
        timePicker = new TimePickerDialog(OptionsActivity.this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
               hourTextView.setText(hourOfDay + ":" + minute);
               hourTextView.setVisibility(View.VISIBLE);
               setTimeButton.setVisibility(View.GONE);
               hourTextViewHint.setVisibility(View.VISIBLE);
               scheduleBurnerOffLayout.setVisibility(View.VISIBLE);
            }
        }, currentTime.get(Calendar.HOUR_OF_DAY), currentTime.get(Calendar.MINUTE), true);

        timePicker.show();
    }

    private void showTimePickerToTurnOff(){
        TimePickerDialog timePicker;
        Calendar currentTime = Calendar.getInstance();
        final Button setTimeButton = findViewById(R.id.set_time_to_turn_off_button);
        final TextView hourTextView = findViewById(R.id.hour_to_turn_off_text_view);
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
