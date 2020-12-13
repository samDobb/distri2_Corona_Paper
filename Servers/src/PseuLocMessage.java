import java.io.Serializable;
import java.util.List;

public class PseuLocMessage implements Serializable {
    private byte[] pseudonym;
    private String location;

   public PseuLocMessage(byte[] pseudonyms,String location){
    this.pseudonym=pseudonyms;
    this.location=location;
    }

    public byte[] getPseudonym() {
        return pseudonym;
    }

    public void setPseudonym(byte[] pseudonyms) {
        this.pseudonym = pseudonyms;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
