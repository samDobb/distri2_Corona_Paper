

public class OwnerFacility {

    private String name;
    private String clientServiceName;
    private String clientAdress;

    //constructor
    public OwnerFacility(String name, String clientAdress,String clientServiceName){
        this.name = name;
        this.clientServiceName = clientServiceName;
        this.clientAdress=clientAdress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientServiceName() {
        return clientServiceName;
    }

    public void setClientServiceName(String clientServiceName) {
        this.clientServiceName = clientServiceName;
    }

    public String getClientAdress() {
        return clientAdress;
    }

    public void setClientAdress(String clientAdress) {
        this.clientAdress = clientAdress;
    }
}