import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class DoctorGUI implements FocusListener, ActionListener{

    JFrame mainFrame;

    JButton sendLogs;
    JButton readLogs;
    JPanel panel;
    Doctor doctor;

    public DoctorGUI(){
        try {
            doctor = new Doctor();
        }catch (Exception e){
            e.printStackTrace();
        }

        mainFrame = new JFrame("Doctor");
        sendLogs = new JButton("Send Infected Patient Logs");
        readLogs = new JButton("Read Patient Logs");

        panel = new JPanel();
    }

    public void readPatientLogs(){
        try {
            File file = new File("patientLogs.txt");

            Scanner myReader = new Scanner(file);

            List<ClientLog> logs=new ArrayList<>();

            SimpleDateFormat formatter=new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split("/");

                logs.add(new ClientLog(Integer.parseInt(data[0]),data[1],data[2],data[3],new Date(Long.parseLong(data[4])),new Date(Long.parseLong(data[5]))));
            }
            myReader.close();
            doctor.setLogs(logs);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==readLogs){
                readPatientLogs();
        }
        else if(e.getSource()==sendLogs){
            try{
                doctor.sendLogs();
            }catch (Exception exception){
                JOptionPane.showMessageDialog(mainFrame, "The logs could not be send");
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

    }

    public void setView() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        mainFrame.setBounds((int) toolkit.getScreenSize().getWidth() / 8, (int) toolkit.getScreenSize().getHeight() / 8, (int) toolkit.getScreenSize().getWidth() / 4, (int) toolkit.getScreenSize().getHeight() / 4);
        Dimension d = new Dimension(100, 30);
        Dimension d2 = new Dimension(200, 30);

        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 10);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(readLogs, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(sendLogs, gbc);


        mainFrame.setContentPane(panel);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        readLogs.addActionListener(this);
        sendLogs.addActionListener(this);
    }
}