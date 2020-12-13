import javax.security.auth.callback.TextInputCallback;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.time.LocalDateTime;
import java.util.*;

public class MixingProxyIMP  extends UnicastRemoteObject implements MixingProxy {

    private List<Capsule> capsules;
    private PrivateKey privKey;
    private PrivateKey RSAPrivKey;

    private MatchingService matchingService;

    private List<PublicKey> publicKeys;

    public MixingProxyIMP() throws RemoteException {
        super();

        capsules=new ArrayList<>();
        publicKeys = new ArrayList<>();

        try{

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

            KeyPairGenerator keyPairGenRSA = KeyPairGenerator.getInstance("RSA");
            keyPairGenRSA.initialize(2048,new SecureRandom());
            KeyPair pairRSA = keyPairGenRSA.generateKeyPair();
            RSAPrivKey=pairRSA.getPrivate();

            matchingService = (MatchingService) Naming.lookup("rmi://localhost/MatchingService");

        }
        catch(Exception e){
            System.out.println("MixingProxy Server had problems starting");
        }
    }

    @Override
   public byte[] sendCapsule(LocalDateTime startTime, LocalDateTime endTime, String token, byte[] signature, byte[] encodedLine, PublicKey publicKey){
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
                    Signature signBack = Signature.getInstance("SHA256withRSA");

                    signBack.initSign(RSAPrivKey);

                    //updating the signature with the given encoded line from the facility
                    signBack.update(encodedLine);

                    return signBack.sign();

                } else return null;

            } else return null;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    //check if the token that is send is of today and not yet used
    public boolean checkToken(String token){
        String[] splitToken = token.split(" ");

        String currentDate= java.util.Calendar.getInstance().getTime().toString();
        String[] splitDate=currentDate.split(" ");
        //if the day name, day number, month number and year numbers are equal then the token is from today
        if(splitDate[0].equals(splitToken[0]) && splitDate[1].equals(splitToken[1]) && splitDate[2].equals(splitToken[2]) && splitDate[5].equals(splitToken[5])){
            return true;
        }
        else return false;
    }

    //adding a public key
    public void addPublicKey(PublicKey key){
        publicKeys.add(key);
    }

    //removing a public key
    public void removePublicKey(PublicKey key){
       if(key!=null)publicKeys.remove(key);
    }

    //returning the critical logs
    public List<CriticalEntry> sendCriticalLogs() throws RemoteException {
        return matchingService.sendCriticalLogs();
    }

    //setting all the given tokens on informed
    public void getInfectedTokens(List<String> infectedTokens) throws RemoteException {
        for(Capsule capsule:capsules) {
            for(String token:infectedTokens)
            if(capsule.getToken().equals(token)){
                capsule.setInformed(true);
            }
        }
        matchingService.getInfectedTokens(infectedTokens);
    }

    //flushes the current capsules to the matching service
    public void flush() throws RemoteException {
        Collections.shuffle(capsules);
        matchingService.addCapsules(capsules);
        capsules.clear();
    }


}
