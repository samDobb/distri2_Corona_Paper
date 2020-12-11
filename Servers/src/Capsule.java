public class Capsule {
    private String time;
    private String encodedLine;
    private String token;

    Capsule(String time,String line, String token){
        this.time=time;
        this.encodedLine=line;
        this.token=token;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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
