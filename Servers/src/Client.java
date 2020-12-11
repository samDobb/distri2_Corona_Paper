import java.util.ArrayList;
import java.util.List;

public class Client {
    private String username;
    private String telephoneNumber;

    private String clientServiceName;
    private String clientAddres;

    private List<String> pastTokens;
    private List<String> activeTokens;

    public Client(String username,String tel,String clientServiceName,String clientAddres){
        this.username=username;
        this.telephoneNumber=tel;

        pastTokens=new ArrayList<>();
        activeTokens=new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
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

    public void shiftActiveTokens(){
        for(String token:activeTokens){
            pastTokens.add(token);
        }
        activeTokens.clear();
    }

    public void setActiveTokens(List<String> tokens){
        activeTokens=tokens;
    }
}
