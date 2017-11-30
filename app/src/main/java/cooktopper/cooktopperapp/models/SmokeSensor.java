package cooktopper.cooktopperapp.models;

public class SmokeSensor {

    int id;
    int smokeLevel;

    public SmokeSensor(int id, int smokeLevel){
        setId(id);
        setSmokeLevel(smokeLevel);
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getSmokeLevel(){
        return smokeLevel;
    }

    public void setSmokeLevel(int smokeLevel){
        this.smokeLevel = smokeLevel;
    }
}