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
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

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
        Timer timer;
        JPanel mainPanel;
        JPanel buttonPanel;
        JScrollPane buddyScrollPane;
        Container cp;
        cp = getContentPane();

        timer = new Timer(30000, this); //every 30 seconds
        timer.setActionCommand("REFRESH_FRIENDS_ONLINE_STATUS");
        timer.start();

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
        buttonPanel.add(removeBuddyButton);

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

    void receiveFile(String fileName, long size) {
        ServerSocket ss;
        Socket s;
        InputStream is;
        FileOutputStream fos;
        byte[] bytes;
        int numBytesRead = 0;

        try {
            ss = new ServerSocket(5000);
            s = ss.accept();

            is = s.getInputStream();
            fos = new FileOutputStream(fileName);

            bytes = new byte[256];
            numBytesRead = is.read(bytes);

            while(numBytesRead < size) {
                fos.write(bytes);
                numBytesRead += is.read(bytes);
            }

            fos.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        //s = ss.a
    }

    void sendFile(File f) {
        Socket s;
        FileInputStream fis;
        OutputStream os;
        byte[] bytes;
        int numBytesRead;

        bytes = new byte[256];

        try {
            fis = new FileInputStream(f);
            s = new Socket("127.0.0.1", 5000);

            os = s.getOutputStream();

            numBytesRead = fis.read(bytes);
            while (numBytesRead != 0) {
                os.write(bytes);

                numBytesRead = fis.read(bytes);
            }
        } catch (IOException ioe) {
           ioe.printStackTrace();
        }
    }

    void refreshOnlineStatuses() {
        for(int i = 0; i < buddyList.size(); i++) {
            send("ONLINE_STATUS " + buddyList.get(i).username);
        }

        buddyJList.repaint();
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
        } else if(cmd.equals("REFRESH_FRIENDS_ONLINE_STATUS")) {
            if(cts != null) {
                refreshOnlineStatuses();
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
}
