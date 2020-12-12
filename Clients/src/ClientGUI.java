import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.rmi.RemoteException;

public class ClientGUI implements FocusListener, ActionListener {
    JFrame mainFrame;
    JButton login;
    JButton stop;
    JButton sendCode;
    JLabel logs,banner,us,qr,con,tel;
    JLabel confirmationCode;
    JTextField userName;
    JTextField cateringInput;
    JPanel panel;
    Boolean loggedIn;
    VisitorIMP v;

    public ClientGUI() {
        mainFrame= new JFrame("Horeca");
        login= new JButton("Register");
        stop = new JButton("Stop Visit");
        sendCode=new JButton("Send code");
        logs = new JLabel("Logs"); con=new JLabel("Confirmation: "); us=new JLabel("Register");banner=new JLabel("Signed in as");
        confirmationCode= new JLabel("Confirmation code will appear here");
        userName=new JTextField("Enter username");
        cateringInput=new JTextField("Give Horeca QR");
        panel=new JPanel();
        cateringInput.setEditable(false);
        banner.setVisible(false);
        loggedIn=false;

    }
    public void setUser(String username) throws RemoteException {
        //random nummer acts as phone number which is user specific
        char[] digits = "0123456789".toCharArray();
        char[] buf=new char[10];
        for (int i = 0; i < 10;i++){
            buf[i] = digits[(int) Math.random() *(digits.length)];
        }
        String tel=buf.toString();
        this.v=new VisitorIMP(username,tel);
        if(v.startClient()){
            System.out.println("Login succes");
            loggedIn=true;
            banner.setVisible(true);
            us.setText("username: "+ username+" tel: "+tel);
            cateringInput.setEditable(true);
            login.setVisible(false);
            userName.setVisible(false);
        }
        else{
            System.out.println("Some error happend");
        }

    }
    public QRcode newQRcode(String inputstring){
        String[] values = inputstring.split("_");
        if(values.length!=3){
            return null;
        }
        QRcode result=new QRcode(Integer.parseInt(values[0]),values[1],values[2]);
        return result;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==login){
            if(userName.getText().isEmpty() || userName.getText().equals("Enter username")){
                JOptionPane.showMessageDialog(mainFrame, "Please enter name first!");
            }
            else{
                try {
                    this.setUser(userName.getText());
                } catch (RemoteException remoteException) {
                    remoteException.printStackTrace();
                }
            }
        }
        if(e.getSource()==sendCode){
            if(loggedIn){
                QRcode qr=newQRcode(cateringInput.getText());
                if(qr==null){
                    System.out.println("An error occured while parsing the QRcode");
                }
                else{
                    this.v.readQr(qr);
                }
            }
        }
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
        panel.add(banner,gbc);
        gbc.gridx=0;
        gbc.gridy=1;
        panel.add(us,gbc);

        gbc.gridx=0;
        gbc.gridy=2;
        panel.add(userName,gbc);

        gbc.gridx=1;
        gbc.gridy=2;

        panel.add(login,gbc);

        gbc.gridx=0;
        gbc.gridy=3;

        panel.add(cateringInput,gbc);
        gbc.gridx=1;
        gbc.gridy=3;

        panel.add(sendCode,gbc);

        gbc.gridx=0;
        gbc.gridy=4;

        panel.add(con,gbc);

        gbc.gridx=0;
        gbc.gridy=5;

        panel.add(confirmationCode,gbc);

        gbc.gridx=0;
        gbc.gridy=6;

        panel.add(stop,gbc);

        mainFrame.setContentPane(panel);
        mainFrame.setVisible(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }


}

