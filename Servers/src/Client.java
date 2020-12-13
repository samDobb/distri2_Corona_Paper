
import java.util.ArrayList;
import java.util.List;


public class Client {
    private String username;
    private String telephoneNumber;

    private List<String> pastTokens;
    private List<String> activeTokens;

    private List<byte[]> signatures;


    public Client(String username,String tel){
        this.username=username;
        this.telephoneNumber=tel;

        pastTokens=new ArrayList<>();
        activeTokens=new ArrayList<>();
        signatures=new ArrayList<>();
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

    public void setActiveTokens(List<String> tokens){
        activeTokens=tokens;
    }

    public void setSignatures(List<byte[]> signatures){this.signatures=signatures;}

    public void shiftActiveTokens(){
        for(String token:activeTokens){
            pastTokens.add(token);
        }
        activeTokens.clear();
    }


    public boolean hasToken(String token){
        for(String t: pastTokens){
            if(t.equals(token))return true;
        }
        return false;
    }

}
