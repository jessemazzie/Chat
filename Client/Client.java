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
    DefaultListModel<Buddy> buddyList;
    boolean isLoggedIn = false;

    public static void main(String[] args) {
        new Client();
    }

    Client() {
        JPanel mainPanel;
        JPanel buttonPanel;
        JList<Buddy> buddyJList;
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

        buddyList = new DefaultListModel<Buddy>();
        buddyJList = new JList<Buddy>(buddyList);
        buddyScrollPane = new JScrollPane(buddyJList);

        buttonPanel = new JPanel(new GridLayout(0,3));
        buttonPanel.add(newJButton("Connect", "CONNECT", this));
        buttonPanel.add(newJButton("Add Buddy", "ADD_BUDDY", this));
        buttonPanel.add(newJButton("Remove Buddy", "REMOVE_BUDDY", this)); //TODO: Hide this if no buddy is selected.

        mainPanel = new JPanel(new BorderLayout());

        mainPanel.add(buddyScrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
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

    void showBuddyRequest(String fromUsername) {
        String[] options = {"Accept", "Deny"};
        if(JOptionPane.showOptionDialog(null, fromUsername + " sent you a buddy request.", "Buddy request!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]) == 0) {
            buddyList.addElement(new Buddy(fromUsername));
            System.out.println("Buddy added.");
        }
    }

    void send(String msgToSend) {
        try {
            cts.send(msgToSend);
        } catch (IOException e) {
            cts = null;
        }
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();

        if(cmd.equals("CONNECT")) {
            connect();
        } else if(cmd.equals("ADD_BUDDY")) {
            String buddyName = JOptionPane.showInputDialog("Enter name of buddy to add.");
            if(buddyName.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Buddy name must not be blank.","Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try {
                    cts.send("BUDDY_REQUEST " + buddyName);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
