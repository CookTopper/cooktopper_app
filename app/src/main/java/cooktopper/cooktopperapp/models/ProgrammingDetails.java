package cooktopper.cooktopperapp.models;

public class ProgrammingDetails {

    private int id;
    private int programmedTime;
    private int expectedDuration;
    private Temperature temperature;
    private BurnerState burnerState;

    public ProgrammingDetails(int id, int programmedTime, int expectedDuration,
                              Temperature temperature, BurnerState burnerState){
        setId(id);
        setProgrammedTime(programmedTime);
        setExpectedDuration(expectedDuration);
        setTemperature(temperature);
        setBurnerState(burnerState);
    }

    public ProgrammingDetails(int programmedTime, int expectedDuration, Temperature temperature,
                              BurnerState burnerState){
        setProgrammedTime(programmedTime);
        setExpectedDuration(expectedDuration);
        setTemperature(temperature);
        setBurnerState(burnerState);
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getProgrammedTime(){
        return programmedTime;
    }

    public void setProgrammedTime(int programmedTime){
        this.programmedTime = programmedTime;
    }

    public int getExpectedDuration(){
        return expectedDuration;
    }

    public void setExpectedDuration(int expectedDuration){
        this.expectedDuration = expectedDuration;
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
