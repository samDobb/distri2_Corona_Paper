
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.List;


public class BarOwnerIMP extends UnicastRemoteObject implements BarOwner {

    private static final long serialVersionUID = 1L;

    private int bussinesNumber;
    private int telephoneNumber;

    private String bussinesName;
    private String bussinesAddres;

    private String clientServiceName;
    private String clientAddres;

    private Registrar registrar;

    private List<String> pseudonyms;

    private QRcode qrcode;


    //constructor
    public BarOwnerIMP(String bussinesName) throws RemoteException {
        super();
        this.bussinesName=bussinesName;
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

    //making connection to the registry and starting the rmi for this facility
    public boolean startClient(){

        clientServiceName="Facility_" + bussinesName;
        clientAddres="localhost";

        try{
            registrar = ( Registrar ) Naming.lookup("rmi://localhost/Registrar");

            //checking if name is not taken
            if(!registrar.checkFacilityName(bussinesName)) {

                //starting rmi for this facility
                Naming.rebind("rmi://"+clientAddres+"/"+clientServiceName, this);
                System.out.println(bussinesName+" is running\n");

                //registering this facility to the registrar
                String[] details = {bussinesName,bussinesAddres,clientServiceName,clientAddres};
                registrar.enrollFacility(details);

                return true;
            }
        }catch (Exception  e) {

            e.printStackTrace();
        }


        return false;
    }

    //disconnecting from the registrar
    public void disconnect(){

        try {
            registrar.disconnectFacility(bussinesName);

        } catch (RemoteException e) {
            System.out.println("facility disconnected");
            e.printStackTrace();
        }
    }

    //setting the pseudonyms that are to be used for the QR code
    @Override
    public void setPseudonyms(List<String> pseudonyms){
        this.pseudonyms=pseudonyms;
    }

    public void generateCurrentQR(int day){

        int random=(int)Math.random()*1000;

        try {
            //hashing the pseudonym with the random number
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            KeySpec spec = new PBEKeySpec(Integer.toString(random).toCharArray());
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);

            byte[] iv = cipher.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
            String encodedLine = Base64.getEncoder().encodeToString(cipher.doFinal(pseudonyms.get(day).getBytes()));

            //making the code
            qrcode = new QRcode(random,bussinesName,encodedLine);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
