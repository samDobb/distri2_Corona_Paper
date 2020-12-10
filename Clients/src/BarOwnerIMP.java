
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



public class BarOwnerIMP extends UnicastRemoteObject implements BarOwner {

    private static final long serialVersionUID = 1L;

    private int bussinesNumber;
    private int telephoneNumber;

    private String bussinesName;
    private String bussisnesAddres;

    private String clientServiceName;
    private String clientAddres;

    private Registrar registrar;


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

    public String getBussisnesAddres() {
        return bussisnesAddres;
    }

    public void setBussisnesAddres(String bussisnesAddres) {
        this.bussisnesAddres = bussisnesAddres;
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
                String[] details = {bussinesName,clientAddres,clientServiceName};
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
}
