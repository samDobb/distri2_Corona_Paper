import java.security.PublicKey;
import java.util.List;

public class GetTokenMessage {
    private List<String> tokens;
    private List<byte[]> signatures;
    private PublicKey publicKey;

    public GetTokenMessage(List<String> tokens,List<byte[]> signatures,PublicKey publicKey){
        this.tokens=tokens;
        this.signatures=signatures;
        this.publicKey=publicKey;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public List<byte[]> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<byte[]> signatures) {
        this.signatures = signatures;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
