package cooktopper.cooktopperapp.models;

public class Stove {

    private int id;
    private String token;

    public Stove(int id, String token){
        setId(id);
        setToken(token);
    }

    public String getToken(){
        return token;
    }

    public void setToken(String token){
        this.token = token;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }
}
