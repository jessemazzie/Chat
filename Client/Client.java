package Client;

import Server.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class Client extends JFrame implements ActionListener, MouseListener, ListSelectionListener {
    CTS cts;
    LoginScreen loginScreen;
    DefaultListModel<Buddy> buddyList;
    JButton removeBuddyButton;
    JList<Buddy> buddyJList;
    boolean isLoggedIn = false;

    public static void main(String[] args) {
        new Client();
    }

    Client() {
        JPanel mainPanel;
        JPanel buttonPanel;
        JScrollPane buddyScrollPane;
        Container cp;
        cp = getContentPane();

        buddyList = new DefaultListModel<Buddy>();
        buddyJList = new JList<Buddy>(buddyList);
        buddyScrollPane = new JScrollPane(buddyJList);

        buddyJList.addMouseListener(this);
        buddyJList.addListSelectionListener(this);

        removeBuddyButton = newJButton("Remove Buddy", "REMOVE_BUDDY", this);
        removeBuddyButton.setEnabled(false);

        buttonPanel = new JPanel(new GridLayout(0,3));
        buttonPanel.add(newJButton("Connect", "CONNECT", this));
        buttonPanel.add(newJButton("Add Buddy", "ADD_BUDDY", this));
        buttonPanel.add(removeBuddyButton); //TODO: Hide this if no buddy is selected.

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
        fromUsername = fromUsername.trim();
        String[] options = {"Accept", "Deny"};
        if(JOptionPane.showOptionDialog(this, fromUsername + " sent you a buddy request.", "Buddy request!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]) == 0) {
            buddyList.addElement(new Buddy(fromUsername));
            //TODO: Send acceptance confirmation
            send("BUDDY_REQUEST_ACCEPTED " + fromUsername + " " + cts.ID);
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

    void displayError(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();

        if(cmd.equals("CONNECT")) {
            connect();
        } else if(cmd.equals("ADD_BUDDY")) {
            String buddyName = JOptionPane.showInputDialog("Enter name of buddy to add.");
            if(buddyName.trim().isEmpty()) {
                displayError("Buddy name must not be blank.","Error");
            } else {
                send("BUDDY_REQUEST " + buddyName + " " + cts.ID);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        System.out.println("It was the JList.");
        if(me.getClickCount() >= 2) { //using == sometimes causes bug that will not open window on accidental triple click.
            buddyList.get(buddyJList.getSelectedIndex()).chatWindow = new ChatWindow("Temp");
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
        //TODO: Could do this in one line.
        if(buddyJList.isSelectionEmpty())
            removeBuddyButton.setEnabled(false);
        else
            removeBuddyButton.setEnabled(true);
    }

    @Override
    public void mousePressed(MouseEvent me) {}

    @Override
    public void mouseReleased(MouseEvent me) {}

    @Override
    public void mouseEntered(MouseEvent me) {}

    @Override
    public void mouseExited(MouseEvent me) {}
}
