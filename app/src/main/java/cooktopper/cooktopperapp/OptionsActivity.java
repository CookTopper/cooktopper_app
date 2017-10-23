package cooktopper.cooktopperapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.google.gson.Gson;
import com.jaredrummler.materialspinner.MaterialSpinner;

import cooktopper.cooktopperapp.models.Burner;
import cooktopper.cooktopperapp.models.BurnerState;
import cooktopper.cooktopperapp.presenter.BurnerPresenter;

public class OptionsActivity extends AppCompatActivity implements
        MaterialSpinner.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private Burner currentBurner;
    private final int ON = 1;
    private final int OFF = 0;

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
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item){

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
        this.currentBurner = null;
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
            burnerState = new BurnerState(1, "Ligada");
        }
        else{
            burnerState = new BurnerState(0, "Desligada");
        }
        BurnerPresenter burnerPresenter = new BurnerPresenter(getApplicationContext());
        currentBurner.setBurnerState(burnerState);
        burnerPresenter.updateBurner(currentBurner);
    }

}
