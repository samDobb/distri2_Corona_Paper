import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

public class Doctor {

    private List<ClientLog> logs;

    private MatchingService matchingService;

    //constructor
    public Doctor() throws RemoteException, NotBoundException, MalformedURLException {
        logs=new ArrayList<>();

        matchingService = (MatchingService) Naming.lookup("rmi://localhost/MatchingService");
    }

    //getting the logs from the sick patient
    public void getLogs(List<ClientLog> logs){
        this.logs=logs;
    }

    //sending the logs from the sick patient to the server
    public boolean sendLogs() {
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

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(logs);
            byte[] bytes = bos.toByteArray();

            sign.update(bytes);
            byte[] signature = sign.sign();


            if(matchingService.getCriticalLogs(logs,signature, pair.getPublic())){
                logs.clear();
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public int getLogsSize(){
        return logs.size();
    }

    public void setLogs(List<ClientLog> logs){
        this.logs=logs;
    }

    public void addLogs(List<ClientLog> logs){
        this.logs.addAll(logs);
    }

}
