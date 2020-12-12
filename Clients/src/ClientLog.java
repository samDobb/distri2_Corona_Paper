import java.util.Date;

public class ClientLog {
    private int random;
    private String facilityName;
    private String encodedLine;
    private Date entryTime;
    private Date stopTime;

    public ClientLog(int random,String  name,String line, Date entryTime,Date stopTime){
        this.random=random;
        this.facilityName=name;
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

    public Date getEntrytime() {
        return entryTime;
    }

    public void setEntrytime(Date entrytime) {
        this.entryTime = entrytime;
    }
}
