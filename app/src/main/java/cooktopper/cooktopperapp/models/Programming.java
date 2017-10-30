package cooktopper.cooktopperapp.models;

public class Programming {

    private Burner burner;
    private ProgrammingType programmingType;

    public Programming(Burner burner, ProgrammingType programmingType){
        setBurner(burner);
        setProgrammingType(programmingType);
    }

    public ProgrammingType getProgrammingType(){
        return programmingType;
    }

    public void setProgrammingType(ProgrammingType programmingType){
        this.programmingType = programmingType;
    }

    public Burner getBurner(){
        return burner;
    }

    public void setBurner(Burner burner){
        this.burner = burner;
    }
}
