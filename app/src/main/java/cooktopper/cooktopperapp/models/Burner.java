package cooktopper.cooktopperapp.models;

public class Burner {

    private int id;
    private String description;
    private Stove stove;
    private Temperature temperature;
    private BurnerState burnerState;

    public Burner(int id, String description, Stove stove, Temperature temperature, BurnerState burnerState) {
        setId(id);
        setDescription(description);
        setStove(stove);
        setTemperature(temperature);
        setBurnerState(burnerState);
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public Stove getStove(){
        return stove;
    }

    public void setStove(Stove stove){
        this.stove = stove;
    }

    public Temperature getTemperature(){
        return temperature;
    }

    public void setTemperature(Temperature temperature){
        this.temperature = temperature;
    }

    public BurnerState getBurnerState(){
        return burnerState;
    }

    public void setBurnerState(BurnerState burnerState){
        this.burnerState = burnerState;
    }
}
