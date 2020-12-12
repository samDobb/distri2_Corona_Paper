import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ClientGUI implements FocusListener {
    JFrame mainFrame;
    JButton login;
    JButton stop;
    JButton sendCode;
    JLabel logs,us,qr,con;
    JLabel confirmationCode;
    JTextField userName;
    JTextField cateringInput;
    JPanel panel;

    public ClientGUI() {
        mainFrame= new JFrame("Horeca");
        login= new JButton("Register");
        stop = new JButton("Stop Visit");
        sendCode=new JButton("Send code");
        logs = new JLabel("Logs"); con=new JLabel("Confirmation: ");
        confirmationCode= new JLabel("Confirmation code will appear here");
        userName=new JTextField("Enter username");
        cateringInput=new JTextField("Give Horeca QR");
        panel=new JPanel();
    }


    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

    }
    public void setView(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        mainFrame.setBounds((int) toolkit.getScreenSize().getWidth()/8 , (int) toolkit.getScreenSize().getHeight()/8, (int) toolkit.getScreenSize().getWidth()/4 , (int) toolkit.getScreenSize().getHeight()/4 );
        Dimension d=new Dimension(100,30);
        Dimension d2=new Dimension(200,30);
        login.setPreferredSize(d);
        sendCode.setPreferredSize(d);
        userName.setPreferredSize(d2);
        cateringInput.setPreferredSize(d2);
        stop.setPreferredSize(d);

        GridBagLayout layout=new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints gbc=new GridBagConstraints();
        gbc.insets=new Insets(5,5,5,10);

        gbc.gridx=0;
        gbc.gridy=0;
        panel.add(userName,gbc);

        gbc.gridx=1;
        gbc.gridy=0;

        panel.add(login,gbc);

        gbc.gridx=0;
        gbc.gridy=1;

        panel.add(cateringInput,gbc);
        gbc.gridx=1;
        gbc.gridy=1;

        panel.add(sendCode,gbc);

        gbc.gridx=0;
        gbc.gridy=2;

        panel.add(con,gbc);

        gbc.gridx=0;
        gbc.gridy=3;

        panel.add(confirmationCode,gbc);

        gbc.gridx=0;
        gbc.gridy=4;

        panel.add(stop,gbc);

        mainFrame.setContentPane(panel);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }


}

