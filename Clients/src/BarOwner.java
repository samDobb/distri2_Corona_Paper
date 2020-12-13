
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class BarOwner{

    private static final long serialVersionUID = 1L;

    private String bussinesNumber;

    private String bussinesName;
    private String bussinesAddres;

    private String CF;

    private Registrar registrar;
    private MixingProxy mixingProxy;

    private List<byte[]> pseudonyms;
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

        pseudonyms=new ArrayList<>();
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

    //connecting to servers
    public void connect() throws RemoteException, NotBoundException, MalformedURLException {
        registrar = (Registrar) Naming.lookup("rmi://localhost/Registrar");
        mixingProxy = (MixingProxy) Naming.lookup("rmi://localhost/MixingProxy");

        String[] details = {bussinesName,bussinesAddres,bussinesNumber};
        registrar.enrollFacility(details);

        //get this months pseudonyms
        pseudonyms=registrar.getPseudonyms(bussinesName,bussinesNumber);

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

    public void generateCurrentQR() {
        this.generateRi();

        LocalDateTime today=LocalDateTime.now();
        int day=today.getDayOfMonth();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String line = new String(pseudonyms.get(day), StandardCharsets.ISO_8859_1);
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
