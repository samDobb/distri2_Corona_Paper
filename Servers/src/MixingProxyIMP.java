import javax.security.auth.callback.TextInputCallback;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class MixingProxyIMP  extends UnicastRemoteObject implements MixingProxy {

    private List<Capsule> capsules;
    private PrivateKey privKey;

    private MatchingService matchingService;

    private List<PublicKey> publicKeys;

    public MixingProxyIMP() throws RemoteException {
        super();

        capsules=new ArrayList<>();
        publicKeys = new ArrayList<>();

        try{

            //creating rmi registry
            java.rmi.registry.LocateRegistry.createRegistry(1101);
            System.out.println("MixingProxy Server ready");

            //setting the registrar in the registry so the clients can find it
            Naming.rebind("rmi://localhost/MixingProxy", this);
            System.out.println("MixingProxy Server is running");

            //Creating KeyPair generator object
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");

            //Initializing the key pair generator
            keyPairGen.initialize(2048);

            //Generate the pair of keys
            KeyPair pair = keyPairGen.generateKeyPair();

            //Getting the privatekey from the key pair
            privKey = pair.getPrivate();

            matchingService = (MatchingService) Naming.lookup("rmi://localhost/MatchingService");

        }
        catch(Exception e){
            System.out.println("MixingProxy Server had problems starting");
        }
    }

    @Override
   public byte[] sendCapsule(Date startTime, Date endTime, String token, byte[] signature, String encodedLine, PublicKey publicKey){
        try {
            //Creating a Signature object
            Signature sign = Signature.getInstance("SHA256withDSA");
            sign.initVerify(publicKey);
            sign.update(token.getBytes());

            //verifing if the token is valid
            if(publicKeys.contains(publicKey) && sign.verify(signature)) {

                //verifing if the token is from the current day
                if (checkToken(token)) {

                    capsules.add(new Capsule(startTime,endTime, encodedLine, token));

                    //creating the signature that will be send back
                    Signature signBack = Signature.getInstance("SHA256withDSA");

                    signBack.initSign(privKey);

                    //updating the signature with the given encoded line from the facility
                    signBack.update(Base64.getDecoder().decode(encodedLine));

                    return signBack.sign();

                } else return null;

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    //check if the token that is send is of today
    public boolean checkToken(String token){
        String[] splitToken = token.split(" ");

        String currentDate= java.util.Calendar.getInstance().getTime().toString();
        String[] splitDate=currentDate.split(" ");

        if(splitDate[2].equals(splitToken[2])){
            System.out.println("TokenCheck: different time zones ");
        }
        //if the day name, day number, month number and year numbers are equal then the token is from today
        if(splitDate[0].equals(splitToken[0]) && splitDate[1].equals(splitToken[1]) && splitDate[2].equals(splitToken[2]) && splitDate[5].equals(splitToken[5])){
            return true;
        }
        else return false;
    }

    public void addPublicKey(PublicKey key){
        publicKeys.add(key);
    }

    public List<Capsule> sendCriticalLogs() throws RemoteException {
        return matchingService.sendCriticalLogs();
    }
}
