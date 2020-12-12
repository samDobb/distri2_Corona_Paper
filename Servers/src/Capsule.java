import java.util.Date;

public class Capsule {
    private Date startTime;
    private Date endTime;
    private byte[] encodedLine;
    private String token;

    private boolean informed;

    Capsule(Date startTime, Date endTime, byte[] line, String token){
        this.startTime=startTime;
        this.endTime=endTime;
        this.encodedLine=line;
        this.token=token;

        this.informed=false;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public byte[] getEncodedLine() {
        return encodedLine;
    }

    public void setEncodedLine(byte[] encodedLine) {
        this.encodedLine = encodedLine;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isInformed() {
        return informed;
    }

    public void setInformed(boolean informed) {
        this.informed = informed;
    }
}
