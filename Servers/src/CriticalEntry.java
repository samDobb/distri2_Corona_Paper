import java.time.LocalDateTime;
import java.util.Date;

public class CriticalEntry {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String hash;

    public CriticalEntry(LocalDateTime startTime, LocalDateTime endTime,String hash){
        this.startTime=startTime;
        this.endTime=endTime;
        this.hash=hash;
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

    public String getHash() {
        return this.hash;
    }

}
