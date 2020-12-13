import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class QRcode {
    private int randomNumber;
    private String CF;
    private byte[] encodedLine;

    public QRcode(int random,String CF,byte[] encodedLine){
    this.randomNumber=random;
    this.CF=CF;
    this.encodedLine=encodedLine;
    }

    public byte[] getEncodedLine() {
        return encodedLine;
    }

    public void setEncodedLine(byte[] encodedLine) {
        this.encodedLine = encodedLine;
    }

    public int getRandomNumber() {
        return randomNumber;
    }

    public String getCF() {
        return CF;
    }

    @Override
    public String toString() {
        String enc= new String(encodedLine, StandardCharsets.ISO_8859_1);
        return randomNumber+"."+CF+"."+enc;
    }
}
