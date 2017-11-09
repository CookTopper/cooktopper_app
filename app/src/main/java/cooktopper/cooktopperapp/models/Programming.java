package cooktopper.cooktopperapp.models;

public class Programming {

    private int id;
    private BurnerState burnerState;
    private Temperature temperature;
    private int creationTime;
    private int expectedDuration;
    private int programmedTime;

    public Programming(int id, BurnerState burnerState, Temperature temperature, int creationTime,
                       int expectedDuration, int programmedTime){
        setId(id);
        setBurnerState(burnerState);
        setTemperature(temperature);
        setCreationTime(creationTime);
        setExpectedDuration(expectedDuration);
        setProgrammedTime(programmedTime);
    }

    public Programming(BurnerState burnerState, Temperature temperature, int creationTime,
                       int expectedDuration, int programmedTime){
        setBurnerState(burnerState);
        setTemperature(temperature);
        setCreationTime(creationTime);
        setExpectedDuration(expectedDuration);
        setProgrammedTime(programmedTime);
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public BurnerState getBurnerState(){
        return burnerState;
    }

    public void setBurnerState(BurnerState burnerState){
        this.burnerState = burnerState;
    }

    public Temperature getTemperature(){
        return temperature;
    }

    public void setTemperature(Temperature temperature){
        this.temperature = temperature;
    }

    public int getCreationTime(){
        return creationTime;
    }

    public void setCreationTime(int creationTime){
        this.creationTime = creationTime;
    }

    public int getExpectedDuration(){
        return expectedDuration;
    }

    public void setExpectedDuration(int expectedDuration){
        this.expectedDuration = expectedDuration;
    }

    public int getProgrammedTime(){
        return programmedTime;
    }

    public void setProgrammedTime(int programmedTime){
        this.programmedTime = programmedTime;
    }
}
