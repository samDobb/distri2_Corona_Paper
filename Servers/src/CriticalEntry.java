import java.time.LocalDateTime;
import java.util.Date;

public class CriticalEntry {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private byte[] encodedLine;

    public CriticalEntry(LocalDateTime startTime, LocalDateTime endTime, byte[] line){
        this.startTime=startTime;
        this.endTime=endTime;
        this.encodedLine=line;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public byte[] getEncodedLine() {
        return encodedLine;
    }

    public void setEncodedLine(byte[] encodedLine) {
        this.encodedLine = encodedLine;
    }
}
