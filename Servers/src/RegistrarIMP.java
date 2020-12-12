import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.*;
import java.security.spec.KeySpec;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RegistrarIMP extends UnicastRemoteObject implements Registrar {

    private List<OwnerFacility> facilities;
    private List<Client> clients;

    private SecretKey masterSecretKey;
    private SecretKeyFactory factory;

    private MixingProxy mixingProxy;

    //constructor
    RegistrarIMP() throws RemoteException {
        super();
        facilities = new ArrayList<>();
        clients = new ArrayList<>();

        try {

            //setting the registrar in the registry so the clients can find it
            Naming.rebind("rmi://localhost/Registrar", this);
            System.out.println("Registrar Server is running");

            //generating the master secret key
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            masterSecretKey = keygen.generateKey();

            mixingProxy =(MixingProxy) Naming.lookup("rmi://localhost/MixingProxy");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Server had problems starting");
        }
    }


    //checking if the facility name is already taken
    @Override
    public boolean checkFacilityName(String name) {
        for (OwnerFacility c : facilities) {

            if (c.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    //adding a facility + generating the pseudonyms
    //details: name, service addres, service name
    @Override
    public void enrollFacility(String[] details) {
        OwnerFacility owner = new OwnerFacility(details[0], details[1], details[2], details[3]);
        facilities.add(owner);
    }


    //removing a facility
    @Override
    public void disconnectFacility(String name) {
        for (OwnerFacility c : facilities) {
            if (c.getName().equals(name)) {
                System.out.println(name + " left the registry \n");
                facilities.remove(c);
                break;
            }
        }
    }


    //generate the 48 pseudonyms for the facility
    @Override
    public List<byte[]> getPseudonyms(String name,String location){

        int monthMaxDays = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);

        List<byte[]> pseudonyms = new ArrayList<>(monthMaxDays);

        byte[] date = java.util.Calendar.getInstance().getTime().toString().getBytes();

        try {
            KeySpec spec = new PBEKeySpec(name.toCharArray(), date, 1000);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);

            String encodedLine = Base64.getEncoder().encodeToString(cipher.doFinal(masterSecretKey.getEncoded()));

            Calendar c = java.util.Calendar.getInstance();

            //making a pseudonym for every day of the current month
            for (int i = 0; i < monthMaxDays; i++) {
                c.add(Calendar.DATE, 1);

                String[] tmpDate = c.getTime().toString().split(" ");

                String day=tmpDate[2];
                String month=tmpDate[1];
                String year=tmpDate[5];

                MessageDigest md = MessageDigest.getInstance("SHA-256");

                String line = new String(encodedLine) + location + day + month + year;

                byte[] encodedLinePseu = md.digest(line.getBytes());

                pseudonyms.add(encodedLinePseu);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pseudonyms;
    }

    //adding a visitor
    @Override
    public void enrollNewUser(String[] details) {
        System.out.println("New client added: "+details[0]+" tel: "+ details[1]);
        Client client = new Client(details[0], details[1], details[2], details[3]);
        clients.add(client);
    }

    //checking if the username is taken
    @Override
    public boolean checkUserName(String username) {
        for (Client c : clients) {
            if (c.getUsername().equals(username)) return false;
        }
        return true;
    }

    //checking if the telephone number is taken
    @Override
    public boolean checkUserTel(String telephoneNumber) {
        for (Client c : clients) {
            if (c.getTelephoneNumber().equals(telephoneNumber)) return false;
        }
        return true;
    }

    @Override
    //generating the new 48 users token for the given client
    public GetTokenMessage generateUserToken(String username,PublicKey key) {

        List<String> actTokens = new ArrayList<>();
        List<byte[]> signatures = new ArrayList<>();

        RandomToken randomGen = new RandomToken();

        String date = java.util.Calendar.getInstance().getTime().toString();
        Client client=null;
        GetTokenMessage tokenMessage=null;
        for(Client c:clients){
            if(c.getUsername().equals(username))client=c;
        }

    //preparing for the signature
        try {
            //Creating KeyPair generator object
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");

            //Initializing the key pair generator
            keyPairGen.initialize(2048);

            //Generate the pair of keys
            KeyPair pair = keyPairGen.generateKeyPair();

            //Getting the privatekey from the key pair
            PrivateKey privKey = pair.getPrivate();
            //Creating a Signature object
            Signature sign = Signature.getInstance("SHA256withDSA");
            sign.initSign(privKey);

            //generates the 48 tokens
            //also checks if the token is already used
            for (int i = 0; i < 48; i++) {

                String newToken = date + " " + randomGen.nextString();

                //checking if token is the a duplicate
                if (!actTokens.contains(newToken)) {

                    sign.update(newToken.getBytes());
                    signatures.add(sign.sign());

                    actTokens.add(newToken);
                }
            }

            client.shiftActiveTokens();
            client.setActiveTokens(actTokens);
            client.setSignatures(signatures);

            tokenMessage=new GetTokenMessage(actTokens,signatures, pair.getPublic());

            mixingProxy.addPublicKey(pair.getPublic());

        }catch (Exception e) {
            e.printStackTrace();
        }
        return tokenMessage;
    }

    //getting the pseudonyms from all the facilities from a specific day
    public List<PseuLocMessage> sendPseudonyms(int day){
        List<PseuLocMessage> allPseudonyms=new ArrayList<>();

        for(OwnerFacility fac:facilities){
            PseuLocMessage mes=new PseuLocMessage(fac.getPseu(day),fac.getLocation());
            allPseudonyms.add(mes);
        }

        return allPseudonyms;
    }

    public void getCrits(List<Capsule> criticalEntries){
        List<String> uninformed=new ArrayList<>();
        for(Capsule capsule:criticalEntries) {
            for (Client c : clients) {
                if (c.hasToken(capsule.getToken()) && !uninformed.contains(c.getTelephoneNumber())){
                    uninformed.add(c.getTelephoneNumber());
                }
            }
        }
        for(String tel:uninformed){
            System.out.println("Uninformed: "+tel);
        }
    }

}
