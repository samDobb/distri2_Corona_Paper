
public class Client {
    private String username;
    private String telephoneNumber;


    private String clientServiceName;
    private String clientAddres;

    public Client(String username,String tel,String clientServiceName,String clientAddres){
        this.username=username;
        this.telephoneNumber=tel;
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
}
