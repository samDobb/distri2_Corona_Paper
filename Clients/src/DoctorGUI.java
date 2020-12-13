import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class DoctorGUI implements FocusListener, ActionListener{

    JFrame mainFrame;

    JButton sendLogs;
    JButton readLogs;
    JPanel panel;
    JTextField fileName;
    JLabel fn;

    Doctor doctor;

    public DoctorGUI(){
        try {
            doctor = new Doctor();
        }catch (Exception e){
            e.printStackTrace();
        }

        mainFrame = new JFrame("Doctor");
        sendLogs = new JButton("Send");
        readLogs = new JButton("Read Patient Logs");
        fileName = new JTextField("Filename");
        fn=new JLabel("Filename: ");

        panel = new JPanel();
    }
    public void sendTheLogs(){
        this.doctor.sendLogs();
    }

    public void readPatientLogs(){
        try {
            if(fileName.getText().isEmpty()){
                JOptionPane.showMessageDialog(mainFrame, "Please give the filename");
                return;
            }
            File file = new File(fileName.getText()+".txt");

            Scanner myReader = new Scanner(file);

            List<ClientLog> logs=new ArrayList<>();

            while (myReader.hasNextLine()) {
                String[] data = myReader.nextLine().split("/");

                logs.add(new ClientLog(Integer.parseInt(data[0]),data[1],data[3],data[2], LocalDateTime.ofEpochSecond(Long.parseLong(data[4]), 0, ZoneOffset.UTC),LocalDateTime.ofEpochSecond(Long.parseLong(data[5]), 0, ZoneOffset.UTC)));
            }
            myReader.close();
            doctor.addLogs(logs);
            JOptionPane.showMessageDialog(mainFrame, "The logs have been read");
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainFrame, "There is no such file");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==readLogs){
                readPatientLogs();
        }
        else if(e.getSource()==sendLogs){
            try{
                if(doctor.getLogsSize()==0){
                    JOptionPane.showMessageDialog(mainFrame, "There are no logs available");
                }
                else JOptionPane.showMessageDialog(mainFrame, "Logs have been succesfully added!");
            }catch (Exception exception){
                JOptionPane.showMessageDialog(mainFrame, "The logs could not be send");
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if(e.getSource()==fileName&&fileName.getText().equals("Filename")){
            fileName.setText("");
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if(e.getSource()==fileName&&fileName.getText().equals("")){
            fileName.setText("Filename");
        }
    }

    public void setView() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        mainFrame.setBounds((int) toolkit.getScreenSize().getWidth() / 8, (int) toolkit.getScreenSize().getHeight() / 8, (int) toolkit.getScreenSize().getWidth() / 4, (int) toolkit.getScreenSize().getHeight() / 4);
        Dimension d = new Dimension(150, 30);
        Dimension d2 = new Dimension(200, 30);
        sendLogs.setPreferredSize(d);
        readLogs.setPreferredSize(d);
        fileName.setPreferredSize(d2);
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(fn, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(fileName, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(readLogs, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        panel.add(sendLogs, gbc);


        mainFrame.setContentPane(panel);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        readLogs.addActionListener(this);
        sendLogs.addActionListener(this);
        fileName.addFocusListener(this);
    }
}