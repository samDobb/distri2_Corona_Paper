import java.util.Date;

public class CriticalEntry {

    private Date startTime;
    private Date endTime;
    private byte[] encodedLine;

    public CriticalEntry(Date startTime, Date endTime, byte[] line){
        this.startTime=startTime;
        this.endTime=endTime;
        this.encodedLine=line;
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
}
