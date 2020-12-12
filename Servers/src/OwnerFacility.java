import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

public class OwnerFacility {
    private String name;
    private String location;

    private String clientServiceName;
    private String clientAddres;

    private List<String> pseudonyms;

    OwnerFacility(String name,String location,String clientServiceName,String clientAddres){
        this.name=name;
        this.location=location;
        this.clientAddres=clientAddres;
        this.clientServiceName=clientServiceName;

        pseudonyms=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getClientServiceName() {
        return clientServiceName;
    }

    public void setClientServiceName(String clientServiceName) {
        this.clientServiceName = clientServiceName;
    }

    public String getClientAddres() {
        return clientAddres;
    }

    public void setClientAddres(String clientAddres) {
        this.clientAddres = clientAddres;
    }

    public List<String> getPseudonyms() {
        return pseudonyms;
    }

    public void setPseudonyms(List<String> pseudonyms) {
        this.pseudonyms = pseudonyms;
    }

    public String getPseu(int day){
        return pseudonyms.get(day);
    }
}
