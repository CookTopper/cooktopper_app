package cooktopper.cooktopperapp.models;

public class Shortcut {

    private int id;
    private Programming programming;
    private String description;

    public Shortcut(String description, Programming programming){
        setDescription(description);
        setProgramming(programming);
    }

    public Shortcut(int id, String description, Programming programming){
        setId(id);
        setDescription(description);
        setProgramming(programming);
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

    public Programming getProgramming(){
        return programming;
    }

    public void setProgramming(Programming programming){
        this.programming = programming;
    }
}
