import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;

public class RegistrarIMP extends UnicastRemoteObject implements Registrar{

    private List<OwnerFacility> facilities;
    private List<SecretKey> secretKeys;
    private List<Visitor> users;

    private SecretKey masterSecretKey;
    private SecretKeyFactory factory;

    //constructor
    RegistrarIMP() throws RemoteException{
        super();
        facilities=new ArrayList<>();
        secretKeys=new ArrayList<>();
        users=new ArrayList<>();

        try{
            //creating rmi registry
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            System.out.println("Registrar Server ready");

            //setting the registrar in the registry so the clients can find it
            Naming.rebind("rmi://localhost/Registrar", this);
            System.out.println("Registrar Server is running");

            //generating the master secret key
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            masterSecretKey = keygen.generateKey();
        }
        catch(Exception e){
            System.out.println("Server had problems starting");
        }
    }

    public void generateQRcode(){

    }

    //checking if the facility name is already taken
    public boolean checkFacilityName(String name){
        for(OwnerFacility c : facilities){

            if(c.getName().equals(name)){
               return false;
            }
        }
        return true;
    }

    //adding a facility + generating the specialized secret key
    //details: name, service addres, service name
    public void enrollFacility(String[] details){
        facilities.add(new OwnerFacility(details[0],details[1],details[2]));
        generateSecretKey(details[0]);

    }

    public void generateSecretKey(String name){

        byte[] date=java.util.Calendar.getInstance().getTime().toString().getBytes();

        try {
            KeySpec spec = new PBEKeySpec(name.toCharArray(),date,1000);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);

            byte[] ciphertext = cipher.doFinal(masterSecretKey.getEncoded());

        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
    }

    //removing a facility
    public void disconnectFacility(String name) {

        for(OwnerFacility c : facilities){
            if(c.getName().equals(name)){
                System.out.println(name + " left the registry \n");
                facilities.remove(c);
                break;
            }
        }
    }

    //adding a visitor
    public String enrollNewUser(String[] details){
    return "";
    }


}
