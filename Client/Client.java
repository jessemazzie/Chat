package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

public class Client extends JFrame implements ActionListener {
    CTS cts;
    JPanel loginRegisterPanel;
    JTextField usernameField;
    JPasswordField passwordField;
    JButton registerButton;
    JButton loginButton;


    public static void main(String[] args) {
        new Client();
    }

    Client() {
        Container cp;
        cp = getContentPane();
//        while(cts == null) {
//            try {
//                cts = new CTS(this);
//            } catch (IOException ioe) {
//                ioe.printStackTrace();
//            }
//        }

        usernameField = new JTextField();
        usernameField.setSize(25, 1);

        passwordField = new JPasswordField();
        passwordField.setSize(25, 1);

        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        loginButton.setActionCommand("LOGIN");

        registerButton = new JButton("Register");
        registerButton.addActionListener(this);
        registerButton.setActionCommand("REGISTER");

        loginRegisterPanel = new JPanel(new GridLayout(3, 2, 3, 5));
        loginRegisterPanel.setVisible(true);

        loginRegisterPanel.add(new JLabel("Username: "));
        loginRegisterPanel.add(usernameField);

        loginRegisterPanel.add(new JLabel("Password: "));
        loginRegisterPanel.add(passwordField);

        loginRegisterPanel.add(loginButton);
        loginRegisterPanel.add(registerButton);

        cp.add(loginRegisterPanel);
        setupMainFrame();
    }


    void setupMainFrame() {
        Toolkit tk;
        Dimension d;

        tk = Toolkit.getDefaultToolkit();
        d = tk.getScreenSize();
        setSize(d.width/4, d.height/4);
        setLocation(d.width/4, d.height/4);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Login/Register");

        setVisible(true);
    }

    String getPasswordString() {
        String tempPassword = "";

        for(int i = 0; i < passwordField.getPassword().length; i++) {
            tempPassword += passwordField.getPassword()[i];
        }

        return tempPassword;
    }

    void login() {
        String username = usernameField.getText();
        String password = getPasswordString();

        try {
            cts.send("LOGIN " + username + " " + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void register() {
        String username = usernameField.getText();
        String password = getPasswordString();

        try {
            cts.send("REGISTER " + username + " " + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();

        if(cmd.equals("LOGIN")) {
            login();
        } else if(cmd.equals("REGISTER")) {
            register();
        }
    }
}
