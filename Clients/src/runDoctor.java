import java.util.Timer;

public class runDoctor {
    public static void main(String[] args){
        DoctorGUI gui=new DoctorGUI();
        gui.setView();
        DoctorDayScheduler dT=new DoctorDayScheduler(gui);
        Timer t= new Timer();
        t.schedule(dT,0,120000);
    }
}
