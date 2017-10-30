package cooktopper.cooktopperapp.models;

public class ProgrammingDetails {

    private int id;
    private int programmedTime;
    private int expectedDuration;
    private ProgrammingType programmingType;
    private Temperature temperature;

    public ProgrammingDetails(int id, int programmedTime, int expectedDuration,
                              ProgrammingType programmingType, Temperature temperature){
        setId(id);
        setProgrammedTime(programmedTime);
        setExpectedDuration(expectedDuration);
        setProgrammingType(programmingType);
        setTemperature(temperature);
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

    public ProgrammingType getProgrammingType(){
        return programmingType;
    }

    public void setProgrammingType(ProgrammingType programmingType){
        this.programmingType = programmingType;
    }

    public Temperature getTemperature(){
        return temperature;
    }

    public void setTemperature(Temperature temperature){
        this.temperature = temperature;
    }
}
