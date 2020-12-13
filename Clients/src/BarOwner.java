
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;


public class BarOwner{

    private static final long serialVersionUID = 1L;

    private String bussinesNumber;
    private String telephoneNumber;

    private String bussinesName;
    private String bussinesAddres;

    private String CF;

    private Registrar registrar;
    private MixingProxy mixingProxy;

    private byte[] pseudonym;
    private int Ri;

    private QRcode qrcode;


    //constructor
    public BarOwner(String bussinesName) {
        char[] digits = "0123456789".toCharArray();
        char[] buf=new char[10];
        buf[0]='B';buf[1]='E';
        for (int i = 2; i < 10;i++){
            int randomInd=(int) (Math.random() *(digits.length));
            buf[i] = digits[randomInd];
        }
        String number=new String(buf);
        this.bussinesNumber=number;
        this.bussinesName = bussinesName;
        this.CF=bussinesName+number;
    }

    public String getBussinesNumber() {
        return bussinesNumber;
    }

    public void setBussinesNumber(String bussinesNumber) {
        this.bussinesNumber = bussinesNumber;
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
    public void connect() throws RemoteException, NotBoundException, MalformedURLException {
        registrar = (Registrar) Naming.lookup("rmi://localhost/Registrar");
        mixingProxy = (MixingProxy) Naming.lookup("rmi://localhost/MixingProxy");
        //get this months pseudonyms
        pseudonym=registrar.getPseudonyms(bussinesName,bussinesNumber);

    }
    public void generateRi(){
        SecureRandom rand= new SecureRandom();
        Ri= rand.nextInt(9000);
        System.out.println("Todays Ri: " +Ri);
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

    public void getPseudonym() throws RemoteException {
        pseudonym=registrar.getPseudonyms(bussinesName,bussinesAddres);
    }

    public void generateCurrentQR() {
        this.generateRi();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            String line = new String(pseudonym, StandardCharsets.ISO_8859_1);
            line=Ri+line;
            byte[] encodedLine = md.digest(line.getBytes(StandardCharsets.ISO_8859_1));
            //making the code
            qrcode = new QRcode(Ri, CF, encodedLine);
            System.out.println("Showing QR:");
            System.out.println(qrcode.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public QRcode getQrcode(){
        return this.qrcode;
    }

    @Override
    public String toString() {
        return "BarOwner{" +
                "CF='" + CF + '\'' +
                '}';
    }
}
