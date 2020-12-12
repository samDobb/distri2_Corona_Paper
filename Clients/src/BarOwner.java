
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;


public class BarOwner{

    private static final long serialVersionUID = 1L;

    private int bussinesNumber;
    private int telephoneNumber;

    private String bussinesName;
    private String bussinesAddres;

    private Registrar registrar;

    private List<byte[]> pseudonyms;

    private QRcode qrcode;


    //constructor
    public BarOwner(String bussinesName) throws RemoteException {
        super();
        this.bussinesName = bussinesName;
    }

    public int getBussinesNumber() {
        return bussinesNumber;
    }

    public void setBussinesNumber(int bussinesNumber) {
        this.bussinesNumber = bussinesNumber;
    }

    public int getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(int telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getBussinesName() {
        return bussinesName;
    }

    public void setBussinesName(String bussinesName) {
        this.bussinesName = bussinesName;
    }

    public String getBussinesAddres() {
        return bussinesAddres;
    }

    public void setBussinesAddres(String bussisnesAddres) {
        this.bussinesAddres = bussisnesAddres;
    }


    //disconnecting from the registrar
    public void disconnect() {
        try {
            registrar.disconnectFacility(bussinesName);

        } catch (RemoteException e) {
            System.out.println("facility disconnected");
            e.printStackTrace();
        }
    }

    public void getPseudonyms() throws RemoteException {
        pseudonyms=registrar.getPseudonyms(bussinesName,bussinesAddres);
    }

    public void generateCurrentQR(int day) {

        int random = (int) Math.random() * 1000;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            String line = new String(pseudonyms.get(day)) + random + bussinesAddres;

            byte[] encodedLine = md.digest(line.getBytes());

            //making the code
            qrcode = new QRcode(random, bussinesName, new String(encodedLine));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
