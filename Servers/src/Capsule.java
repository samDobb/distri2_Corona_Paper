import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Capsule {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private byte[] encodedLine;
    private String token;

    private boolean informed;

    Capsule(LocalDateTime startTime, LocalDateTime endTime, byte[] line, String token){
        this.startTime=startTime;
        this.endTime=endTime;
        this.encodedLine=line;
        this.token=token;

        this.informed=false;
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

    public boolean getInformed() {
        return informed;
    }
}
