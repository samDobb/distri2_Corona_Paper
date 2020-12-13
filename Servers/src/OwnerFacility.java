import java.util.ArrayList;
import java.util.List;

public class OwnerFacility {
    private String name;
    private String location;
    private String telephoneNumber;

    private List<byte[]> pseudonyms;

    OwnerFacility(String name,String location, String telephoneNumber){
        this.name=name;
        this.location=location;
        this.telephoneNumber=telephoneNumber;

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

    public List<byte[]> getPseudonyms() {
        return pseudonyms;
    }

    public void setPseudonyms(List<byte[]> pseudonyms) {
        this.pseudonyms = pseudonyms;
    }

    public void setPseu(int day,byte[] p){
        pseudonyms.set(day,p);
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public byte[] getPseu(int day){
        return pseudonyms.get(day);
    }
}
