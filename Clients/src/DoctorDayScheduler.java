import java.util.TimerTask;

public class DoctorDayScheduler extends TimerTask {
    private DoctorGUI d;
    private boolean first;
    DoctorDayScheduler(DoctorGUI d){
        this.d=d;
        first=true;
    }
    @Override
    public void run(){
        if(!first){
            this.d.sendTheLogs();
        }
        else{
            first=false;
        }

    }
}
