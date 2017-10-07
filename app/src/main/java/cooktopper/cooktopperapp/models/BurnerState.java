package cooktopper.cooktopperapp.models;

public class BurnerState {

    private int id;
    private String description;

    public BurnerState(int id, String description){
        setId(id);
        setDescription(description);
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

}
