package Client;

import Server.User;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class Client extends JFrame implements ActionListener, MouseListener, ListSelectionListener, DropTargetListener {
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

    Buddy getBuddy(String buddyName) {
        for(int i = 0; i < buddyList.size(); i++) {
            if(buddyList.get(i).username.equals(buddyName))
                return buddyList.get(i);
        }

        return null; //if no buddy was found.
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
            if(buddyName != null) {
                if (buddyName.trim().isEmpty()) {
                    displayError("Buddy name must not be blank.", "Error");
                } else {
                    send("BUDDY_REQUEST " + buddyName + " " + cts.ID);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        System.out.println("It was the JList.");
        if(me.getClickCount() >= 2) { //using == sometimes causes bug that will not open window on accidental triple click.
            buddyList.get(buddyJList.getSelectedIndex()).chatWindow = new ChatWindow(buddyList.get(buddyJList.getSelectedIndex()).username, this); //TODO: Am I assigning the right username here?
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
    public void drop(DropTargetDropEvent dtde) {
        Transferable transferableData;

        transferableData = dtde.getTransferable();

    }

    /**
     * These methods needed to be implemented to be a MouseListener but there is no use for them in this class.
     */
    @Override
    public void mousePressed(MouseEvent me) {}

    @Override
    public void mouseReleased(MouseEvent me) {}

    @Override
    public void mouseEntered(MouseEvent me) {}

    @Override
    public void mouseExited(MouseEvent me) {}

    /**
     * These methods needed to be implemented to be a DropTargetListener but there is no use for them in this class.
     */
    @Override
    public void dragEnter(DropTargetDragEvent dtde) {}

    @Override
    public void dragOver(DropTargetDragEvent dtde) {}

    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {}

    @Override
    public void dragExit(DropTargetEvent dte) {}
}
