import java.util.Date;

public class Capsule {
    private Date startTime;
    private Date endTime;
    private String encodedLine;
    private String token;

    Capsule(Date startTime, Date endTime, String line, String token){
        this.startTime=startTime;
        this.endTime=endTime;
        this.encodedLine=line;
        this.token=token;
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

    public String getEncodedLine() {
        return encodedLine;
    }

    public void setEncodedLine(String encodedLine) {
        this.encodedLine = encodedLine;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
