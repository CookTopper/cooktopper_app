package cooktopper.cooktopperapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jaredrummler.materialspinner.MaterialSpinner;

public class OptionsActivity extends AppCompatActivity implements
        MaterialSpinner.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.temperature);
        spinner.setItems("Baixa", "Média", "Alta");
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(MaterialSpinner view, int position, long id, Object item){

    }
}
