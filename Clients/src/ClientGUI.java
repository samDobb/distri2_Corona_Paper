import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class ClientGUI implements FocusListener, ActionListener {
    JFrame mainFrame;
    JButton login;
    JButton stop;
    JButton sendCode;
    JButton writeLogs;
    JLabel logs, banner, us, qr, con, sessionStatus;
    JLabel confirmationCode;
    JLabel informedSick;
    JTextField userName;
    JTextField cateringInput;
    JPanel panel;
    Boolean loggedIn, sessionRunning;
    Visitor v;


    public ClientGUI() {
        mainFrame = new JFrame("Horeca");
        login = new JButton("Register");
        stop = new JButton("Stop Visit");
        sendCode = new JButton("Send code");
        writeLogs = new JButton("Write Logs");
        logs = new JLabel("Logs");
        con = new JLabel("Confirmation: ");
        us = new JLabel("Register");
        banner = new JLabel("Signed in as");
        informedSick = new JLabel();
        confirmationCode = new JLabel("Confirmation code will appear here");
        sessionStatus = new JLabel("Current session is: Not Running");
        userName = new JTextField("Enter username");
        cateringInput = new JTextField("Give Horeca QR");
        panel = new JPanel();
        cateringInput.setEditable(false);
        banner.setVisible(false);
        loggedIn = false;

    }

    public void setInformed(){
        informedSick.setText("You are sick!!!");
    }

    public void setUser(String username) throws RemoteException {
        //random nummer acts as phone number which is user specific
        char[] digits = "0123456789".toCharArray();
        char[] buf = new char[10];
        for (int i = 0; i < 10; i++) {
            int randomInd = (int) (Math.random() * (digits.length));
            buf[i] = digits[randomInd];
        }
        String tel = new String(buf);
        System.out.println(tel);
        this.v = new Visitor(username, tel,this);
        System.out.println(this.v.toString());
        if (v.startClient()) {
            System.out.println("Login succes");
            loggedIn = true;
            banner.setVisible(true);
            us.setText("username: " + username + " tel: " + tel);
            cateringInput.setEditable(true);
            login.setVisible(false);
            userName.setVisible(false);
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Please choose another name!");
            System.out.println("Some error happend");
        }

    }

    public void writeDownLogs() {
        try {

            File file = new File(v.getUsername()+"Logs.txt");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }

            FileWriter writer = new FileWriter(file);
            List<ClientLog> logs=v.getLogs();

            for(ClientLog log:logs){
                writer.write(log.toString());
                writer.write(System.lineSeparator());
            }
            writer.close();

            JOptionPane.showMessageDialog(mainFrame, "The logs have been written to file");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public QRcode newQRcode(String inputstring) {
        String[] values = inputstring.split("_");
        if (values.length != 3) {
            return null;
        }
        byte[] s = values[2].getBytes(StandardCharsets.ISO_8859_1);
        QRcode result = new QRcode(Integer.parseInt(values[0]), values[1], values[2].getBytes(StandardCharsets.ISO_8859_1));
        System.out.println(result.toString());
        return result;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == login) {
            if (userName.getText().isEmpty() || userName.getText().equals("Enter username")) {
                JOptionPane.showMessageDialog(mainFrame, "Please enter name first!");
            } else {
                try {
                    this.setUser(userName.getText());
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }
        }
        if (e.getSource() == sendCode) {
            if (loggedIn) {
                //interpreter the string
                QRcode qr = newQRcode(cateringInput.getText());
                if (qr == null) {
                    JOptionPane.showMessageDialog(mainFrame, "An error while sending your QR code occured");
                    System.out.println("An error occured while parsing the QRcode");
                } else {
                    try {
                        String result = this.v.readQr(qr);
                        if (result != null) {
                            confirmationCode.setText(result);
                            sessionStatus.setText("Current session is: Running");
                            sessionRunning = true;
                            JOptionPane.showMessageDialog(mainFrame, "QR code sent!");
                        }
                    } catch (RemoteException remoteException) {
                        remoteException.printStackTrace();
                    }
                }
            }
        } else if (e.getSource() == writeLogs) {
            writeDownLogs();
        }
        if (e.getSource() == stop) {
            if (loggedIn && sessionRunning) {
                this.v.stopSession();
                sessionRunning = false;
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
        login.setPreferredSize(d);
        sendCode.setPreferredSize(d);
        userName.setPreferredSize(d2);
        cateringInput.setPreferredSize(d2);
        stop.setPreferredSize(d);

        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(banner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(us, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(userName, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        panel.add(login, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(cateringInput, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        panel.add(sendCode, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(con, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        panel.add(confirmationCode, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(stop, gbc);

        gbc.gridx = 1;
        gbc.gridy = 6;
        panel.add(informedSick, gbc);

        gbc.gridx = 2;
        gbc.gridy = 6;
        panel.add(writeLogs, gbc);


        mainFrame.setContentPane(panel);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.addActionListener(this);
        sendCode.addActionListener(this);
        stop.addActionListener(this);
        writeLogs.addActionListener(this);


    }


}

