package cooktopper.cooktopperapp.models;

public class Programming {

    private Burner burner;
    private ProgrammingDetails programmingDetails;

    public Programming(Burner burner, ProgrammingDetails programmingDetails){
        setBurner(burner);
        setProgrammingDetails(programmingDetails);
    }

    public ProgrammingDetails getProgrammingDetails(){
        return programmingDetails;
    }

    public void setProgrammingDetails(ProgrammingDetails programmingDetails){
        this.programmingDetails = programmingDetails;
    }

    public Burner getBurner(){
        return burner;
    }

    public void setBurner(Burner burner){
        this.burner = burner;
    }
}
