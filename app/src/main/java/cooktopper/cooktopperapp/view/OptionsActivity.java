package cooktopper.cooktopperapp.view;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Calendar;

import cooktopper.cooktopperapp.R;
import cooktopper.cooktopperapp.models.Burner;
import cooktopper.cooktopperapp.models.BurnerState;
import cooktopper.cooktopperapp.models.Temperature;
import cooktopper.cooktopperapp.presenter.BurnerPresenter;

public class OptionsActivity extends AppCompatActivity implements
        MaterialSpinner.OnItemSelectedListener, View.OnClickListener {

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

        setCurrentBurner();

        Button setTimeButton = findViewById(R.id.set_time_to_turn_on_button);
        setTimeButton.setOnClickListener(this);

        Button setTimeToTurnOffButtonOnLayout = findViewById(R.id.set_time_to_turn_off_button);
        setTimeToTurnOffButtonOnLayout.setOnClickListener(this);

        Button confirmSchedulingButton = findViewById(R.id.confirm_scheduling_button);
        confirmSchedulingButton.setOnClickListener(this);

    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item){
        switch(view.getId()){
            case R.id.temperature:
                changeBurnerTemperature(position);
                break;
        }
    }

    private void changeBurnerTemperature(int temperatureId) {
        Temperature temperature = getTemperatureFromSpinner(temperatureId);
        if(temperature == null){
            Toast.makeText(this, "Por favor, selecione a temperatura a qual a boca deve ser ligada",
                    Toast.LENGTH_LONG).show();
        }
        else {
            BurnerPresenter burnerPresenter = new BurnerPresenter(getApplicationContext());
        }
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


    private void setCurrentBurner() {
        BurnerPresenter burnerPresenter = new BurnerPresenter(getApplicationContext());
        this.currentBurner = burnerPresenter.getBurnerFromExtrasFormatJson(getIntent().getExtras()
                .getString("burner_json"));
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

    private boolean isScheduleToTurnOnEmpty() {
        boolean isEmpty = false;
        TextView hourTextViewOn = findViewById(R.id.hour_to_turn_on_text_view);
        if(hourTextViewOn.getText().length() == 0 && temperatureFromSchedule == null) {
            isEmpty = true;
        }

        return isEmpty;
    }

    private boolean isScheduleToTurnOffEmpty() {
        boolean isEmpty = false;
        TextView hourTextViewOff = findViewById(R.id.hour_to_turn_off_text_view);
        if(hourTextViewOff.getText().length() == 0){
            isEmpty = true;
        }
        return isEmpty;
    }

    private void confirmScheduling(){
        TextView hourTextViewOn = findViewById(R.id.hour_to_turn_on_text_view);

        if(isScheduleToTurnOnEmpty() && isScheduleToTurnOffEmpty()){
            Toast.makeText(this, "Nenhuma informação de agendamento preenchida",
                    Toast.LENGTH_LONG).show();
        }
        else if(!isScheduleToTurnOnEmpty() && isScheduleToTurnOffEmpty()){
            if(hourTextViewOn.getText().length() == 0){
                Toast.makeText(this, "Por favor, selecione a hora a qual a boca deve " +
                        "ser ligada", Toast.LENGTH_LONG).show();
            }
            if(temperatureFromSchedule == null) {
                Toast.makeText(this, "Por favor, selecione a temperatura a qual a boca deve " +
                        "ser ligada", Toast.LENGTH_LONG).show();
            }
            else {
                confirmNoTimeToTurnOff();
            }
        }
        else if (!isScheduleToTurnOffEmpty() && isScheduleToTurnOnEmpty()){
            //agendar desligamento
        }
        else {
                if(hourTextViewOn.getText().length() == 0){
                    Toast.makeText(this, "Por favor, selecione a hora a qual a boca deve " +
                            "ser ligada", Toast.LENGTH_LONG).show();
                }
                if(temperatureFromSchedule == null) {
                    Toast.makeText(this, "Por favor, selecione a temperatura a qual a boca deve " +
                            "ser ligada", Toast.LENGTH_LONG).show();
                }
                else {
                    BurnerPresenter burnerPresenter = new BurnerPresenter(getApplicationContext());
                    changeBurnerState(ON);

                    TextView hourOnTextView = findViewById(R.id.hour_to_turn_on_text_view);
                    TextView hourOffTextView = findViewById(R.id.hour_to_turn_off_text_view);
                    String[] timeOn = hourOnTextView.getText().toString().split(":");
                    String[] timeOff = hourOffTextView.getText().toString().split(":");
                    burnerPresenter.scheduleBurnerOnAndOff(currentBurner,
                            Integer.parseInt(timeOn[0]),
                            Integer.parseInt(timeOn[1]),
                            Integer.parseInt(timeOff[0]),
                            Integer.parseInt(timeOff[1]));
                    Toast.makeText(getApplicationContext(), "Agendamento realizado com sucesso",
                            Toast.LENGTH_LONG).show();
                    finish();
                }
        }
    }

    private void changeBurnerState(int state) {
        BurnerState burnerState;
        if(state == ON){
            burnerState = new BurnerState(2, "Ligada");
        }
        else {
            burnerState = new BurnerState(1, "Desligada");
        }

        currentBurner.setBurnerState(burnerState);
    }

    private void confirmNoTimeToTurnOff(){
        final TextView hourTextView = findViewById(R.id.hour_to_turn_on_text_view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Atenção");
        builder.setMessage("Programar pra ligar às " + hourTextView.getText() + "h sem horário de " +
                "desligamento?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface arg0, int arg1){
                BurnerPresenter burnerPresenter = new BurnerPresenter(getApplicationContext());
                changeBurnerState(ON);
                String[] time = hourTextView.getText().toString().split(":");
                burnerPresenter.scheduleBurnerOnOrOff(currentBurner,
                        Integer.parseInt(time[0]),
                        Integer.parseInt(time[1]));
                Toast.makeText(getApplicationContext(),
                        "Agendamento realizado com sucesso", Toast.LENGTH_LONG).show();
                finish();
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
        timePicker = new TimePickerDialog(OptionsActivity.this, new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute){
                hourTextView.setText(hourOfDay + ":" + minute);
                hourTextView.setVisibility(View.VISIBLE);
                setTimeButton.setVisibility(View.GONE);
                hourTextViewHint.setVisibility(View.VISIBLE);
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
