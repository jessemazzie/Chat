package Client;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginScreen extends JDialog implements ActionListener, DocumentListener {
    CTS cts;
    Client client;
    Container cp;
    JButton loginButton;
    JButton registerButton;
    JTextField userNameField;
    JPasswordField passwordField;

    LoginScreen(CTS cts, Client client) {
        JPanel textFieldsPanel;
        JPanel buttonPanel;

        this.cts = cts;
        this.client = client;

        textFieldsPanel = new JPanel(new GridLayout(2, 2));

        userNameField = new JTextField();
        userNameField.getDocument().addDocumentListener(this);
        textFieldsPanel.add(new JLabel("Username: "));
        textFieldsPanel.add(userNameField);

        passwordField = new JPasswordField();
        passwordField.getDocument().addDocumentListener(this);
        textFieldsPanel.add(new JLabel("Password: "));
        textFieldsPanel.add(passwordField);

        buttonPanel = new JPanel(new BorderLayout());

        loginButton = Client.newJButton("Login", "LOGIN", this);
        loginButton.setEnabled(false);
        getRootPane().setDefaultButton(loginButton);
        registerButton = Client.newJButton("Register", "REGISTER", this);
        registerButton.setEnabled(false);
        buttonPanel.add(loginButton, BorderLayout.NORTH);
        buttonPanel.add(registerButton, BorderLayout.SOUTH);

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

    void maybeToggleSendButton() {
        if (userNameField.getText().trim().isEmpty() || passwordField.getText().trim().isEmpty() || userNameField.getText().contains("\n") || passwordField.getText().contains("\n") || userNameField.getText().contains(" ") || passwordField.getText().contains(" ")) {
            loginButton.setEnabled(false);
            registerButton.setEnabled(false);
        } else {
            loginButton.setEnabled(true);
            registerButton.setEnabled(true);
        }
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

    @Override
    public void insertUpdate(DocumentEvent e) {
        maybeToggleSendButton();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        maybeToggleSendButton();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        maybeToggleSendButton();
    }
}
