import java.util.Date;

public class ClientLog {
    private int Ri;
    private String CF;
    private String token;
    private String hash;
    private Date entryTime;
    private Date stopTime;

    public ClientLog(int Ri,String CF,String hash,String token, Date entryTime,Date stopTime){
        this.Ri=Ri;
        this.CF=CF;
        this.token=token;
        this.hash=hash;
        this.entryTime=entryTime;
        this.stopTime=stopTime;
    }

    public int getRi() {
        return Ri;
    }

    public Date getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Date entryTime) {
        this.entryTime = entryTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public String getHash() {
        return hash;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
