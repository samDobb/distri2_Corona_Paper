import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


public class ClientLog implements Serializable{

    private static final long serialVersionUID=2L;

    private int Ri;
    private String CF;
    private String token;
    private String hash;
    private LocalDateTime entryTime;
    private LocalDateTime  stopTime;

    public ClientLog(int Ri,String CF,String hash,String token, LocalDateTime  entryTime,LocalDateTime  stopTime){
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

    public void setRi(int ri) {
        Ri = ri;
    }

    public String getCF() {
        return CF;
    }

    public void setCF(String CF) {
        this.CF = CF;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public LocalDateTime  getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(LocalDateTime  entryTime) {
        this.entryTime = entryTime;
    }

    public LocalDateTime  getStopTime() {
        return stopTime;
    }

    public void setStopTime(LocalDateTime  stopTime) {
        this.stopTime = stopTime;
    }

    @Override
    public String toString() {
       return Ri+"/"+CF+"/"+token+"/"+hash+"/"+entryTime.toEpochSecond( ZoneOffset.UTC)+"/"+stopTime.toEpochSecond( ZoneOffset.UTC);
    }
}
