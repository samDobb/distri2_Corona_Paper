import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
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
            if(matchingService.getCriticalLogs(logs)){
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
