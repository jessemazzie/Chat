package Client;

import Server.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Arrays;

public class Client extends JFrame implements ActionListener {
    CTS cts;
    LoginScreen loginScreen;
    boolean isLoggedIn = false;

    public static void main(String[] args) {
        new Client();
    }

    Client() {
        JPanel mainPanel;
        DefaultListModel<User> buddyList;
        JList<User> buddyJList;
        JScrollPane buddyScrollPane;
        Container cp;
        cp = getContentPane();
        while(cts == null) {
            try {
                cts = new CTS(this);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        buddyList = new DefaultListModel<User>();
        buddyJList = new JList<User>(buddyList);
        buddyScrollPane = new JScrollPane(buddyJList);

        mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(buddyScrollPane, BorderLayout.CENTER);
        mainPanel.add(newJButton("Connect", "CONNECT", this), BorderLayout.SOUTH);
        cp.add(mainPanel);
        setupMainFrame();
    }

    public static JButton newJButton(String label, String actionCommand, ActionListener al) {
        JButton tempButton = new JButton();

        tempButton.setText(label);
        tempButton.setActionCommand(actionCommand);
        tempButton.addActionListener(al);

        return tempButton;
    }


    void setupMainFrame() {
        Toolkit tk;
        Dimension d;

        tk = Toolkit.getDefaultToolkit();
        d = tk.getScreenSize();
        setSize(d.width/4, d.height/4);
        setLocation(d.width/4, d.height/4);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Chat");

        setVisible(true);
    }

    void connect() {
        loginScreen = new LoginScreen(cts, this);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();

        if(cmd.equals("CONNECT")) {
            connect();
        }
    }
}
