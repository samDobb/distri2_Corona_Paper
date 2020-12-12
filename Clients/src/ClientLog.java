import java.util.Date;

public class ClientLog {
    private int random;
    private String facilityName;
    private byte[] token;
    private byte[] encodedLine;
    private Date entryTime;
    private Date stopTime;

    public ClientLog(int random,String name,byte[] token,byte[] line, Date entryTime,Date stopTime){
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

    public byte[] getEncodedLine() {
        return encodedLine;
    }

    public void setEncodedLine(byte[] encodedLine) {
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

    public byte[] getToken() {
        return token;
    }

    public void setToken(byte[] token) {
        this.token = token;
    }
}
