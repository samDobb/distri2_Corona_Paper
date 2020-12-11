import javax.imageio.spi.RegisterableService;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class VisitorIMP extends UnicastRemoteObject implements Visitor{
    private String username;
    private String telephoneNumber;

    private String clientServiceName;
    private String clientAddres;

    private Registrar registrar;

    private List<String> tokens;

    public VisitorIMP(String username,String telephoneNumber) throws RemoteException {
        super();
        this.username=username;
        this.telephoneNumber=telephoneNumber;
    }

    //making connection to the registry and starting the rmi for this facility
    public boolean startClient(){

        clientServiceName="Visitor_" + username;
        clientAddres="localhost";

        try{
            registrar = ( Registrar ) Naming.lookup("rmi://localhost/Registrar");

            //checking if name is not taken
            if(!registrar.checkUserName(username) && !registrar.checkUserTel(telephoneNumber)) {

                //starting rmi for this facility
                Naming.rebind("rmi://"+clientAddres+"/"+clientServiceName, this);
                System.out.println(username+" is running\n");

                //registering this facility to the registrar
                String[] details = {username,telephoneNumber,clientAddres,clientServiceName};
                registrar.enrollNewUser(details);

                return true;
            }
        }catch (Exception  e) {

            e.printStackTrace();
        }

        return false;
    }

    @Override
    public void setTokens(List<String> tokens){
    this.tokens=tokens;
    }
}
