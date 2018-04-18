package Client;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginScreen extends JDialog implements ActionListener {
    Container cp;
    JTextField userNameField;
    JTextField passwordField;

    LoginScreen() {
        JPanel textFieldsPanel;
        JPanel buttonPanel;

        textFieldsPanel = new JPanel(new GridLayout(2, 2));

        userNameField = new JTextField();
        textFieldsPanel.add(new JLabel("Username: "));
        textFieldsPanel.add(userNameField);

        passwordField = new JTextField();
        textFieldsPanel.add(new JLabel("Password: "));
        textFieldsPanel.add(passwordField);

        buttonPanel = new JPanel(new BorderLayout());

        buttonPanel.add(Client.newJButton("Login", "LOGIN", this), BorderLayout.NORTH);
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

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setTitle("Login/Register");

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();

        if(cmd.equals("LOGIN")) {
            System.out.println("Attempting login...");
        } else if(cmd.equals("REGISTER")) {
            System.out.println("Attempting registration...");
        }
    }
}
