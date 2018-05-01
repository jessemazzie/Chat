package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginScreen extends JDialog implements ActionListener {
    CTS cts;
    Client client;
    Container cp;
    JButton loginButton;
    JTextField userNameField;
    JPasswordField passwordField;

    LoginScreen(CTS cts, Client client) {
        JPanel textFieldsPanel;
        JPanel buttonPanel;

        this.cts = cts;
        this.client = client;

        textFieldsPanel = new JPanel(new GridLayout(2, 2));

        userNameField = new JTextField();
        textFieldsPanel.add(new JLabel("Username: "));
        textFieldsPanel.add(userNameField);

        passwordField = new JPasswordField();
        textFieldsPanel.add(new JLabel("Password: "));
        textFieldsPanel.add(passwordField);

        buttonPanel = new JPanel(new BorderLayout());

        loginButton = Client.newJButton("Login", "LOGIN", this);
        getRootPane().setDefaultButton(loginButton);
        buttonPanel.add(loginButton, BorderLayout.NORTH);
        buttonPanel.add(Client.newJButton("Register", "REGISTER", this), BorderLayout.SOUTH);

        cp = getContentPane();
        cp.add(textFieldsPanel, BorderLayout.NORTH);
        cp.add(buttonPanel, BorderLayout.SOUTH);

        setupMainFrame();
    }

    void setupMainFrame() {
        Toolkit tk;
        Dimension d;

        tk = Toolkit.getDefaultToolkit();
        d = tk.getScreenSize();
        setSize(250, 250);
        setLocation(d.width/4, d.height/4);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setTitle("Login/Register");

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();

        while(cts == null) {
            try {
                cts = new CTS(client, userNameField.getText());
                client.cts = cts;
            } catch (IOException ioe) {
                System.out.println("Failed to connect. Retrying...");
            }
        }

        if(cmd.equals("LOGIN")) {
            System.out.println("Attempting login...");
            try {
                cts.send("LOGIN " + userNameField.getText() + " " + passwordField.getText());
            } catch (IOException e) {
                System.out.println("Unable to send message from login screen to server");
            }
        } else if(cmd.equals("REGISTER")) {
            System.out.println("Attempting registration...");
            try {
                cts.send("REGISTER " + userNameField.getText() + " " + passwordField.getText());
            } catch (IOException e) {
                System.out.println("Unable to send message from registration screen to server");
            }
        }
    }
}
