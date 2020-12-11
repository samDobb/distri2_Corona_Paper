import javax.security.auth.callback.TextInputCallback;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

public class MixingProxyIMP  extends UnicastRemoteObject implements MixingProxy {

    private List<Capsule> capsules;
    private List<String> usedTokens;
    public MixingProxyIMP() throws RemoteException {
        super();

        capsules=new ArrayList<>();
        usedTokens=new ArrayList<>();

        try{
            //creating rmi registry
            java.rmi.registry.LocateRegistry.createRegistry(1101);
            System.out.println("MixingProxy Server ready");

            //setting the registrar in the registry so the clients can find it
            Naming.rebind("rmi://localhost/MixingProxy", this);
            System.out.println("MixingProxy Server is running");

        }
        catch(Exception e){
            System.out.println("MixingProxy Server had problems starting");
        }
    }

    @Override
   public boolean sendCapsule(String time, String token, byte[] signature, String encodedLine, PublicKey publicKey){
        try {
            //Creating a Signature object
            Signature sign = Signature.getInstance("SHA256withDSA");
            sign.initVerify(publicKey);
            sign.update(token.getBytes());

            //verifing the token is valid
            if(sign.verify(signature)) {

                //verifing if the token is from the current day and if it is not already used
                if (checkToken(token) && !usedTokens.contains(token)) {
                    usedTokens.add(token);
                    capsules.add(new Capsule(time, encodedLine, token));
                    return true;
                } else return false;

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return false;
    }

    //check if the token that is send is of today
    public boolean checkToken(String token){
        String[] splitToken = token.split(" ");

        String currentDate= java.util.Calendar.getInstance().getTime().toString();
        String[] splitDate=currentDate.split(" ");

        if(splitDate[2].equals(splitToken[2])){
            System.out.println("TokenCheck: different time zones ");
        }
        //if the day name, day number, month number and year numbers are equal then ok
        if(splitDate[0].equals(splitToken[0]) && splitDate[1].equals(splitToken[1]) && splitDate[2].equals(splitToken[2]) && splitDate[5].equals(splitToken[5])){
            return true;
        }
        else return false;
    }
}
