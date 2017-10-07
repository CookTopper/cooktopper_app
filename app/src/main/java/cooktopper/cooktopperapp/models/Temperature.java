package cooktopper.cooktopperapp.models;

public class Temperature {

    private int id;
    private String description;

    public Temperature(int id, String description){
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
