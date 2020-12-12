import java.util.Date;

public class ClientLog {
    private int random;
    private String facilityName;
    private String token;
    private String encodedLine;
    private Date entryTime;
    private Date stopTime;

    public ClientLog(int random,String name,String token,String line, Date entryTime,Date stopTime){
        this.random=random;
        this.facilityName=name;
        this.token=token;
        this.encodedLine=line;
        this.entryTime=entryTime;
        this.stopTime=stopTime;
    }

    public int getRandom() {
        return random;
    }

    public void setRandom(int random) {
        this.random = random;
    }

    public String getFacilityName() {
        return facilityName;
    }

    public void setFacilityName(String facilityName) {
        this.facilityName = facilityName;
    }

    public String getEncodedLine() {
        return encodedLine;
    }

    public void setEncodedLine(String encodedLine) {
        this.encodedLine = encodedLine;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
