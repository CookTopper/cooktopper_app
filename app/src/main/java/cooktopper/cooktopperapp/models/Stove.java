package cooktopper.cooktopperapp.models;

public class Stove {

    private int id;
    private String token;
    private String mobileMacAddress;

    public Stove(int id, String token, String mobileMacAddress){
        setId(id);
        setToken(token);
        setMobileMacAddress(mobileMacAddress);
    }

    public String getToken(){
        return token;
    }

    private void setToken(String token){
        this.token = token;
    }

    public int getId(){
        return id;
    }

    private void setId(int id){
        this.id = id;
    }

    public String getMobileMacAddress() {
        return mobileMacAddress;
    }

    public void setMobileMacAddress(String mobileMacAddress) {
        this.mobileMacAddress = mobileMacAddress;
    }

}
