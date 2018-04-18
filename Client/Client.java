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


    public static void main(String[] args) {
        new Client();
    }

    Client() {
        JPanel mainPanel;
        JPanel navigationPanel;
        DefaultListModel<User> buddyList;
        JList<User> buddyJList;
        JScrollPane buddyScrollPane;
        GridBagConstraints gbc = new GridBagConstraints();
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

        for(int i = 0; i < 15; i++)
            buddyList.addElement(new User("username" + i, ""));

        gbc.fill = GridBagConstraints.HORIZONTAL;
        //gbc.ipady = 20;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        mainPanel = new JPanel(new BorderLayout());
        navigationPanel = new JPanel(new GridBagLayout());
        navigationPanel.add(buddyScrollPane, gbc);
        navigationPanel.add(newJButton("Connect", "CONNECT"), gbc);

        mainPanel.add(navigationPanel, BorderLayout.WEST);
        //cp.add(loginRegisterPanel);
        cp.add(mainPanel);
        setupMainFrame();
    }

    JButton newJButton(String label, String actionCommand) {
        JButton tempButton = new JButton();

        tempButton.setText(label);
        tempButton.setActionCommand(actionCommand);

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

    @Override
    public void actionPerformed(ActionEvent ae) {
        String cmd = ae.getActionCommand();

        if(cmd.equals("LOGIN")) {
//            login();
        }
    }
}
